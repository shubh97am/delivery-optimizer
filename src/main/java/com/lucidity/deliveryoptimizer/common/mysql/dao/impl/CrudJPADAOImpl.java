package com.lucidity.deliveryoptimizer.common.mysql.dao.impl;


import com.lucidity.deliveryoptimizer.common.mysql.dao.jpa.CrudJPADAO;
import com.lucidity.deliveryoptimizer.common.mysql.entity.BaseJPAEntity;
import com.lucidity.deliveryoptimizer.common.mysql.exception.MissingResourceException;
import com.lucidity.deliveryoptimizer.common.mysql.exception.UniqueKeyConstraintException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;


public class CrudJPADAOImpl<T extends BaseJPAEntity<ID>, ID extends Serializable> implements CrudJPADAO<T, ID> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(CrudJPADAOImpl.class);

    @PersistenceContext
    protected EntityManager entityManager;
    protected Class<T> entityClass = null;
    protected Class<ID> idClass = null;

    public CrudJPADAOImpl() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
        this.idClass = (Class<ID>) genericSuperclass.getActualTypeArguments()[1];
    }

    /**
     * @param entityManager the entityManager to set
     */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public T create(T entity) {
        try {
            entityManager.persist(entity);
            entityManager.flush();
        } catch (EntityExistsException ex) {
            throw new UniqueKeyConstraintException(ex);
        } catch (PersistenceException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof ConstraintViolationException) {
                if (cause.getCause() instanceof SQLIntegrityConstraintViolationException) {
                    Throwable cause2 = cause.getCause();
                    throw new UniqueKeyConstraintException(cause2);
                }
                throw new UniqueKeyConstraintException(cause);
            }
            throw ex;
        }
        return entity;
    }

    @Override
    public List<T> bulkCreate(List<T> entities) {
        List<T> createdEntities = new ArrayList<T>();
        try {
            entities.forEach(entity -> {
                entityManager.persist(entity);
                createdEntities.add(entity);
            });
            entityManager.flush();
        } catch (EntityExistsException ex) {
            throw new UniqueKeyConstraintException(ex);
        } catch (PersistenceException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof ConstraintViolationException) {
                if (cause.getCause() instanceof SQLIntegrityConstraintViolationException) {
                    Throwable cause2 = cause.getCause();
                    throw new UniqueKeyConstraintException(cause2);
                }
                throw new com.lucidity.deliveryoptimizer.common.mysql.exception.ConstraintViolationException(cause);
            }
            throw ex;
        }

        return createdEntities;
    }

    public T update(ID id, T entity) {
        // Need old state to perform some operations like Audit
		/*
		T oldEntity = findById(id);
        if (oldEntity == null) {
            throw new MissingResourceException();
        }
        */
        entity = entityManager.merge(entity);
        entityManager.flush();
        return entity;
    }

    @Override
    public List<T> update(List<T> entities) {
        List<T> updatedEntities = new ArrayList<T>();
        entities.forEach(entity -> {
            T oldEntity = findById(entity.getId());
            if (oldEntity == null) {
                throw new MissingResourceException();
            }
            entity = entityManager.merge(entity);
            updatedEntities.add(entity);
        });

        entityManager.flush();
        return updatedEntities;
    }

    public T findById(ID id) {
        return (T) entityManager.find(entityClass, id);
    }

    public List<T> findByIds(Set<ID> ids) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> rootEntry = query.from(entityClass);
        CriteriaQuery<T> all = query.select(rootEntry).where(rootEntry.get("id").in(ids));
        TypedQuery<T> allQuery = entityManager.createQuery(all);
        return allQuery.getResultList();
    }

    public List<T> search(String searchQuery, Integer offset, Integer limit,
                          String sortBy, String sortOrder) {
        return null;
    }

    public void detachEntity(T entity) {
        entityManager.detach(entity);
    }

    public void deleteById(ID id) {
        T entity = findById(id);
        if (entity == null) {
            throw new com.lucidity.deliveryoptimizer.common.mysql.exception.MissingResourceException();
        }
        entityManager.remove(entity);
    }

    public List<T> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> rootEntry = query.from(entityClass);
        CriteriaQuery<T> all = query.select(rootEntry);
        TypedQuery<T> allQuery = entityManager.createQuery(all);
        return allQuery.getResultList();
    }

    private Path getFieldPath(Root root, String fieldIdentifier) {
        boolean nestedField = fieldIdentifier.contains(".");
        String fieldName = fieldIdentifier;
        Join fetchJoin = null;
        while (fieldName.contains(".")) {
            if (fetchJoin == null) {
                fetchJoin = root.join(fieldName.split("\\.")[0]);
            } else {
                fetchJoin = fetchJoin.join(fieldName.split("\\.")[0]);
            }
            fieldName = fieldName.split("\\.", 2)[1];
        }
        Path orderingPath;
        if (nestedField) {
            orderingPath = fetchJoin.get(fieldName);
        } else {
            orderingPath = root.get(fieldIdentifier);
        }
        return orderingPath;
    }


    private Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
        Field field = null;
        while (clazz != null && field == null) {
            try {
                field = clazz.getDeclaredField(name);
            } catch (Exception e) {
            }
            clazz = clazz.getSuperclass();
        }
        if (field == null) {
            throw new NoSuchFieldException();
        }
        return field;
    }

    private Object getValueForField(String fieldIdentifier, String fieldValue, CriteriaBuilder criteriaBuilder) {
        Field field = null;
        Object value = null;
        try {
            String fieldName = fieldIdentifier;
            boolean nestedField = fieldName.contains(".");
            Class rootClass = entityClass;
            while (fieldName.contains(".")) {
                // ASSUMING ONLY SINGLE LEVEL
                // TODO: MultiLevel compatible
                field = getField(rootClass, fieldName.split("\\.")[0]);
                if (Collection.class.isAssignableFrom(field.getType())) {
                    ParameterizedType containerType = (ParameterizedType) field.getGenericType();
                    Class<?> containedClass = (Class<?>) containerType.getActualTypeArguments()[0];
                    field = getField(containedClass, fieldName.split("\\.")[1]);
                    rootClass = containedClass;
                } else {
                    field = getField(field.getType(), fieldName.split("\\.")[1]);
                }
                fieldName = fieldName.split("\\.", 2)[1];
            }
            if (!nestedField) {
                field = getField(entityClass, fieldName);
            }
        } catch (NoSuchFieldException e) {
            LOGGER.error("Unable to get field - {}. {}", fieldIdentifier, e);
        }
        Class fieldType = field.getType();
        if (fieldType.isEnum()) {
            value = Enum.valueOf(fieldType, fieldValue);
        } else if (fieldType.equals(Boolean.class)) {
            if (fieldValue.equals("1")) {
                fieldValue = "true";
            }
            value = Boolean.valueOf(fieldValue);
        } else if (fieldType.equals(Date.class)) {
            if (fieldValue.equalsIgnoreCase("now()")) {
                value = criteriaBuilder.currentDate();
            } else {
                value = new Date(Long.parseLong(fieldValue));
            }
        } else {
            value = fieldValue;
        }
        return value;
    }

}

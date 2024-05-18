use delivery;


CREATE TABLE `user`
(
    `id`               bigint(20)                              NOT NULL AUTO_INCREMENT,
    `name`             varchar(255)                            NOT NULL,
    `gender`           varchar(255) DEFAULT NULL,
    `age`              bigint(20)   DEFAULT NULL,
    `phone`            varchar(20)                             NOT NULL,
    `address_id`       bigint(20)   DEFAULT NULL,
    `created_by`       varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `created_on`       datetime                                NOT NULL,
    `last_modified_on` datetime                                NOT NULL,
    `version`          bigint(20)   DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE KEY `phone_idx` (`phone`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 8398
  DEFAULT CHARSET = utf8;
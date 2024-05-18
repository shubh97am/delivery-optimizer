use
delivery;

--     not adding unique constraint on phone here but we can add that constraint as extention
CREATE TABLE `restaurant`
(
    `id`               bigint(20) NOT NULL AUTO_INCREMENT,
    `name`             varchar(255)                            NOT NULL,
    `phone`            varchar(20)                             NOT NULL,
    `is_serviceable`      tinyint(1) DEFAULT NULL,
    `address_id`       bigint(20) DEFAULT NULL,
    `created_by`       varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `created_on`       datetime                                NOT NULL,
    `last_modified_on` datetime                                NOT NULL,
    `version`          bigint(20) DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 20000
  DEFAULT CHARSET = utf8;
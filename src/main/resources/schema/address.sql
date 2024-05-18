use delivery;



CREATE TABLE `address`
(
    `id`               bigint(20)                              NOT NULL AUTO_INCREMENT,
    `address`          longtext COLLATE utf8mb4_unicode_ci     NOT NULL,
    `latitude`         double                                  NOT NULL,
    `longitude`        double                                  NOT NULL,
    `created_by`       varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `created_on`       datetime                                NOT NULL,
    `last_modified_on` datetime                                NOT NULL,
    `version`          bigint(20) DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 8398
  DEFAULT CHARSET = utf8;

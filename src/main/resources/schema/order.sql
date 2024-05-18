use delivery;

CREATE TABLE `order`
(
    `id`                             bigint(20)                              NOT NULL AUTO_INCREMENT,
    `restaurant_id`                  bigint(20)                              NOT NULL,
    `restaurant_name`                varchar(255)                            NOT NULL,
    `restaurant_phone`               varchar(20) DEFAULT NULL,
    `restaurant_address`             longtext COLLATE utf8mb4_unicode_ci     NOT NULL,
    `restaurant_latitude`            double                                  NOT NULL,
    `restaurant_longitude`           double                                  NOT NULL,
    `min_time_to_prepare_in_minutes` bigint(20)  DEFAULT NULL,
    `user_id`                        bigint(20)                              NOT NULL,
    `user_name`                      varchar(255)                            NOT NULL,
    `user_phone`                     varchar(20) DEFAULT NULL,
    `user_address`                   longtext COLLATE utf8mb4_unicode_ci     NOT NULL,
    `user_latitude`                  double                                  NOT NULL,
    `user_longitude`                 double                                  NOT NULL,
    `order_status`                   varchar(20)                             NOT NULL,
    `assigned_on`                    datetime    DEFAULT NULL,
    `picked_on`                      datetime    DEFAULT NULL,
    `delivered_on`                   datetime    DEFAULT NULL,
    `delivery_agent_id`              bigint(20)  DEFAULT NULL,
    `created_by`                     varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `created_on`                     datetime                                NOT NULL,
    `last_modified_on`               datetime                                NOT NULL,
    `version`                        bigint(20)  DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `restaurant_idx` (`restaurant_id`),
    KEY `restaurant_orderstatus_idx` (`restaurant_id`, `order_status`),
    KEY `user_idx` (`user_id`),
    KEY `user_orderstatus_idx` (`user_id`, `order_status`),
    KEY `delivery_agent_idx` (`delivery_agent_id`),
    KEY `deliveryagent_orderstatus_idx` (`delivery_agent_id`, `order_status`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 50000
  DEFAULT CHARSET = utf8;

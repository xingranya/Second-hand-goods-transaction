-- 二手交易系统数据库初始化脚本
-- 适用数据库：MySQL 8.x
-- 导入后可用测试账号：
-- 1) seller01 / 123456
-- 2) buyer01  / 123456
-- 3) seller02 / 123456

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS `secondhand`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE `secondhand`;

CREATE TABLE IF NOT EXISTS `users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `phone_number` VARCHAR(255) DEFAULT NULL,
  `name` VARCHAR(255) DEFAULT NULL,
  `school` VARCHAR(255) DEFAULT NULL,
  `student_no` VARCHAR(255) DEFAULT NULL,
  `verified` BIT(1) NOT NULL DEFAULT b'0',
  `created_at` DATETIME DEFAULT NULL,
  `updated_at` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_users_username` (`username`),
  UNIQUE KEY `uk_users_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `products` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(1000) DEFAULT NULL,
  `price` DECIMAL(19,2) NOT NULL,
  `image_url` VARCHAR(255) DEFAULT NULL,
  `category` VARCHAR(255) DEFAULT NULL,
  `condition` VARCHAR(255) DEFAULT NULL,
  `seller_id` BIGINT NOT NULL,
  `status` VARCHAR(255) DEFAULT NULL,
  `created_at` DATETIME DEFAULT NULL,
  `updated_at` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_products_seller` (`seller_id`),
  CONSTRAINT `fk_products_seller` FOREIGN KEY (`seller_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `transactions` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `product_id` BIGINT NOT NULL,
  `buyer_id` BIGINT NOT NULL,
  `seller_id` BIGINT NOT NULL,
  `amount` DECIMAL(19,2) NOT NULL,
  `status` VARCHAR(255) NOT NULL,
  `created_at` DATETIME DEFAULT NULL,
  `completed_at` DATETIME DEFAULT NULL,
  `cancelled_at` DATETIME DEFAULT NULL,
  `note` VARCHAR(1000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_transactions_product` (`product_id`),
  KEY `idx_transactions_buyer` (`buyer_id`),
  KEY `idx_transactions_seller` (`seller_id`),
  CONSTRAINT `fk_transactions_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `fk_transactions_buyer` FOREIGN KEY (`buyer_id`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_transactions_seller` FOREIGN KEY (`seller_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `messages` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `sender_id` BIGINT NOT NULL,
  `receiver_id` BIGINT NOT NULL,
  `product_id` BIGINT DEFAULT NULL,
  `content` VARCHAR(1000) NOT NULL,
  `is_read` BIT(1) DEFAULT b'0',
  `created_at` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_messages_sender` (`sender_id`),
  KEY `idx_messages_receiver` (`receiver_id`),
  KEY `idx_messages_product` (`product_id`),
  CONSTRAINT `fk_messages_sender` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_messages_receiver` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_messages_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `reviews` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `product_id` BIGINT NOT NULL,
  `reviewer_id` BIGINT NOT NULL,
  `rating` INT NOT NULL,
  `comment` VARCHAR(1000) DEFAULT NULL,
  `created_at` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_reviews_product` (`product_id`),
  KEY `idx_reviews_reviewer` (`reviewer_id`),
  CONSTRAINT `fk_reviews_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `fk_reviews_reviewer` FOREIGN KEY (`reviewer_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `wanted_posts` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL,
  `expected_price` DECIMAL(19,2) DEFAULT NULL,
  `deadline` DATE DEFAULT NULL,
  `description` VARCHAR(1000) DEFAULT NULL,
  `campus` VARCHAR(255) DEFAULT NULL,
  `publisher_id` BIGINT NOT NULL,
  `created_at` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_wanted_publisher` (`publisher_id`),
  CONSTRAINT `fk_wanted_publisher` FOREIGN KEY (`publisher_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 清理脚本内置演示数据，便于重复导入
DELETE FROM `reviews` WHERE `id` IN (1, 2);
DELETE FROM `messages` WHERE `id` IN (1, 2, 3);
DELETE FROM `transactions` WHERE `id` IN (1, 2);
DELETE FROM `wanted_posts` WHERE `id` IN (1, 2);
DELETE FROM `products` WHERE `id` IN (1, 2, 3, 4);
DELETE FROM `users` WHERE `id` IN (1, 2, 3);

-- BCrypt(123456)
SET @PWD_HASH = '$2a$10$VreBkEVIIqIvynfocMCYruDyMRShpmj5ynkdNwKw94VEYsRWSRt9i';

INSERT INTO `users`
(`id`, `username`, `password`, `email`, `phone_number`, `name`, `school`, `student_no`, `verified`, `created_at`, `updated_at`)
VALUES
(1, 'seller01', @PWD_HASH, 'seller01@example.com', '13800000001', '张同学', '主校区', '20230001', b'1', NOW(), NOW()),
(2, 'buyer01',  @PWD_HASH, 'buyer01@example.com',  '13800000002', '李同学', '主校区', '20230002', b'1', NOW(), NOW()),
(3, 'seller02', @PWD_HASH, 'seller02@example.com', '13800000003', '王同学', '东校区', '20230003', b'0', NOW(), NOW());

INSERT INTO `products`
(`id`, `name`, `description`, `price`, `image_url`, `category`, `condition`, `seller_id`, `status`, `created_at`, `updated_at`)
VALUES
(1, '九成新机械键盘', 'Cherry 青轴，带原装线材和键帽。', 199.00, 'https://images.unsplash.com/photo-1517336714739-489689fd1ca8?w=1200', '电子产品', '9成新', 1, 'AVAILABLE', NOW(), NOW()),
(2, '考研数学全套资料', '含真题、笔记和讲义，几乎全新。', 88.00, 'https://images.unsplash.com/photo-1513258496099-48168024aec0?w=1200', '学习资料', '95新', 1, 'AVAILABLE', NOW(), NOW()),
(3, '二手显示器 24寸', '1080P，接口正常，无坏点。', 360.00, 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=1200', '数码设备', '8成新', 3, 'AVAILABLE', NOW(), NOW()),
(4, '宿舍折叠自行车', '可折叠，通勤代步，刹车灵敏。', 420.00, 'https://images.unsplash.com/photo-1485965120184-e220f721d03e?w=1200', '交通工具', '8成新', 3, 'SOLD', NOW(), NOW());

INSERT INTO `transactions`
(`id`, `product_id`, `buyer_id`, `seller_id`, `amount`, `status`, `created_at`, `completed_at`, `cancelled_at`, `note`)
VALUES
(1, 4, 2, 3, 420.00, 'COMPLETED', NOW(), NOW(), NULL, '历史已完成订单'),
(2, 1, 2, 1, 199.00, 'PENDING', NOW(), NULL, NULL, '待确认订单');

INSERT INTO `messages`
(`id`, `sender_id`, `receiver_id`, `product_id`, `content`, `is_read`, `created_at`)
VALUES
(1, 2, 1, 1, '你好，这个键盘还在吗？', b'1', NOW()),
(2, 1, 2, 1, '在的，可以校园内当面交易。', b'0', NOW()),
(3, 2, 3, 3, '显示器支持自提吗？', b'0', NOW());

INSERT INTO `wanted_posts`
(`id`, `title`, `expected_price`, `deadline`, `description`, `campus`, `publisher_id`, `created_at`)
VALUES
(1, '求购 iPad 手写笔', 180.00, DATE_ADD(CURDATE(), INTERVAL 20 DAY), '希望九成新以上，支持面交。', '主校区', 2, NOW()),
(2, '求购 程序设计教材', 40.00, DATE_ADD(CURDATE(), INTERVAL 10 DAY), '可接受少量笔记，不影响阅读。', '东校区', 2, NOW());

INSERT INTO `reviews`
(`id`, `product_id`, `reviewer_id`, `rating`, `comment`, `created_at`)
VALUES
(1, 4, 2, 5, '交易顺利，卖家回复及时。', NOW()),
(2, 4, 3, 5, '买家守时，沟通顺畅。', NOW());

SET FOREIGN_KEY_CHECKS = 1;

-- 创建数据库
CREATE DATABASE IF NOT EXISTS musicstore CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE musicstore;

-- 用户表
CREATE TABLE `user` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `username` VARCHAR(50) UNIQUE NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `email` VARCHAR(100),
  `role` ENUM('admin','user') DEFAULT 'user',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 音乐分类表
CREATE TABLE `category` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `description` VARCHAR(200)
);

-- 音乐表
CREATE TABLE `music` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `title` VARCHAR(100) NOT NULL,
  `artist` VARCHAR(100) NOT NULL,
  `category_id` INT,
  `album` VARCHAR(100),
  `duration` INT COMMENT '时长(秒)',
  `play_count` INT DEFAULT 0,
  `price` DECIMAL(10,2) DEFAULT 0.00,
  `audio_path` VARCHAR(255) NOT NULL,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) NOT NULL DEFAULT 0,
  FOREIGN KEY (`category_id`) REFERENCES `category`(`id`)
);

-- 收藏表
CREATE TABLE `favorite` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `user_id` INT,
  `music_id` INT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
  FOREIGN KEY (`music_id`) REFERENCES `music`(`id`)
);

-- 购物车表
CREATE TABLE `cart` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `user_id` INT,
  `music_id` INT,
  `quantity` INT DEFAULT 1,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
  FOREIGN KEY (`music_id`) REFERENCES `music`(`id`)
);

-- 订单表
CREATE TABLE `order` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `order_number` VARCHAR(100) UNIQUE NOT NULL,
  `user_id` INT NOT NULL,
  `total_amount` DECIMAL(10,2) NOT NULL,
  `status` ENUM('pending', 'paid', 'completed', 'cancelled') DEFAULT 'pending',
  `payment_method` VARCHAR(50),
  `shipping_address` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
);

-- 订单项表
CREATE TABLE `order_item` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `order_id` INT NOT NULL,
  `music_id` INT NOT NULL,
  `price` DECIMAL(10,2) NOT NULL,
  `quantity` INT NOT NULL DEFAULT 1,
  FOREIGN KEY (`order_id`) REFERENCES `order`(`id`),
  FOREIGN KEY (`music_id`) REFERENCES `music`(`id`)
);

-- 用户每日全曲试用表
CREATE TABLE `user_daily_trial` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `trial_date` DATE NOT NULL,
  `used` TINYINT(1) DEFAULT 0,
  UNIQUE KEY `uniq_user_date` (`user_id`, `trial_date`),
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
);

-- 网站信息表
CREATE TABLE IF NOT EXISTS `site_info` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `site_name` VARCHAR(100) DEFAULT '音乐商店',
  `customer_service_phone` VARCHAR(20) DEFAULT '400-123-4567',
  `zip_code` VARCHAR(10) DEFAULT '100000',
  `address` VARCHAR(200),
  `email` VARCHAR(100),
  `about_us` TEXT,
  `copyright` VARCHAR(200) DEFAULT '© 2025 音乐商店 版权所有'
);

-- 插入默认网站信息（如果表为空）
INSERT INTO site_info (site_name, customer_service_phone, zip_code, address, email, about_us, copyright) 
SELECT '音乐商店', '400-123-4567', '100000', '广州市天河区五山街道486号', 'service@musicstore.com', '专业的在线音乐销售平台', '© 2025 音乐商店 版权所有'
WHERE NOT EXISTS (SELECT 1 FROM site_info);

-- 插入分类
INSERT INTO category (name, description) VALUES 
('流行', '流行音乐'),
('摇滚', '摇滚音乐'),
('古典', '古典音乐'),
('电子', '电子音乐'),
('爵士', '爵士音乐');

-- 插入管理员用户
INSERT INTO user (username, password, email, role) VALUES 
('admin', 'e10adc3949ba59abbe56e057f20f883e', 'admin@music.com', 'admin'),
('user1', 'e10adc3949ba59abbe56e057f20f883e', 'user1@music.com', 'user');

-- 插入音乐数据（audio_path 使用示例文件名，存放于 audios 目录）
INSERT INTO music (title, artist, category_id, album, duration, price, audio_path) VALUES
('告白气球', '周杰伦', 1, '周杰伦的床边故事', 235, 2.99, 'gaobaiqiqiu.mp3'),
('海阔天空', 'Beyond', 2, '乐与怒', 326, 3.49, 'haikuotiankong.mp3'),
('月光奏鸣曲', '贝多芬', 3, '钢琴奏鸣曲集', 890, 4.99, 'yueguang.mp3'),
('Faded', 'Alan Walker', 4, 'Different World', 212, 2.49, 'faded.mp3'),
('Take Five', 'Dave Brubeck', 5, 'Time Out', 324, 3.99, 'takefive.mp3');
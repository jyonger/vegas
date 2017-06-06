-- 用户数据表
CREATE TABLE user_info(
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` VARCHAR(50) NOT NULL COMMENT '账号',
  `password` VARCHAR(50) NOT NULL COMMENT '密码',
  `email` VARCHAR(50) DEFAULT NULL COMMENT '注册邮箱',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `question` VARCHAR(50) DEFAULT NULL COMMENT '注册问题',
  `answer` VARCHAR(50) DEFAULT NULL COMMENT '问题答案',
  `role` INT(4) NOT NULL DEFAULT 1 COMMENT '角色  0.管理员 1.普通用户',
  `create_time` TIMESTAMP DEFAULT current_timestamp COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT current_timestamp COMMENT '最近更新',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name_unique`(`username`) USING BTREE
)ENGINE=InnoDB AUTO_INCREMENT 1000 DEFAULT CHARSET utf8;

-- 商品类型表
CREATE TABLE goods_category(
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `parent_id` INT(11) DEFAULT NULL COMMENT '0.根节点',
  `name` VARCHAR(50) DEFAULT NULL COMMENT '名称',
  `status` TINYINT(1) DEFAULT '1' COMMENT '状态 1.正常 2.废弃',
  `order` INT(4) DEFAULT NULL COMMENT '排序编号',
  `create_time` TIMESTAMP DEFAULT current_timestamp COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT current_timestamp COMMENT '最近更新',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB AUTO_INCREMENT 1000 DEFAULT CHARSET utf8;

-- 产品表
CREATE TABLE goods_info(
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `category_id` INT(11) NOT NULL COMMENT '类别id',
  `name` VARCHAR(100) NOT NULL COMMENT '名称',
  `title` VARCHAR(200) DEFAULT NULL COMMENT '标题',
  `main_image` VARCHAR(500) DEFAULT NULL COMMENT '主图 [URL]',
  `sub_images` TEXT COMMENT '相册 [JSON]',
  `detail` TEXT COMMENT '介绍',
  `price` DECIMAL(20,2) NOT NULL COMMENT '单价',
  `stock` INT(11) NOT NULL COMMENT '库存',
  `status` TINYINT(1) DEFAULT '2' COMMENT '状态 1.上架 2.下架 3.删除',
  `create_time` TIMESTAMP DEFAULT current_timestamp COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT current_timestamp COMMENT '最近更新',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB AUTO_INCREMENT 1000 DEFAULT CHARSET utf8;

-- 购物车表
CREATE TABLE user_cart(
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` INT(11) NOT NULL COMMENT '用户id',
  `goods_id` INT(11) NOT NULL COMMENT '商品id',
  `quantity` INT(11) DEFAULT '1' COMMENT '数量',
  `checked` INT(11) DEFAULT '1' COMMENT '是否勾选 1.未勾选 2.已勾选',
  `create_time` TIMESTAMP DEFAULT current_timestamp COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT current_timestamp COMMENT '最近更新',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`) USING  BTREE
)ENGINE=InnoDB AUTO_INCREMENT 1000 DEFAULT CHARSET utf8;

-- 支付信息表
CREATE TABLE pay_info(
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` INT(11) NOT NULL COMMENT '用户id',
  `order_no` BIGINT(20) NOT NULL COMMENT '订单号',
  `platform` TINYINT(1) DEFAULT NULL COMMENT '支付平台 1.支付宝 2.微信',
  `platform_number` VARCHAR(200) DEFAULT NULL COMMENT '支付平台流水号',
  `platform_status` VARCHAR(20) DEFAULT NULL COMMENT '支付状态',
  `create_time` TIMESTAMP DEFAULT current_timestamp COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT current_timestamp COMMENT '最近更新',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`) USING BTREE
)ENGINE=InnoDB AUTO_INCREMENT 1000 DEFAULT CHARSET utf8;

-- 订单表
CREATE TABLE order_info(
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` INT(11) NOT NULL COMMENT '用户id',
  `order_no` BIGINT(20) NOT NULL COMMENT '订单号',
  `address_id` INT(11) DEFAULT NULL COMMENT '地址id',
  `payment` DECIMAL(20,2) DEFAULT NULL COMMENT '实际支付金额',
  `payment_type` INT(4) DEFAULT NULL COMMENT '支付方式 1.在线支付',
  `postage` INT(10) DEFAULT NULL COMMENT '运费',
  `status` INT(10) DEFAULT NULL COMMENT '订单状态 0.已取消 10.未付款 20.已付款 40.已发货 50.交易成功 60.交易关闭',
  `payment_time` DATETIME DEFAULT NULL COMMENT '支付时间',
  `send_time` DATETIME DEFAULT NULL COMMENT '发货时间',
  `end_time` DATETIME DEFAULT NULL COMMENT '交易完成时间',
  `close_time` DATETIME DEFAULT NULL COMMENT '交易关闭时间',
  `create_time` TIMESTAMP DEFAULT current_timestamp COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT current_timestamp COMMENT '最近更新',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_no_index` (`order_no`) USING BTREE
)ENGINE=InnoDB AUTO_INCREMENT 1000 DEFAULT CHARSET utf8;

-- 订单详情表
CREATE TABLE order_item(
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` INT(11) DEFAULT NULL COMMENT '用户id',
  `order_no` BIGINT(20) DEFAULT NULL COMMENT '订单号',
  `goods_id` INT(11) NOT NULL COMMENT '商品id',
  `goods_name` VARCHAR(100) DEFAULT NULL COMMENT '商品名称',
  `goods_image` VARCHAR(500) DEFAULT NULL COMMENT '商品图片',
  `current_unit_price` DECIMAL(20,2) DEFAULT NULL COMMENT '下单时单价',
  `quantity` INT(11) DEFAULT NULL COMMENT '数量',
  `total_price` DECIMAL(20,2) DEFAULT NULL COMMENT '订单总额',
  `create_time` TIMESTAMP DEFAULT current_timestamp COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT current_timestamp COMMENT '最近更新',
  PRIMARY KEY (`id`),
  KEY `idx_order_no` (`order_no`) USING BTREE,
  KEY `idx_order_no_user_id` (`order_no`,`user_id`) USING BTREE
)ENGINE=InnoDB AUTO_INCREMENT 1000 DEFAULT CHARSET utf8;


CREATE TABLE user_address(
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` INT(11) NOT NULL COMMENT '用户id',
  `receiver_name` VARCHAR(20) DEFAULT NULL COMMENT '收货人姓名',
  `receiver_phone` VARCHAR(20) DEFAULT NULL COMMENT '收货人座机',
  `receiver_mobile` VARCHAR(20) DEFAULT NULL COMMENT '收货人手机',
  `receiver_province` VARCHAR(20) DEFAULT NULL COMMENT '省',
  `receiver_city` VARCHAR(20) DEFAULT NULL COMMENT '市',
  `receiver_district` VARCHAR(20) DEFAULT NULL COMMENT '区/县',
  `receiver_address` VARCHAR(200) DEFAULT NULL COMMENT '详细地址',
  `receiver_zip` VARCHAR(6) DEFAULT NULL COMMENT '邮编',
  `create_time` TIMESTAMP DEFAULT current_timestamp COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT current_timestamp COMMENT '最近更新',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB AUTO_INCREMENT 1000 DEFAULT CHARSET utf8;

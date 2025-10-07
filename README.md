# 在线音乐商店系统项目文档

## 项目简介

**项目名称**：在线音乐商店系统   
**项目类型**：基于 Springboot + MyBatis 的Web应用程序  
**主要功能**：

##### **管理员**

1. 添加、修改、删除⾳乐信息；添加、修改、删除⾳乐类别；查看⾳乐信息。

2. 订单处理、订单查询、订单删除。

3. 添加、修改、删除系统⽤户。 

4. ⽹站信息设置（包括⽹站名称，客服电话，邮编等）。

   ##### **用户**

   1. 注册新⽤户、登录、⽤户修改密码、浏览和查询⾳乐信息。

   2. ⽤户能收藏⾃⼰喜欢的⾳乐。

   3.   ⾳乐分类浏览、⾳乐详细信息。 

   4.  ⽤户能通过歌名、歌⼿、⾳乐分类、专辑名称等来搜索⾳乐。 

   5. 添加商品到购物⻋、购物⻋信息修改、下订单。

   6. 查询个⼈订单列表、查询某笔订单的详细信息。

      ##### 游客

      1. 游客能使⽤⾳乐商店（查看、搜索）。 
      2. 2. 当游客使⽤需要登录才能使⽤的功能（购买、收藏、加⼊购物⻋等）的时候，提 ⽰登录。

---

## 系统功能分析

### 角色权限设计

#### 1. 游客
- 浏览音乐列表
- 搜索音乐（按歌名、歌手、专辑、分类）
- 查看音乐详情
- 收藏/购买功能需登录

#### 2. 注册用户
- 用户注册/登录
- 浏览和搜索音乐
- 收藏喜欢的音乐
- 购物车管理（添加、修改、删除）
- 下单购买音乐
- 查看个人订单
- 个人资料管理
- 修改密码

#### 3. 管理员
- 音乐信息管理（增删改查）
- 音乐分类管理（增删改查）
- 用户账户管理
- 订单处理（状态更新、删除）
- 网站信息设置

### 核心功能模块

#### 用户管理模块
- 用户注册、登录、退出
- 个人资料修改
- 密码修改（MD5加密）
- 权限验证

#### 音乐管理模块
- 音乐信息CRUD操作
- 音乐分类管理
- 音乐搜索（多条件）
- 音乐详情查看

#### 购物流程模块
- 购物车管理
- 订单生成
- 订单状态跟踪
- 收藏功能

#### 后台管理模块
- 全功能数据管理
- 订单处理
- 网站配置

---

## 技术栈

### 后端技术
| 技术组件    | 版本  | 用途         |
| ----------- | ----- | ------------ |
| Spring Boot | 2.7.0 | 项目基础框架 |
| MyBatis     | 2.2.2 | ORM框架      |
| MySQL       | 8.0+  | 数据存储     |
| Maven       | 3.6+  | 项目构建     |

### 前端技术
| 技术组件   | 用途     |
| ---------- | -------- |
| Thymeleaf  | 模板引擎 |
| HTML5      | 页面结构 |
| CSS3       | 样式设计 |
| JavaScript | 交互逻辑 |

### 开发工具
- **IDE**：IntelliJ IDEA
- **数据库工具**：MySQL Workbench

---

## 数据库设计

### 数据库表结构

#### 用户表 (user)
```sql
CREATE TABLE `user` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `username` VARCHAR(50) UNIQUE NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `email` VARCHAR(100),
  `role` ENUM('admin','user') DEFAULT 'user',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

#### 音乐分类表 (category)
```sql
CREATE TABLE `category` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `description` VARCHAR(200)
);
```

#### 音乐表 (music)
```sql
CREATE TABLE `music` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `title` VARCHAR(100) NOT NULL,
  `artist` VARCHAR(100) NOT NULL,
  `category_id` INT,
  `album` VARCHAR(100),
  `duration` INT COMMENT '时长(秒)',
  `play_count` INT DEFAULT 0,
  `price` DECIMAL(10,2) DEFAULT 0.00,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`category_id`) REFERENCES `category`(`id`)
);
```

#### 收藏表 (favourite)
```sql
CREATE TABLE `favourite` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `user_id` INT,
  `music_id` INT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
  FOREIGN KEY (`music_id`) REFERENCES `music`(`id`)
);
```

#### 购物车表 (cart)
```sql
CREATE TABLE `cart` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `user_id` INT,
  `music_id` INT,
  `quantity` INT DEFAULT 1,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
  FOREIGN KEY (`music_id`) REFERENCES `music`(`id`)
);
```

#### 订单表 (order)
```sql
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
```

#### 订单项表 (order_item)
```sql
CREATE TABLE `order_item` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `order_id` INT NOT NULL,
  `music_id` INT NOT NULL,
  `price` DECIMAL(10,2) NOT NULL,
  `quantity` INT NOT NULL DEFAULT 1,
  FOREIGN KEY (`order_id`) REFERENCES `order`(`id`),
  FOREIGN KEY (`music_id`) REFERENCES `music`(`id`)
);
```

#### 网站信息表 (site_info)
```sql
CREATE TABLE `site_info` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `site_name` VARCHAR(100) DEFAULT '音乐商店',
  `customer_service_phone` VARCHAR(20) DEFAULT '400-123-4567',
  `zip_code` VARCHAR(10) DEFAULT '100000',
  `address` VARCHAR(200),
  `email` VARCHAR(100),
  `about_us` TEXT,
  `copyright` VARCHAR(200) DEFAULT '© 2025 音乐商店 版权所有'
);
```

### 数据关系图
```
用户(User) ←--- 收藏(Favorite) ---→ 音乐(Music)
   ↓                              ↑
购物车(Cart) ---→ 音乐(Music) ←--- 分类(Category)
   ↓
订单(Order) ---→ 订单项(OrderItem) ---→ 音乐(Music)
```

---

## 系统架构

### MVC架构模式
```
表示层 (View)
   ↓
控制层 (Controller)
   ↓
业务层 (Service)
   ↓
数据访问层 (Mapper)
   ↓
数据库 (MySQL)
```

### 包结构
```
src/main/java/com/music_store/
├── Controller/          # 控制器层
│   ├── UserController.java
│   ├── MusicController.java
│   ├── AdminController.java
│   ├── CartController.java
│   ├── OrderController.java
│   └── SiteInfoController.java
├── Entity/              # 实体类
│   ├── User.java
│   ├── Music.java
│   ├── Category.java
│   ├── Favourite.java
│   ├── Cart.java
│   ├── Order.java
│   └── SiteInfo.java
├── Dao/              # 数据访问层
│   ├── UserDao.java
│   ├── MusicDao.java
│   ├── CategoryDao.java
│   ├── FavouriteDao.java
│   ├── CartDao.java
│   ├── OrderDao.java
│   └── SiteInfoDao.java
├── Service/             # 业务逻辑层
│   ├── UserService.java
│   ├── MusicService.java
│   ├── CategoryService.java
│   ├── FavouriteService.java
│   ├── CartService.java
│   ├── OrderService.java
│   └── SiteInfoService.java
└── Config/              # 配置类
    ├── WebMvcConfig.java
    └── SiteInfoInterceptor.java
```

---

## 项目部署

### 环境要求
- **Java**: JDK 17+
- **MySQL**: 5.7+
- **Maven**: 3.6+
- **服务器**: Tomcat 9+

### 部署步骤

#### 1. 数据库准备
执行所提供的initialize.sql以创建数据库和初始数据

#### 2. 配置文件修改
**application.yml**

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/musicstore
    username: root
    password: // 对应的root密码
```

### 默认测试账户
| 角色     | 用户名 | 密码   | 权限           |
| -------- | ------ | ------ | -------------- |
| 管理员   | admin  | 123456 | 全系统管理权限 |
| 普通用户 | user1  | 123456 | 基础用户功能   |

---

## 性能优化

### 已实现的优化
- **数据库索引**：关键字段添加索引
- **连接池**：使用Druid连接池
- **缓存策略**：Thymeleaf模板缓存控制
- **SQL优化**：避免N+1查询问题

### 可进一步优化
- **Redis缓存**：热点数据缓存
- **CDN加速**：静态资源分发
- **数据库读写分离**：高并发场景
- **页面静态化**：商品详情页

---

## 安全考虑

### 安全措施
- **密码加密**：MD5不可逆加密
- **SQL注入防护**：MyBatis参数绑定
- **XSS防护**：Thymeleaf自动转义
- **权限验证**：操作前身份校验

### 安全建议
- 使用HTTPS协议
- 添加验证码机制
- 实现API限流
- 定期安全审计

---

## 项目特色

### 技术特色
1. **代码规范**：遵循MVC设计模式
2. **易于扩展**：模块化架构设计

### 功能特色
1. **完整的电商流程**：浏览→收藏→购物车→订单
2. **多角色权限系统**：清晰的权限划分
3. **响应式管理后台**：管理员友好界面
4. **个性化用户体验**：收藏、搜索等功能

---

## 已知问题与改进方向

### 当前限制
- 前端界面较为简单
- 缺少支付接口集成
- 无音乐试听功能
- 缺少数据统计分析

### 改进计划
1. **前端美化**：使用现代UI框架
2. **功能扩展**：评论、评分、推荐系统
3. **性能提升**：引入缓存和异步处理
4. **移动端适配**：开发响应式或原生App

# 二手物品交易管理系统

这是一个基于Spring Boot的二手物品交易管理系统，提供用户注册、登录、物品发布、搜索、交易等功能。

## 技术栈

- 后端：Spring Boot 2.7.0
- 数据库：MySQL 8.0
- 安全框架：Spring Security
- JWT认证
- Lombok

## 主要功能

1. 用户管理
   - 用户注册
   - 用户登录
   - 个人信息管理

2. 物品管理
   - 物品发布
   - 物品搜索
   - 物品详情查看
   - 物品状态管理

3. 交易管理
   - 交易发起
   - 交易确认
   - 交易完成
   - 交易评价

## 项目结构

```
src/main/java/com/secondhand/
├── config/          # 配置类
├── controller/      # 控制器
├── model/          # 实体类
├── repository/     # 数据访问层
├── service/        # 业务逻辑层
├── security/       # 安全相关
└── util/           # 工具类
```

## 开发环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+

## 快速开始

1. 克隆项目
2. 配置数据库
   - 创建数据库：secondhand
   - 修改application.yml中的数据库配置
3. 运行项目
   ```bash
   mvn spring-boot:run
   ```

## API文档

API文档将在开发过程中逐步完善。

## 贡献指南

1. Fork 项目
2. 创建特性分支
3. 提交代码
4. 创建Pull Request

## 许可证

MIT License 
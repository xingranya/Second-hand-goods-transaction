# 校园二手交易平台

这是一个面向校园场景的二手交易平台，覆盖用户端交易闭环与基础管理端能力，技术栈保持为 `Vue 3 + Pinia + Spring Boot + JPA + JWT`。

## 文档入口
- 项目 Wiki 首页：[`wiki/Home.md`](./wiki/Home.md)
- 接口说明：[`API.md`](./API.md)
- 前端页面说明：[`FRONTEND_DEMO.md`](./FRONTEND_DEMO.md)
- 后端说明：[`server/README.md`](./server/README.md)
- 数据初始化脚本：[`server/sql/init.sql`](./server/sql/init.sql)

## 当前能力概览
- 用户端支持首页浏览、搜索筛选、商品详情、消息沟通、订单推进、评价与实名认证。
- 登录页已升级为更完整的项目入口页，并联动响应式用户状态。
- 管理端支持管理员登录、系统统计、商品管理、用户管理和订单管理。
- 后端已统一认证失败提示、异常输出与多类 DTO 返回结构。

## 默认账号
- 普通用户：`alice / 123456`
- 普通用户：`bob / 123456`
- 管理员：`admin / 123456`

## 快速定位
- 如果你在写论文，优先看 [`wiki/01-系统总览.md`](./wiki/01-%E7%B3%BB%E7%BB%9F%E6%80%BB%E8%A7%88.md)
- 如果你在写技术实现，优先看 [`wiki/02-前端架构.md`](./wiki/02-%E5%89%8D%E7%AB%AF%E6%9E%B6%E6%9E%84.md) 和 [`wiki/03-后端架构.md`](./wiki/03-%E5%90%8E%E7%AB%AF%E6%9E%B6%E6%9E%84.md)
- 如果你在写数据库设计，优先看 [`wiki/04-数据库设计.md`](./wiki/04-%E6%95%B0%E6%8D%AE%E5%BA%93%E8%AE%BE%E8%AE%A1.md)
- 如果你在写系统测试与项目验证内容，优先看 [`wiki/08-测试与答辩支撑.md`](./wiki/08-%E6%B5%8B%E8%AF%95%E4%B8%8E%E7%AD%94%E8%BE%A9%E6%94%AF%E6%92%91.md)

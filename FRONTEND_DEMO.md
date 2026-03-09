# 校园二手交易前端演示说明

## 启动前准备
- 安装依赖：`npm install`
- 本地开发：`npm run dev`
- 生产构建：`npm run build`

## 环境变量
- `VITE_API_BASE_URL`：前端请求基地址，默认 `/api`
- `VITE_API_TARGET`：Vite 代理目标地址，默认 `http://localhost:8080`

## 演示路由
- `/` 首页
- `/search` 搜索筛选
- `/product/:id` 商品详情
- `/publish` 商品发布
- `/messages` 消息中心
- `/wanted` 求购广场
- `/order/:id` 订单详情
- `/profile` 个人中心
- `/verify` 实名认证

## 演示链路
1. 首页 -> 搜索筛选 -> 商品详情 -> 发起聊天 -> 订单详情
2. 个人中心 -> 发布商品 -> 返回首页查看最新发布
3. 求购广场 -> 联系发布者 -> 消息沟通

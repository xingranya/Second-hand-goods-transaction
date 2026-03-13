# 校园二手交易平台接口说明

## 1. 说明
- 接口基础前缀：`/api`
- 鉴权方式：`Authorization: Bearer <token>`
- 返回格式：
  - 成功时默认直接返回业务 DTO 或列表
  - 失败时统一返回：

```json
{
  "message": "错误说明"
}
```

## 2. 认证接口

| 方法 | 路径 | 鉴权 | 说明 |
| --- | --- | --- | --- |
| `POST` | `/api/auth/login` | 否 | 用户登录 |
| `POST` | `/api/auth/register` | 否 | 用户注册 |

### 2.1 登录请求

```json
{
  "username": "alice",
  "password": "123456"
}
```

### 2.2 登录响应

```json
{
  "token": "jwt-token",
  "username": "alice",
  "role": "USER"
}
```

## 3. 系统接口

| 方法 | 路径 | 鉴权 | 说明 |
| --- | --- | --- | --- |
| `GET` | `/api/system/db-health` | 否 | 数据库健康检查 |
| `GET` | `/api/system/summary` | 否 | 首页公共摘要数据 |

### 3.1 `/api/system/summary` 响应字段
- `userCount`
- `productCount`
- `wantedCount`
- `orderCount`
- `completedOrderCount`

## 4. 用户接口

| 方法 | 路径 | 鉴权 | 说明 |
| --- | --- | --- | --- |
| `GET` | `/api/users/me` | 是 | 获取当前用户资料与聚合数据 |
| `POST` | `/api/users/verify` | 是 | 提交实名认证 |

### 4.1 `/api/users/me` 响应字段
- `id`
- `username`
- `name`
- `school`
- `verified`
- `role`
- `enabled`
- `publishCount`
- `soldCount`
- `wantedCount`
- `orderCount`
- `unreadMessageCount`
- `favoriteCount`
- `latestOrderId`

## 5. 商品接口

| 方法 | 路径 | 鉴权 | 说明 |
| --- | --- | --- | --- |
| `POST` | `/api/products` | 是 | 发布商品 |
| `GET` | `/api/products` | 否 | 商品列表，支持筛选 |
| `GET` | `/api/products/{id}` | 否 | 商品详情 |
| `GET` | `/api/products/search` | 否 | 按关键字搜索商品 |
| `GET` | `/api/products/category/{category}` | 否 | 按分类查询 |
| `GET` | `/api/products/price-range` | 否 | 按价格区间查询 |
| `GET` | `/api/products/seller/{sellerId}` | 否 | 查询卖家发布商品 |
| `PUT` | `/api/products/{id}` | 是 | 修改商品 |
| `DELETE` | `/api/products/{id}` | 是 | 删除商品 |
| `PATCH` | `/api/products/{id}/status` | 是 | 修改商品状态 |

### 5.1 商品列表筛选参数
- `keyword`
- `campus`
- `sort`
  - 支持 `priceAsc`、`priceDesc`
- `status`

### 5.2 商品 DTO 关键字段
- `id`
- `name`
- `description`
- `price`
- `originalPrice`
- `imageUrl`
- `category`
- `condition`
- `campus`
- `status`
- `createdAt`
- `seller`

## 6. 订单接口

| 方法 | 路径 | 鉴权 | 说明 |
| --- | --- | --- | --- |
| `POST` | `/api/orders` | 是 | 创建订单 |
| `GET` | `/api/orders/{id}` | 是 | 获取订单详情，支持 `o-1` 形式 ID |
| `GET` | `/api/orders/my` | 是 | 获取当前用户相关订单 |
| `POST` | `/api/orders/{id}/next-step` | 是 | 推进订单状态 |

### 6.1 创建订单请求

```json
{
  "productId": 1
}
```

### 6.2 订单 DTO 关键字段
- `id`
- `status`
- `items`
- `totalAmount`
- `buyer`
- `seller`
- `paymentLabel`
- `createdAt`

## 7. 求购接口

| 方法 | 路径 | 鉴权 | 说明 |
| --- | --- | --- | --- |
| `GET` | `/api/wanted` | 否 | 获取求购列表 |
| `POST` | `/api/wanted` | 是 | 创建求购帖 |

### 7.1 创建求购请求

```json
{
  "title": "求购 iPad",
  "expectedPrice": 1800,
  "description": "优先 64G 国行",
  "campus": "主校区",
  "deadline": "2026-03-30"
}
```

## 8. 消息接口

| 方法 | 路径 | 鉴权 | 说明 |
| --- | --- | --- | --- |
| `GET` | `/api/messages/conversations` | 是 | 获取会话列表 |
| `GET` | `/api/messages/conversations/{peerUserId}` | 是 | 获取与某用户的会话消息 |
| `POST` | `/api/messages/conversations/{peerUserId}` | 是 | 发送会话消息 |
| `GET` | `/api/messages/{id}` | 是 | 获取单条消息详情 |
| `GET` | `/api/messages/user/{userId}` | 是 | 获取某用户相关消息，管理员可查看他人 |
| `GET` | `/api/messages/conversation` | 是 | 按两个用户查询会话 |
| `GET` | `/api/messages/product/{productId}` | 是 | 管理员查看某商品消息汇总 |
| `PATCH` | `/api/messages/{id}/read` | 是 | 标记单条消息已读 |
| `PATCH` | `/api/messages/user/{userId}/read-all` | 是 | 标记用户全部消息已读 |
| `GET` | `/api/messages/user/{userId}/unread-count` | 是 | 获取未读消息数 |
| `DELETE` | `/api/messages/{id}` | 是 | 删除消息 |

### 8.1 发送消息请求

```json
{
  "content": "这件商品还在吗？",
  "productId": 1
}
```

### 8.2 消息详情 DTO 关键字段
- `id`
- `senderId`
- `senderName`
- `receiverId`
- `receiverName`
- `productId`
- `productName`
- `content`
- `read`
- `createdAt`

## 9. 评价接口

| 方法 | 路径 | 鉴权 | 说明 |
| --- | --- | --- | --- |
| `POST` | `/api/reviews` | 是 | 创建评价 |
| `GET` | `/api/reviews/{id}` | 否 | 查看评价详情 |
| `GET` | `/api/reviews/product/{productId}` | 否 | 查看商品评价列表 |
| `GET` | `/api/reviews/user/{userId}` | 否 | 查看用户评价列表 |
| `GET` | `/api/reviews/product/{productId}/rating` | 否 | 获取商品平均评分 |
| `GET` | `/api/reviews/product/{productId}/count` | 否 | 获取商品评价数 |
| `PUT` | `/api/reviews/{id}` | 是 | 修改自己的评价 |
| `DELETE` | `/api/reviews/{id}` | 是 | 删除评价，管理员可删除任意评价 |
| `GET` | `/api/reviews/check` | 否 | 检查用户是否评价过商品 |

### 9.1 创建评价请求

```json
{
  "productId": 1,
  "rating": 5,
  "comment": "商品与描述一致，交易顺利。"
}
```

## 10. 管理端接口

所有管理端接口均要求管理员身份访问，统一前缀为 `/api/admin`。

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| `GET` | `/api/admin/dashboard/stats` | 获取后台统计概览 |
| `GET` | `/api/admin/products` | 获取后台商品列表 |
| `PATCH` | `/api/admin/products/{id}/status` | 修改商品状态 |
| `DELETE` | `/api/admin/products/{id}` | 删除商品 |
| `GET` | `/api/admin/users` | 获取后台用户列表 |
| `PATCH` | `/api/admin/users/{id}/enabled` | 启用或禁用用户 |
| `PATCH` | `/api/admin/users/{id}/verify` | 修改用户认证状态 |
| `GET` | `/api/admin/orders` | 获取后台订单列表 |

### 10.1 后台统计 DTO
- `userCount`
- `productCount`
- `wantedCount`
- `orderCount`
- `completedOrderCount`
- `verifiedUserCount`
- `availableProductCount`

### 10.2 后台商品查询参数
- `keyword`
- `status`

### 10.3 后台用户查询参数
- `keyword`
- `role`
- `enabled`

### 10.4 后台订单查询参数
- `status`

## 11. 典型错误场景

| 场景 | HTTP 状态码 | 返回示例 |
| --- | --- | --- |
| 未登录或 token 失效 | `401` | `{ "message": "未登录或登录已过期" }` |
| 权限不足 | `403` | `{ "message": "无权执行该操作" }` |
| 参数校验失败 | `400` | `{ "message": "请求参数不合法" }` |
| 用户名或密码错误 | `401` | `{ "message": "用户名或密码错误" }` |
| 账号被禁用 | `403` | `{ "message": "账号已被禁用，请联系管理员" }` |

## 12. 使用建议
- 普通用户可使用 `alice / 123456` 或 `bob / 123456`
- 管理员可使用 `admin / 123456`
- 建议按“首页摘要 -> 登录 -> 搜索下单 -> 个人中心 -> 管理后台”顺序体验主要功能

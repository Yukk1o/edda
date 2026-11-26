# 接口契约 v0

所有接口的约定。改动这里等于改动契约，需要同步更新对应的 issue 与测试。

## 通用约定

| 项 | 约定 |
|---|---|
| 返回结构 | `{ code, message, data }` |
| 业务失败 | HTTP 仍为 200，`code != 200`。传输层错误才用 4xx/5xx |
| 金额 | **字符串**，两位小数（`"279.00"`）。JSON number 在 JS 里会掉精度 |
| 时间 | ISO-8601 带时区（`2026-11-26T19:45:12+08:00`） |
| 分页 | `{ page, size, total, items }`，page 从 1 开始 |
| 请求标识 | 响应头返回 `X-Request-Id`，用于排查 |
| 鉴权 | `Authorization: Bearer <session>` |
| 幂等 | 写接口用 `Idempotency-Key` 请求头（不是请求体） |

## 错误码

`code = HTTP 语义 × 1000 + 序号`。

| code | 含义 | 客户端应做什么 |
|---|---|---|
| 400001 | 参数不合法 | 修正请求 |
| 400004 | 发言过于频繁 | 等待后重试 |
| 401001 | 未登录 | 跳登录 |
| 403001 | 无权限 | 不重试 |
| 404002 | 课程不存在 | 不重试 |
| 409001 | 名额售罄 | 不重试，提示 |
| 409002 | 优惠券不可用 | 不重试，提示换券 |
| 409004 | 订单状态不允许该操作 | 刷新订单状态 |
| 409005 | 已拥有该课程 | 跳"我的课程" |
| 500001 | 系统繁忙 | 可重试（带退避） |

---

## 1. 登录（验证码）

```
POST /api/v1/auth/login/sms
{ "phone": "13800000000", "code": "123456" }
→ { "code":200, "data": { "token":"...", "userId":1001, "expireAt":"..." } }
```

规则：验证码 5 分钟有效、一次性；同手机号 60 秒内限 1 次发送；错误码 `401001`。

## 2. 课程列表

```
GET /api/v1/courses?categoryId=1&page=1&size=20
→ { "code":200, "data": { "page":1, "size":20, "total":8, "items":[{ id, title, coverUrl, teacherName, minPrice }] } }
```

规则：只返回在售课程；`minPrice` 取该课程所有在售班期的最低价。

## 3. 课程详情（含班期）

```
GET /api/v1/courses/{courseId}
→ data: { id, title, subtitle, teacherName, description, lessons:[{id,title,durationSeconds,freePreview}], packages:[{id,name,price,originalPrice,seatLeft,saleStart,saleEnd,status}] }
```

规则：无购买也返回详情与课时列表，但**不返回播放地址**；`seatLeft` 为实时可售数。

## 4. 券试算（纯读）

```
POST /api/v1/coupons/quote
{ "packageId": 1001 }
→ data: { "usable": true, "best": { "userCouponId":2001, "discount":"20.00", "name":"满100减20" }, "options":[...] }
```

规则：**纯读，连续调用 10 次券状态不变**；只统计有效期内、范围内、门槛满足的券；
`best` 为优惠金额最大者，同额取先到期者。

## 5. 下单

```
POST /api/v1/orders
Header: Idempotency-Key: <1~64 字符>
{ "packageId": 1001, "userCouponId": 2001 }
→ data: { "orderNo":"...", "totalAmount":"299.00", "discountAmount":"20.00", "payAmount":"279.00", "expireAt":"..." }
```

规则：

- 缺幂等键 → `400001`
- 同键重复 → 返回**原订单**，`code=200`
- 名额不足 → `409001`，名额不变
- 券不可用 → `409002`，**名额必须已回滚**
- 已拥有课程 → `409005`
- 有效期 15 分钟，超时转 `CLOSED`（见 08 阶段）

## 6. 支付回调

```
POST /api/v1/payments/{channel}/callback
{ "orderNo":"...", "tradeNo":"...", "amount":"279.00", "sign":"..." }
→ { "code":200, "data": true }
```

规则：验签失败拒绝；同 `(channel, tradeNo)` 重复投递只生效一次并返回成功；
金额与订单实付不一致时拒绝并告警；关单后的回调走「异常收款」通道，不改订单状态。

## 7. 取消订单

```
POST /api/v1/orders/{orderNo}/cancel
→ data: true
```

规则：仅 `CREATED` 可取消；取消后立即释放名额、退回券；重复取消返回 `409004`。

## 8. 我的课程

```
GET /api/v1/my/enrollments?page=1&size=20
→ items: [{ courseId, title, coverUrl, enrolledAt, progressPercent }]
```

规则：`progressPercent` 由服务端计算（已看秒数 / 课时总时长），**不接受客户端上报**；
退款后授权撤销的课程不再出现。

## 变更记录

| 日期 | 变更 |
|---|---|
| 2026-11-26 | 初版：8 个接口 |

# Edda

一个卖课站。目标不是功能多，而是**每个决策都能说清代价，每个不变量都有可执行的验收**。

> 名字取自北欧神话里的《埃达》——知识与诗篇之书。
>
> 当前状态：**M0 骨架已完成**。服务可启动，模块边界由 ArchUnit 守，业务代码尚未开始。

## 从这里开始读

| 顺序 | 文档 | 花多久 |
|---|---|---|
| 1 | [`docs/contracts/invariants.md`](docs/contracts/invariants.md) | 5 分钟，七条不能破的规则 |
| 2 | [`docs/roadmap.md`](docs/roadmap.md) | 10 分钟，从单体演进到微服务的完整路线 |
| 3 | [`docs/decisions/0001-模块化单体起步.md`](docs/decisions/0001-模块化单体起步.md) ~ `0003` | 10 分钟，架构层面的取舍 |
| 4 | [`docs/contracts/api-v0.md`](docs/contracts/api-v0.md) | 10 分钟，接口与错误码 |

其余文档按需查，不需要通读。

## 文档结构

```
docs/
├── roadmap.md     演进路线：功能 / 组件 / 形态三条线怎么交叉推进
├── decisions/     ADR：决策 + 代价 + 否决的替代方案（只增不改）
├── contracts/     契约：不变量、接口、错误码
└── practice/      阶段与 issue：实现顺序与验收（可忽略，不影响理解项目）
```

三类文档的生命周期不同，不要混写：

- **decisions** 记录"为什么"，改它要慎重，且必须新增而不是覆盖
- **contracts** 是别人照着写代码的依据，改动要同步 issue 与测试
- **practice** 是一次性的，做完即归档

## 核心约束（先看这三条，能省你很多争论）

1. **起步是单体，终点是微服务**。先保持"可拆性"（ArchUnit 守边界），再按 [`roadmap`](docs/roadmap.md) 逐个拆；先拆叶子、最后拆编排者
2. **不提前引组件**。有状态中间件必须过四问（见 `0006`），且同一时刻只允许一项新组件处于试用期
3. **订单状态只管钱**，交付状态独立（见 `0003`）

## 范围边界

### 定位外，不做

实物商品、物流、发票、多租户、多语言。与"卖课"这个定位冲突，不是排期问题。

### 现在不做，以后会做

拼团、秒杀、分销、会员等级。**它们不在 v0，不是永不做** —— 每一项都有自己的入场券：

| 能力 | 入场券 | 会牵动什么 |
|---|---|---|
| 秒杀 | 单班期出现并发抢购，或需要在固定时刻放量 | 名额预扣 + 队列，会触发 Redis / MQ 的准入评审 |
| 拼团 | 运营要按"成团"计价 | 订单模型要支持一单多人，是结构性改动 |
| 分销 | 有真实推广者，需要结算与提现 | 新增结算域 + 对账，涉及资金，风险最高 |
| 会员等级 | 复购率低于 20%，需要留存手段 | 券与定价要接权益，影响试算链路 |

规矩不变：**每引入一项，先写一条 ADR**（背景 / 决策 / 代价 / 替代方案），再动代码。
尤其是秒杀和分销 —— 一个会引入中间件，一个会动钱。

## 运行

数据库有两种接法，二选一。

### 方式 A 连自己的 MySQL 服务器（推荐）

```bash
cp src/main/resources/application-local.yml.example src/main/resources/application-local.yml
# 改里面的地址与账号,该文件已被 gitignore
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

要求：库已建好，字符集 `utf8mb4`，账号具备该库的全部权限。

### 方式 B 用本地容器

```bash
docker compose up -d
docker compose ps        # 等 edda-mysql 变成 healthy
mvn spring-boot:run
```

**目标数据库是 MySQL 8.x**。本地容器暂时跑 **MariaDB 11.4**，原因写在 compose 文件注释里（MySQL 镜像的大层在当前网络下多个加速站都拉不动）。
两者协议与驱动兼容，DDL 已避开 MySQL 专有排序规则、也未使用 MariaDB 专有写法，换回 MySQL 只需改 `image` 与几个环境变量。

### 验证

```bash
curl -i http://localhost:8080/actuator/health   # 期望 200 且 status 为 UP
```

响应头里应该带 `X-Request-Id`，日志格式里也应带同一个 id。

## 许可

暂未声明。

# 🌟 Gforo - Forum Project Deployment Guide | Gforo 论坛项目部署指南

This guide helps you set up and run the Gforo forum project including backend, database, Redis, Kafka, and frontend locally.  
本指南将帮助你本地部署并运行 Gforo 论坛项目，包括后端、数据库、Redis、Kafka 和前端部分。

---

## 🛠 Deployment Instructions | 部署说明

### 📦 Backend + Infrastructure Setup | 后端与基础设施配置

#### English
1. Ensure [Docker](https://www.docker.com/products/docker-desktop) and [Docker Compose](https://docs.docker.com/compose/) are installed.
2. Navigate to the deployment directory:
   ```bash
   cd ./deploy
   ```
3. Start all backend and infrastructure services:
   ```bash
   docker compose up -d
   ```
4. The backend will start and listen on port `80` by default.

#### 中文
1. 请确保已安装 [Docker](https://www.docker.com/products/docker-desktop) 和 [Docker Compose](https://docs.docker.com/compose/)。
2. 进入部署目录：
   ```bash
   cd ./deploy
   ```
3. 启动后端与基础设施容器：
   ```bash
   docker compose up -d
   ```
4. 默认后端服务监听 `80` 端口。

---

### 💻 Frontend Setup | 前端部署

#### English
1. Clone the frontend project to your local machine:
   ```bash
   git clone https://github.com/Gabri177/gforo-frontend.git
   ```
2. Replace the default frontend environment configuration:
   ```bash
   cp ./deploy/frontend/.env.development ./gforo-frontend/.env.development
   ```
3. Navigate to the frontend directory and run the dev server:
   ```bash
   cd ./gforo-frontend
   npm install
   npm run dev
   ```

#### 中文
1. 克隆前端项目到本地：
   ```bash
   git clone https://github.com/Gabri177/gforo-frontend.git
   ```
2. 替换默认的前端环境变量配置文件：
   ```bash
   cp ./deploy/frontend/.env.development ./gforo-frontend/.env.development
   ```
3. 进入前端目录并运行开发服务器：
   ```bash
   cd ./gforo-frontend
   npm install
   npm run dev
   ```

---

## ✅ Done! | 部署完成！

#### English
Your application will be running at:  
**http://localhost:5173**

#### 中文
你的应用将运行在：  
**http://localhost:5173**

---

# 🧱 Gforo Project Architecture Overview | Gforo 项目架构概览

## 📘 Project Description | 项目简介

Gforo is a modular community forum system built with Spring Boot. It features real-time WebSocket chat, role/permission management, experience-based titles, Kafka messaging, Redis caching, Elasticsearch-based search, and a frontend written in Vue3 + Element Plus with dynamic module loading and permission control.  
Gforo 是一个基于 Spring Boot 构建的社区论坛系统，支持模块化开发、前后台分离，集成了 WebSocket 实时通信、权限管理、经验值系统、Kafka 消息队列、Redis 缓存、Elasticsearch 搜索等功能。前端使用 Vue3 + Element Plus，支持动态模块加载和权限控制。

---

## 🧩 Module Overview | 模块构成

| Module | Description (English) | 描述（中文） |
|--------|------------------------|--------------|
| `auth` | Authentication & Account Activation | 登录注册、邮箱验证、忘记密码流程 |
| `user` | User Profile, Level, Exp System | 用户信息、用户等级、经验变动、修改资料等 |
| `post` | Post Management | 发帖、分页加载、详情查看 |
| `comment` | Nested Comments | 评论与子评论嵌套结构、分页加载 |
| `like` | Like System | 点赞功能（帖子/评论）|
| `notification` | Notification System | 系统与互动通知（点赞/评论/私信）|
| `title` | Title System Based on Experience | 经验称号系统、自动授予称号、用户称号管理 |
| `report` | Report & Moderation | 举报功能、后台处理、忽略等操作 |
| `permission` | Role-Based Access Control | 角色权限系统、论坛管理员控制板块权限 |
| `chat` | Private Messaging via WebSocket | 私信系统，基于 Netty WebSocket 实现 |
| `layout` | Homepage Layout Elements | 首页轮播图配置等页面组件 |
| `statistic` | User Activity Statistics | 活跃用户统计、日/月度趋势分析 |
| `search` | Post Search Engine | 基于 Elasticsearch 的帖子搜索 |

---

## 🏗️ Backend Layered Structure | 后端分层结构

```
src
└── main
    ├── java/com/yugao
    │   ├── config             → Configurations (Kafka, Redis, Security) | 配置模块
    │   ├── controller         → REST API Controllers | 控制器层
    │   ├── service
    │   │   ├── business       → Core Business Logic | 业务逻辑
    │   │   └── data           → Data Access Services | 数据访问
    │   ├── mapper             → MyBatis-Plus Mappers | Mapper 接口
    │   ├── domain             → Entity Models | 实体类
    │   ├── dto / vo           → Request / Response Models | 请求 / 响应对象
    │   ├── converter          → DTO <-> Entity <-> VO Converters | 数据转换器
    │   ├── netty              → WebSocket Communication | 实时通信模块
    │   ├── event              → Kafka Producer/Consumer | 事件模块
    │   ├── enums / constants  → Enums & Constants | 枚举与常量
    │   ├── exception / result → Error Handling & Response Wrapping | 异常处理
    │   ├── util / validator   → Utils & Custom Validators | 工具类与校验器
    │   └── security           → Security User Context | 安全认证上下文
    └── resources
        ├── application.yml
        └── templates / static
```

---

## 🔐 Permission & Authentication System | 权限与认证机制设计

### 🧩 Fine-Grained Role-Based Access Control (RBAC) | 多权限细分设计（四表分离）

The system adopts a fine-grained RBAC model with four tables to manage user access.  
系统采用细粒度权限控制模型，通过四张核心表实现用户权限管理。

| Table | English | 中文 |
|-------|---------|------|
| `permission` | Permission resource registry | 权限资源表（按钮、菜单、接口）|
| `role` | Role types: admin, moderator, user | 角色表（管理员、版主、普通用户）|
| `role_permission` | Role-permission mapping | 角色与权限的关联关系 |
| `user_role` | User-role mapping | 用户与角色的关联关系 |

### ✅ Supported Features | 支持功能

- Custom role/permission configuration by admin  
  管理员自定义角色与权限组合
- Board-specific moderator permissions  
  可扩展板块管理权限（每个板块设版主）
- `@PreAuthorize` annotations on controller methods  
  接口使用 `@PreAuthorize` 进行权限控制
- Dynamic frontend rendering based on permissions  
  支持前端按钮权限动态渲染

---

### 🪪 Two-Token Authentication Strategy | 双令牌身份认证机制

The system uses Access + Refresh tokens, validated via JWT and managed with Redis.  
系统采用 Access Token + Refresh Token 双令牌机制，结合 Redis 会话控制。

| Token Type | Purpose | 说明 |
|------------|---------|------|
| Access Token | Short-lived (e.g. 30 min), used for API access | 有效期短，用于接口访问 |
| Refresh Token | Longer-lived (e.g. 7 days), used to renew access token | 有效期长，用于刷新 |
| Redis Session | Stores device/session info | Redis记录设备登录状态 |

### 🔁 Flow | 登录流程

1. Issue access + refresh tokens after login  
   登录成功后发放双令牌  
2. Access token used for API calls  
   后续接口携带 access token  
3. If expired, use refresh token to get new one  
   过期后通过 refresh token 获取新令牌  
4. Redis controls max session/device count  
   Redis 控制最多同时登录设备数  

### 🛠 Technical Implementation | 技术细节

- JWT signing/validation via `JwtUtil`
- Redis stores token sessions with expiration
- `LoginUser` extends Spring Security's `UserDetails`
- Refresh token handled automatically via filter

---

### 📊 Flow Diagram | 认证流程示意图

```text
[ User ] ⇄ [ Login API ]
          ⇩ Access + Refresh Tokens
       [ Frontend Stores Tokens ]
             ⇅
    [ API with Access Token ] ──▶ [ Spring Security ]
                                └─▶ Validate JWT + Redis
                                    ↳ Allow / Deny
             ⇵
     [ Refresh Token API ] ──▶ Refresh if access expired
```

---

## 🔬 Test Structure | 测试结构

```
src/test/java/com/yugao
├── controller          → Controller Tests | 控制层测试
├── service             → Service & DAO Unit Tests | 服务层测试
├── util / log          → Utility Tests | 工具类测试
└── GforoApplicationTests.java → Full Integration Test | 启动测试
```

---

## 🔧 Tech Stack | 技术栈

| Layer | Stack |
|-------|-------|
| Backend | Spring Boot, MyBatis-Plus, Spring Security |
| Realtime | Netty WebSocket |
| Messaging | Apache Kafka |
| Cache | Redis |
| Search | Elasticsearch |
| Database | MySQL 8 |
| Frontend | Vue 3, Vite, Element Plus, TailwindCSS |
| Deployment | Docker + Docker Compose |


---

## 🔗 Live Demo | 在线体验地址

You can try out the deployed version of Gforo here:  
你可以在以下地址体验已部署的 Gforo 论坛：

🌐 **[https://foro.mistsiete.com/](https://foro.mistsiete.com/)**

> ⚠️ Note: Some admin functionalities may be restricted in the demo version.  
> ⚠️ 注意：线上演示版本部分管理功能可能受限。

### 🧪 Admin Test Account | 管理员体验账号

- **Username | 用户名**: `AdminUser`  
- **Password | 密码**: `333333`


## 📦 项目展示

<img width="1339" alt="iShot_2025-05-25_04 14 37" src="https://github.com/user-attachments/assets/22f56f55-f354-48b7-b51d-edef327c2462" />
<img width="1356" alt="iShot_2025-05-25_04 14 45" src="https://github.com/user-attachments/assets/236c515e-09c5-4f14-b3eb-288f6bfac171" />
<img width="1393" alt="iShot_2025-05-25_04 14 55" src="https://github.com/user-attachments/assets/8919c936-3b7c-4c2f-9aba-6b28f198f22f" />
<img width="1393" alt="iShot_2025-05-25_04 15 32" src="https://github.com/user-attachments/assets/fe145f84-3a8f-4a13-9f21-57ca971c70b5" />
<img width="1393" alt="iShot_2025-05-25_04 16 06" src="https://github.com/user-attachments/assets/9435d6e7-144a-4319-ba68-b41b5b9ba4e7" />
<img width="617" alt="iShot_2025-05-28_02 09 24" src="https://github.com/user-attachments/assets/e4db9365-71cb-43a4-98aa-0a415d433bd8" />
<img width="1096" alt="iShot_2025-05-28_02 10 01" src="https://github.com/user-attachments/assets/4e0155b7-811b-4ccc-b0e6-39b1688c8f85" />
<img width="1003" alt="iShot_2025-05-28_02 10 56" src="https://github.com/user-attachments/assets/6a03b291-5846-429d-8f7a-af8203dab082" />
<img width="864" alt="iShot_2025-05-28_02 10 42" src="https://github.com/user-attachments/assets/9547cd90-cc2f-4d50-98f3-bf16e0ab4a18" />
<img width="930" alt="iShot_2025-05-28_02 10 11" src="https://github.com/user-attachments/assets/a4eb049f-ae09-4880-95df-c5b4749abfcd" />
<img width="1430" alt="iShot_2025-05-25_04 17 00" src="https://github.com/user-attachments/assets/7f795e14-b0a4-45ff-900a-c19393a65c1e" />
<img width="1393" alt="iShot_2025-05-25_04 16 25" src="https://github.com/user-attachments/assets/e75d8e66-5597-4595-90f9-4887fa4aa2ee" />





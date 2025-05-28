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

# 🧱 Gforo 项目架构概览 | Gforo Project Architecture Overview

## 📘 项目简介 | Project Description

Gforo 是一个基于 Spring Boot 构建的社区论坛系统，支持模块化开发、前后台分离，集成了 WebSocket 实时通信、权限管理、经验值系统、Kafka 消息队列、Redis 缓存、Elasticsearch 搜索等功能。前端使用 Vue3 + Element Plus，支持动态模块加载和权限控制。

Gforo is a modular community forum system built with Spring Boot. It features real-time WebSocket chat, role/permission management, experience-based titles, Kafka messaging, Redis caching, Elasticsearch-based search, and a frontend written in Vue3 + Element Plus with dynamic module loading and permission control.

---

## 🧩 模块构成 | Module Overview

模块划分清晰、职责单一，支持可插拔式扩展：

| 模块名 | 功能说明 | 英文说明 |
|--------|----------|----------|
| `auth` | 登录注册、邮箱验证、忘记密码流程 | Authentication & Account Activation |
| `user` | 用户信息、用户等级、经验变动、修改资料等 | User Profile, Level, Exp System |
| `post` | 发帖、分页加载、详情查看 | Post Management |
| `comment` | 评论与子评论嵌套结构、分页加载 | Nested Comments |
| `like` | 点赞功能（帖子/评论）| Like System |
| `notification` | 系统与互动通知（点赞/评论/私信）| Notification System |
| `title` | 经验称号系统、自动授予称号、用户称号管理 | Title System Based on Experience |
| `report` | 举报功能、后台处理、忽略等操作 | Report & Moderation |
| `permission` | 角色权限系统、论坛管理员控制板块权限 | Role-Based Access Control |
| `chat` | 私信系统，基于 Netty WebSocket 实现 | Private Messaging via WebSocket |
| `layout` | 首页轮播图配置等页面组件 | Homepage Carousel, Layout Elements |
| `statistic` | 活跃用户统计、日/月度趋势分析 | User Activity Statistics |
| `search` | 基于 Elasticsearch 的帖子搜索 | Post Search Engine |

---

## 🏗️ 后端分层结构 | Backend Layered Structure

```
src
└── main
    ├── java/com/yugao
    │   ├── config             → 第三方框架配置（Kafka、Redis、Security）
    │   ├── controller         → 控制器层，REST API入口
    │   ├── service
    │   │   ├── business       → 核心业务逻辑（封装流程/规则）
    │   │   └── data           → 具体数据操作服务（数据库交互）
    │   ├── mapper             → MyBatis-Plus 的 Mapper 接口
    │   ├── domain             → 实体类（聚合根，对应数据库表）
    │   ├── dto / vo           → 输入/输出对象
    │   ├── converter          → DTO ↔ Entity ↔ VO 转换器
    │   ├── netty              → WebSocket 实现模块（连接管理、消息调度）
    │   ├── event              → Kafka 事件系统（Producer/Consumer）
    │   ├── enums / constants  → 枚举与常量定义
    │   ├── exception / result → 通用异常与响应格式封装
    │   ├── util / validator   → 工具类与参数校验器
    │   └── security           → Spring Security 用户上下文实现
    └── resources
        ├── application.yml
        └── templates / static
```
---

## 🔐 权限与认证机制设计 | Permission & Authentication System

---

### 🧩 1. 多权限细分设计（四表分离）  
### Fine-Grained Role-Based Access Control (RBAC)

本系统采用细粒度权限控制，基于「角色-权限」中间表建模，扩展出四张权限核心表，实现灵活分配与动态扩展。

> The project uses a fine-grained Role-Based Access Control (RBAC) mechanism using 4 tables to support flexible, extensible, and hierarchical permission control.

#### 📄 权限相关四张核心表 | Four Core Permission Tables

| 表名 | 说明 | 英文解释 |
|------|------|-----------|
| `permission` | 权限资源表（按钮、菜单、接口）| Permission resource registry |
| `role` | 角色表（管理员、版主、普通用户等）| Role types: admin, moderator, user |
| `role_permission` | 角色与权限的关联关系 | Role-permission mapping table |
| `user_role` | 用户与角色的关联关系 | User-role assignment table |

#### 🧠 支持功能 | Supported Features
- 管理员自定义角色与权限组合
- 可扩展板块管理权限（如每个板块设置不同版主）
- 接口权限标注使用 `@PreAuthorize`
- 支持前端动态按钮权限渲染（基于传回的权限标识码）

---

### 🪪 2. Two Token 身份认证机制  
### Secure Two-Token Authentication Strategy

为提升安全性与用户体验，本系统采用「Access Token + Refresh Token」双令牌机制，配合 Redis 管理会话状态。

> To improve security and usability, the system implements a Two-Token authentication strategy using JWT + Redis for token validation and session control.

#### 🧱 核心组成 | Core Components

| 名称 | 说明 |
|------|------|
| **Access Token** | 有效期较短（默认30分钟），用于访问受保护接口 |
| **Refresh Token** | 有效期较长（默认7天），用于刷新 Access Token |
| **Redis Session** | 存储用户设备信息 + 过期时间，用于控制会话数量及踢出设备 |

#### 🔁 登录流程 | Login Flow

1. 用户登录成功后服务器颁发 Access Token 与 Refresh Token。
2. Access Token 放入响应头/LocalStorage，用于后续接口访问。
3. 每次请求后台校验 JWT 是否有效 + 是否在 Redis 中已登记。
4. 若 Access Token 过期，可携带 Refresh Token 请求刷新。
5. Redis 可控制：
   - 限制用户同时在线设备数（如最多3个）
   - 手动登出/管理员强制踢人

#### 📦 技术细节 | Implementation

- 使用 `JwtUtil` 工具类进行签发与解析
- 使用 Redis ZSet 记录设备 session 信息（支持过期控制、排序）
- 集成 Spring Security，自定义 `LoginUser` 作为认证载体
- 支持前后端统一拦截器刷新令牌（无感续期）

---

### ✅ 整体示意图 | Overall Structure (Text Form)

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

## 🔬 测试结构 | Test Structure

```
src/test/java/com/yugao
├── controller          → 控制层测试（接口验证）
├── service             → 业务与数据层单元测试
├── util / log          → 工具类与日志测试
├── GforoApplicationTests.java → 启动与集成测试
```

---

## 🔧 技术栈 | Tech Stack

| 层级 | 技术栈 |
|------|--------|
| 后端核心 | Spring Boot, MyBatis-Plus, Spring Security |
| 实时通信 | Netty WebSocket |
| 消息队列 | Apache Kafka |
| 缓存 | Redis |
| 搜索 | Elasticsearch |
| 数据库 | MySQL 8 |
| 前端 | Vue 3, Vite, Element Plus, TailwindCSS |
| 构建部署 | Docker + Docker Compose |

---



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
![iShot_2025-05-23_11 30 34](https://github.com/user-attachments/assets/2ed615c0-c6af-46ec-9ead-1f642592fa26)





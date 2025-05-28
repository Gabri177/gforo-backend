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
**http://localhost:5173

#### 中文
你的应用将运行在：  
**http://localhost:5173

# 🧱 Gforo 项目架构概览 | Gforo Project Architecture Overview

## 📘 项目简介 | Project Description

Gforo 是一个基于 Spring Boot 构建的社区论坛系统，支持模块化开发、前后台分离，集成了 WebSocket 实时通信、权限管理、经验值系统、Kafka 消息队列、Redis 缓存、Elasticsearch 搜索等功能。前端使用 Vue3 + Element Plus，支持动态模块加载和权限控制。

Gforo is a modular community forum system built with Spring Boot. It features real-time WebSocket chat, role/permission management, experience-based titles, Kafka messaging, Redis caching, Elasticsearch-based search, and a frontend written in Vue3 + Element Plus with dynamic module loading and permission control.

---

## 🏗️ 后端分层结构 | Backend Layered Structure

```
src
└── main
    ├── java/com/yugao
    │   ├── GforoApplication.java             // 启动类 | Main Application
    │   ├── config                            // 第三方组件配置 | Configurations (Redis, Kafka, MyBatisPlus, Security, etc.)
    │   ├── constants                         // 全局常量定义 | Constant Enums and Keys
    │   ├── controller                        // 控制器层（含 admin, post, user 等）| REST Controllers (incl. admin/post/user...)
    │   ├── converter                         // DTO/Entity/VO 转换器 | Entity ↔ DTO ↔ VO Converters
    │   ├── domain                            // 实体类（聚合根）| Entity Layer (Domain Models)
    │   ├── dto                               // 数据传输对象 | DTOs for input
    │   ├── enums                             // 枚举类 | Enums for statuses, types, etc.
    │   ├── event                             // Kafka 事件处理 | Kafka Event Producer/Consumer
    │   ├── exception                         // 自定义异常与处理器 | Custom Exceptions and Global Handler
    │   ├── filter                            // 安全过滤器 | JWT and Login Filter
    │   ├── handler                           // 通用业务处理器 | Common Business Handlers (Token, Post, User, etc.)
    │   ├── mapper                            // MyBatis-Plus 映射接口 | DAO Mappers
    │   ├── netty                             // WebSocket 服务 | Netty WebSocket Real-Time Communication
    │   ├── result                            // 通用返回封装 | Standardized Response Format
    │   ├── security                          // Spring Security 用户信息实现 | Spring Security LoginUser
    │   ├── service
    │   │   ├── base                          // 基础服务层（Redis）| Base Service (e.g. Redis)
    │   │   ├── builder                       // 构造器（邮件、VO）| Builder (e.g. Email, VO Assembler)
    │   │   ├── business                      // 业务逻辑服务 | Main Business Services
    │   │   └── data                          // 数据访问服务封装 | Data-Oriented Service Layer
    │   ├── util                              // 工具类（加密、验证码、序列化等）| Utilities (Crypto, Captcha, Serializer, etc.)
    │   ├── validator                         // 参数校验器 | Custom Validators
    │   ├── validation                        // 分组校验注解组 | Bean Validation Groups
    │   └── vo                                // 响应对象 | View Objects for API output
    └── resources
        ├── application.yml                  // 全局配置 | Main Config
        ├── static                           // 静态资源 | Static Files
        └── templates                        // 模板文件 | HTML Templates
```

---

## 🔬 测试结构 | Test Structure

```
src/test/java/com/yugao
├── controller          // Controller 层测试 | REST API Test
├── log                 // 日志系统测试 | Logger Test
├── service             // 服务层测试 | Service Unit Test
├── util                // 工具类测试 | Utility Test
└── GforoApplicationTests.java // 集成测试入口 | Global Boot Test
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





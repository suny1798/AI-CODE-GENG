# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

AI 代码生成平台（云搭），基于 LangChain4j + LangGraph4j 实现智能代码生成工作流。前端 Vue 3，后端 Spring Boot 3.5。

## 常用命令

### 后端 (Java 21)

```bash
# 启动应用
./mvnw spring-boot:run

# 编译
./mvnw compile

# 运行测试
./mvnw test

# 打包
./mvnw package -DskipTests
```

### 前端 (Node.js)

```bash
cd ai-code-geng-frontend

# 安装依赖
npm install

# 开发模式
npm run dev

# 构建生产版本
npm run build

# 类型检查
npm run type-check

# 代码格式化
npm run format
```

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端框架 | Spring Boot 3.5.13, Java 21 |
| AI 集成 | LangChain4j 1.1.0, LangGraph4j 1.6.0-rc2, DeepSeek API |
| 数据库 | MySQL + MyBatis-Flex |
| 缓存/会话 | Redis + Spring Session |
| 对象存储 | 腾讯云 COS |
| 前端 | Vue 3 + TypeScript + Vite 7 + Ant Design Vue 4 + Pinia |
| API 文档 | Knife4j (Swagger) |

## 架构设计

### 后端核心模块

```
src/main/java/com/suny/aicodegeng/
├── ai/                      # AI 服务接口与实现
│   ├── AiCodeGeneratorService.java       # 代码生成服务接口
│   ├── AiCodeGeneratorServiceFactory.java
│   ├── tools/               # AI 工具 (FileRead/Write/Delete等)
│   └── model/message/       # 流式响应消息类型
├── controller/              # REST 控制器
├── service/impl/            # 业务服务实现
├── mapper/                  # MyBatis-Flex Mapper
├── model/
│   ├── entity/              # 数据库实体
│   ├── dto/                 # 数据传输对象
│   ├── vo/                  # 视图对象
│   └── enums/               # 枚举类
├── core/                    # 核心门面与模板
│   ├── AiCodeGeneratorFacade.java  # 代码生成统一入口
│   ├── parser/              # 代码解析器
│   ├── saver/               # 文件保存模板
│   └── handler/             # 流式处理 handler
├── langgraph4j/             # LangGraph4j 工作流
│   ├── CodeGenWorkflow.java        # 工作流定义与执行
│   ├── node/                        # 工作流节点
│   │   ├── ImageCollectorNode.java  # 图片收集
│   │   ├── PromptEnhancerNode.java  # 提示词增强
│   │   ├── RouterNode.java          # 类型路由
│   │   ├── CodeGeneratorNode.java   # 代码生成
│   │   ├── CodeQualityCheckNode.java # 代码质检
│   │   └── ProjectBuilderNode.java  # 项目构建
│   ├── state/WorkflowContext.java    # 工作流状态存储
│   └── tools/                       # 工作流工具
└── config/                   # 配置类
```

### 工作流架构 (LangGraph4j)

`CodeGenWorkflow` 使用 `MessagesStateGraph` 定义工作流：

```
START → ImageCollector → PromptEnhancer → Router
                                          ↓
                              ┌───────────┼───────────┐
                              ↓           ↓           ↓
                         HTML      MULTI_FILE    VUE_PROJECT
                              ↓           ↓           ↓
                         CodeGenerator ←────────────┘
                              ↓
                     CodeQualityCheck
                      ↓     ↓       ↓
                    fail  build   skip_build
                    (循环)   ↓       (结束)
                   CodeGenerator
                              ↓
                      ProjectBuilder → END
```

工作流状态通过 `WorkflowContext` 在 `MessagesState` 中存储，包含：originalPrompt、enhancedPrompt、generationType、imageList、qualityResult 等。

### 前端目录结构

```
ai-code-geng-frontend/src/
├── pages/
│   ├── HomePage.vue
│   ├── app/
│   │   ├── AppChatPage.vue      # 应用聊天页
│   │   └── AppEditPage.vue
│   ├── user/
│   │   ├── UserLoginPage.vue
│   │   └── UserRegisterPage.vue
│   └── admin/
├── components/                  # 公共组件
├── api/                         # API 调用 (由 openapi2ts 生成)
├── stores/                      # Pinia 状态管理
├── layouts/BasicLayout.vue
└── router/index.ts
```

## 代码生成类型

| 类型 | 值 | 说明 |
|------|-----|------|
| HTML | `HTML` | 单页 HTML 代码 |
| MULTI_FILE | `MULTI_FILE` | 多文件项目 |
| VUE_PROJECT | `VUE_PROJECT` | Vue 3 项目(需构建) |

## 关键配置

- 后端端口: `8123`，上下文路径: `/api`
- API 文档: `/api/doc.html` (Knife4j)
- 工作流 prompts: `src/main/resources/prompt/*.txt`
- 代码输出根目录: `AppConstant.CODE_OUTPUT_ROOT_DIR`
- 部署根目录: `AppConstant.CODE_DEPLOY_ROOT_DIR`

## 重要约束

1. 所有 Controller 必须使用 `@RequestMapping("/api/xxx")` 匹配 `server.servlet.context-path`
2. AI 代码生成统一通过 `AiCodeGeneratorFacade` 门面入口
3. 工作流节点必须实现 LangGraph4j 的节点接口，使用 `WorkflowContext` 管理状态
4. 前端 API 调用使用 `src/request.ts` 封装的 Axios 实例

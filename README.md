# 云搭 (AI Code Geng)

一个基于 AI 的智能应用生成平台，用户可以通过自然语言描述快速生成网站应用。

## 项目简介

本项目是一个**前后端分离**的全栈应用，采用 Spring Boot + Vue 3 技术栈构建。用户只需输入简短的应用描述，即可通过 AI 自动生成对应的网站应用，并支持一键部署。

## 技术栈

### 后端
- **框架**: Spring Boot 3.5.13
- **Java 版本**: 21
- **AI 框架**: LangChain4j 1.1.0 + LangGraph4j 1.6.0-rc2
- **数据库**: MySQL + MyBatis-Flex
- **缓存**: Redis (Spring Session + 对话记忆 + 应用列表缓存)
- **对象存储**: 腾讯云 COS
- **API 文档**: Knife4j OpenAPI 3

### 前端
- **框架**: Vue 3.5 + TypeScript
- **构建工具**: Vite 7
- **UI 组件库**: Ant Design Vue 4
- **状态管理**: Pinia
- **路由**: Vue Router 4
- **Markdown 渲染**: markdown-it + highlight.js

## 功能特性

### 1. 用户模块
- 用户注册与登录
- 用户信息管理
- 登录状态持久化 (Redis Session)

### 2. AI 代码生成
- **HTML 单文件生成**: 快速生成单个 HTML 文件的网站代码
- **多文件项目生成**: 生成包含 HTML/CSS/JS 的多文件项目
- **Vue 项目生成**: 生成完整的 Vue 3 + TypeScript 项目（支持构建）
- **流式响应**: 支持 SSE 流式输出，实时显示生成进度
- **LangGraph4j 工作流**: 图片收集 → 提示词增强 → 智能路由 → 代码生成 → 代码质检 → 项目构建

### 3. 应用管理
- 创建新应用（输入初始描述）
- 查看我的应用列表
- 查看精选案例
- 应用详情查看
- 应用排序（置顶权重）

### 4. 对话功能
- 在应用下与 AI 对话
- AI 根据对话内容生成/修改代码
- Markdown 代码渲染
- 对话历史记录（Redis 存储）
- 历史消息分页加载

### 5. 应用部署
- 一键部署生成的应用
- 静态资源访问
- 同步构建等待机制（确保预览就绪）

### 6. 管理员后台
- 用户管理
- 应用管理（审核、排序、置顶）
- 对话管理

### 7. 高级功能
- **可视化编辑模式**: 可视化编辑生成的应用
- **AI 智能路由**: 根据内容自动选择最优代码生成策略
- **图片收集工具**: 并发多源图片收集（Logo、插图、图表）
- **代码质检节点**: 自动检查生成代码质量
- **文件操作工具**: AI 辅助修改和操作项目文件
- **代码下载**: 支持下载完整项目代码
- **Redis 缓存**: 应用列表查询性能优化

## 项目结构

```
ai-code-geng/
├── ai-code-geng-frontend/     # 前端项目
│   ├── src/
│   │   ├── api/              # API 接口定义
│   │   ├── components/       # 公共组件
│   │   │   ├── AppCard.vue
│   │   │   ├── AppDetailModal.vue
│   │   │   ├── DeploySuccessModal.vue
│   │   │   ├── GlobalFooter.vue
│   │   │   ├── GlobalHeader.vue
│   │   │   ├── MarkdownRenderer.vue
│   │   │   └── UserInfo.vue
│   │   ├── config/           # 配置文件
│   │   ├── layouts/          # 布局组件
│   │   ├── pages/            # 页面组件
│   │   │   ├── HomePage.vue           # 首页
│   │   │   ├── AboutPage.vue          # 关于页面
│   │   │   ├── app/
│   │   │   │   ├── AppChatPage.vue    # 应用对话页
│   │   │   │   └── AppEditPage.vue    # 应用编辑页
│   │   │   ├── user/
│   │   │   │   ├── UserLoginPage.vue  # 登录页
│   │   │   │   ├── UserRegisterPage.vue # 注册页
│   │   │   │   └── UserInfoPage.vue    # 用户信息页
│   │   │   └── admin/
│   │   │       ├── AppManagePage.vue  # 应用管理
│   │   │       ├── ChatManagePage.vue # 对话管理
│   │   │       └── UserManagePage.vue # 用户管理
│   │   ├── stores/           # Pinia 状态管理
│   │   ├── utils/            # 工具函数
│   │   ├── request.ts        # Axios 请求封装
│   │   └── main.ts           # 应用入口
│   └── package.json
│
├── src/
│   └── main/
│       ├── java/com/suny/aicodegeng/
│       │   ├── ai/                  # AI 代码生成服务
│       │   │   ├── AiCodeGeneratorService.java
│       │   │   ├── AiCodeGeneratorServiceFactory.java
│       │   │   ├── AiCodeGenTypeRoutingService.java
│       │   │   ├── model/           # AI 返回结果模型
│       │   │   │   └── message/     # 消息模型
│       │   │   └── tools/           # AI 工具类
│       │   │       ├── ToolManager.java
│       │   │       ├── FileWriteTool.java
│       │   │       ├── FileReadTool.java
│       │   │       ├── FileModifyTool.java
│       │   │       ├── FileDeleteTool.java
│       │   │       └── FileDirReadTool.java
│       │   ├── langgraph4j/         # LangGraph4j 工作流
│       │   │   ├── CodeGenWorkflow.java      # 工作流定义
│       │   │   ├── WorkflowApp.java           # 工作流配置
│       │   │   ├── state/                    # 工作流状态
│       │   │   │   └── WorkflowContext.java
│       │   │   ├── node/                     # 工作流节点
│       │   │   │   ├── RouterNode.java            # 类型路由
│       │   │   │   ├── PromptEnhancerNode.java    # 提示词增强
│       │   │   │   ├── ImageCollectorNode.java    # 图片收集
│       │   │   │   ├── CodeGeneratorNode.java     # 代码生成
│       │   │   │   ├── CodeQualityCheckNode.java  # 代码质检
│       │   │   │   └── ProjectBuilderNode.java   # 项目构建
│       │   │   └── tools/                # 工作流工具
│       │   │       ├── ImageSearchTool.java
│       │   │       ├── LogoGeneratorTool.java
│       │   │       ├── MermaidDiagramTool.java
│       │   │       └── UndrawIllustrationTool.java
│       │   ├── config/              # 配置类
│       │   ├── constant/            # 常量定义
│       │   ├── controller/          # 控制器
│       │   │   ├── AppController.java
│       │   │   ├── ChatHistoryController.java
│       │   │   └── UserController.java
│       │   ├── core/                # 核心代码
│       │   │   ├── AiCodeGeneratorFacade.java  # 代码生成统一入口
│       │   │   ├── CodeFileSaver.java          # 文件存储器
│       │   │   ├── builder/         # 项目构建器
│       │   │   │   └── VueProjectBuilder.java
│       │   │   ├── parser/          # 代码解析器
│       │   │   ├── saver/           # 文件存储器模板
│       │   │   └── handler/         # 流处理器
│       │   ├── mapper/              # MyBatis Mapper
│       │   ├── model/               # 数据模型
│       │   │   ├── dto/             # 数据传输对象
│       │   │   ├── entity/          # 实体类
│       │   │   ├── enums/           # 枚举类
│       │   │   └── vo/              # 视图对象
│       │   ├── service/             # 服务层
│       │   │   ├── AppService.java
│       │   │   ├── ChatHistoryService.java
│       │   │   └── ProjectDownloadService.java
│       │   ├── manage/              # 对象存储管理
│       │   │   └── CosManager.java
│       │   └── exception/           # 异常处理
│       └── resources/
│           ├── mapper/              # Mapper XML 文件
│           ├── prompt/              # AI 提示词模板
│           └── application.yml      # 应用配置
│
├── pom.xml
└── mvnw                         # Maven 包装器
```

## 核心流程

### AI 代码生成工作流 (LangGraph4j)

```
START
  ↓
ImageCollectorNode (收集相关图片素材)
  ↓
PromptEnhancerNode (增强提示词)
  ↓
RouterNode (智能路由: HTML / MULTI_FILE / VUE_PROJECT)
  ↓
CodeGeneratorNode (生成代码)
  ↓
CodeQualityCheckNode (代码质检)
  ↓
  ├─→ 质检失败 → CodeGeneratorNode (重新生成)
  ↓
ProjectBuilderNode (Vue项目构建)
  ↓
  → END
```

### 代码生成类型

| 类型 | 值 | 说明 | 输出 |
|------|-----|------|------|
| HTML | `HTML` | 单文件网站 | 单个 HTML 文件，包含内联 CSS/JS |
| Multi-File | `MULTI_FILE` | 多文件项目 | HTML + CSS + JS 分离文件 |
| Vue Project | `VUE_PROJECT` | Vue 项目 | 完整的 Vue 3 + TypeScript 项目 |

## 配置文件

### 后端 application-local.yml

需要配置以下内容：
- 数据库连接 (MySQL)
- Redis 连接
- DeepSeek API Key (AI 模型)
- 腾讯云 COS (对象存储)

### 前端 .env.development

需要配置 API 代理地址

## 运行项目

### 前置条件
- JDK 21+
- Node.js 18+
- MySQL 8+
- Redis 6+

### 后端启动

```bash
# 1. 配置数据库和 Redis (修改 application-local.yml)
# 2. 启动后端
./mvnw spring-boot:run
```

### 前端启动

```bash
cd ai-code-geng-frontend
npm install
npm run dev
```

## 最近提交

- `4a4b63d` - 前端：优化AppCard权限控制
- `55270e9` - 后端：优化Vue项目构建为同步执行，确保项目预览就绪
- `d88755d` - 后端：新增Redis缓存支持，使用Cacheable注解优化应用列表查询性能
- `a3c43a8` - 后端：重构AI模型为多例模式，解决并发问题
- `70c70b2` - 前、后端：优化部署按钮、流式更新性能与应用管理排序
- `46f51a9` - 后端：新增图片收集服务，支持并发收集与增强提示词
- `2e54041` - 后端：新增SSE流式工作流与LangGraph Studio可视化配置

## License

Private Project - All Rights Reserved
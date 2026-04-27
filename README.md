# AI 应用生成平台 (AI Code Geng)

一个基于 AI 的智能应用生成平台，用户可以通过自然语言描述快速生成网站应用。

## 项目简介

本项目是一个**前后端分离**的全栈应用，采用 Spring Boot + Vue 3 技术栈构建。用户只需输入简短的应用描述，即可通过 AI 自动生成对应的网站应用，并支持一键部署。

## 技术栈

### 后端
- **框架**: Spring Boot 3.5.13
- **Java 版本**: 21
- **AI 框架**: LangChain4j (集成 OpenAI)
- **数据库**: MySQL + MyBatis-Flex
- **缓存**: Redis (Spring Session + 对话记忆)
- **API 文档**: Knife4j OpenAPI 3

### 前端
- **框架**: Vue 3.5 + TypeScript
- **构建工具**: Vite 7
- **UI 组件库**: Ant Design Vue 4
- **状态管理**: Pinia
- **路由**: Vue Router 4
- **Markdown 渲染**: markdown-it + highlight.js

## 功能特性

### 已实现功能

#### 1. 用户模块
- 用户注册与登录
- 用户信息管理
- 登录状态持久化 (Redis Session)

#### 2. AI 代码生成
- **HTML 单文件生成**: 快速生成单个 HTML 文件的网站代码
- **多文件项目生成**: 生成包含 HTML/CSS/JS 的多文件项目
- **Vue 项目生成**: 生成完整的 Vue 3 + TypeScript 项目
- **流式响应**: 支持 SseEmitter 流式输出，实时显示生成进度

#### 3. 应用管理
- 创建新应用（输入初始描述）
- 查看我的应用列表
- 查看精选案例
- 应用详情查看

#### 4. 对话功能
- 在应用下与 AI 对话
- AI 根据对话内容生成/修改代码
- Markdown 代码渲染
- 对话历史记录（Redis 存储）
- 历史消息分页加载

#### 5. 应用部署
- 一键部署生成的应用
- 静态资源访问

#### 6. 管理员后台
- 用户管理
- 应用管理
- 对话管理

#### 7. 高级功能
- **可视化编辑模式**: 可视化编辑生成的应用
- **AI 智能路由**: 根据内容自动选择最优代码生成策略
- **应用截图**: 自动生成应用缩略图
- **代码下载**: 支持下载完整项目代码
- **图片收集工具**: LangGraph4j 工作流自动收集相关图片素材
- **文件操作工具**: AI 辅助修改和操作项目文件

### 开发中功能

- [ ] 移动端适配

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
│       │   │   │   ├── HtmlCodeResult.java
│       │   │   │   ├── MultiFileCodeResult.java
│       │   │   │   └── message/     # 消息模型
│       │   │   └── tools/           # AI 工具类
│       │   │       ├── ToolManager.java
│       │   │       ├── FileWriteTool.java
│       │   │       ├── FileReadTool.java
│       │   │       ├── FileModifyTool.java
│       │   │       ├── FileDeleteTool.java
│       │   │       └── FileDirReadTool.java
│       │   ├── langgraph4j/         # LangGraph4j 工作流
│       │   │   ├── CodeGenWorkflow.java
│       │   │   ├── WorkflowApp.java
│       │   │   ├── node/            # 工作流节点
│       │   │   │   ├── RouterNode.java
│       │   │   │   ├── PromptEnhancerNode.java
│       │   │   │   ├── ImageCollectorNode.java
│       │   │   │   ├── CodeGeneratorNode.java
│       │   │   │   └── ProjectBuilderNode.java
│       │   │   ├── tools/           # 图片收集工具
│       │   │   │   ├── ImageSearchTool.java
│       │   │   │   ├── LogoGeneratorTool.java
│       │   │   │   ├── MermaidDiagramTool.java
│       │   │   │   └── UndrawIllustrationTool.java
│       │   │   └── ai/              # 图片服务
│       │   ├── config/              # 配置类
│       │   │   ├── CorsConfig.java
│       │   │   ├── JsonConfig.java
│       │   │   ├── RedisChatMemoryStoreConfig.java
│       │   │   ├── ReasoningStreamingChatModelConfig.java
│       │   │   └── CosClientConfig.java
│       │   ├── constant/            # 常量定义
│       │   ├── controller/          # 控制器
│       │   │   ├── AppController.java
│       │   │   ├── ChatHistoryController.java
│       │   │   ├── HealthController.java
│       │   │   ├── StaticResourceController.java
│       │   │   └── UserController.java
│       │   ├── core/                # 核心代码
│       │   │   ├── AiCodeGeneratorFacade.java
│       │   │   ├── CodeFileSaver.java
│       │   │   ├── CodeParser.java
│       │   │   ├── builder/         # 项目构建器
│       │   │   │   └── VueProjectBuilder.java
│       │   │   ├── parser/          # 代码解析器
│       │   │   │   ├── CodeParser.java
│       │   │   │   ├── CodeParserExecutor.java
│       │   │   │   ├── HtmlCodeParser.java
│       │   │   │   └── MultiFileCodeParser.java
│       │   │   ├── saver/           # 文件存储器
│       │   │   │   ├── CodeFileSaverExecutor.java
│       │   │   │   ├── CodeFileSaverTemplate.java
│       │   │   │   ├── HtmlCodeFileSaverTemplate.java
│       │   │   │   └── MultiFileCodeFileSaverTemplate.java
│       │   │   └── handler/         # 流处理器
│       │   │       ├── StreamHandlerExecutor.java
│       │   │       ├── SimpleTextStreamHandler.java
│       │   │       └── JsonMessageStreamHandler.java
│       │   ├── mapper/              # MyBatis Mapper
│       │   ├── model/               # 数据模型
│       │   │   ├── dto/             # 数据传输对象
│       │   │   ├── entity/          # 实体类
│       │   │   ├── enums/           # 枚举类
│       │   │   └── vo/              # 视图对象
│       │   ├── service/             # 服务层
│       │   │   ├── AppService.java
│       │   │   ├── ChatHistoryService.java
│       │   │   ├── ScreenshotService.java
│       │   │   └── ProjectDownloadService.java
│       │   ├── manage/              # 对象存储管理
│       │   │   └── CosManager.java
│       │   ├── utils/               # 工具类
│       │   │   └── WebScreenshotUtils.java
│       │   └── exception/           # 异常处理
│       └── resources/
│           ├── mapper/              # Mapper XML 文件
│           ├── prompt/              # AI 提示词模板
│           │   ├── codegen-html-system-prompt.txt
│           │   ├── codegen-multi-file-system-prompt.txt
│           │   └── codegen-vue-project-system-prompt.txt
│           └── application.yml      # 应用配置
│
├── pom.xml
└── mvnw                         # Maven 包装器
```

## 核心流程

### AI 代码生成流程

```
用户输入描述
    ↓
AiCodeGeneratorFacade 协调器
    ↓
选择代码生成类型 (HTML/多文件/Vue)
    ↓
调用 LangChain4j AI 服务
    ↓
CodeParser 解析 AI 返回的代码
    ↓
CodeFileSaver 保存代码到文件
    ↓
返回生成结果给前端
```

### 代码生成类型

| 类型 | 说明 | 输出 |
|------|------|------|
| HTML | 单文件网站 | 单个 HTML 文件，包含内联 CSS/JS |
| Multi-File | 多文件项目 | HTML + CSS + JS 分离文件 |
| Vue Project | Vue 项目 | 完整的 Vue 3 + TypeScript 项目结构 |

## 待完成工作

### 功能增强
- [ ] 生成代码的在线预览
- [ ] 代码版本管理
- [ ] 模板市场
- [ ] 团队协作功能
- [ ] 移动端适配

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

## 配置文件

### 后端 application-local.yml

需要配置以下内容：
- 数据库连接 (MySQL)
- Redis 连接
- LangChain4j / OpenAI API Key

### 前端 .env.development

需要配置 API 代理地址

## 最近提交

- `61e76ec` - 后端：新增LangGraph4j工作流与图片收集工具
- `41a9b09` - 前端：优化管理页面UI，新增菜单图标
- `cd6e7db` - 后端：重构工具系统，新增文件操作工具与工具管理器
- `27291ff` - 后端：优化提示词，增加修改要求处理规范
- `ede8d13` - 前端：配置Vite代理，新增可视化编辑模式与元素选择功能

## 贡献指南

项目正在积极开发中，欢迎提出 Issue 和 Pull Request。

## License

Private Project - All Rights Reserved

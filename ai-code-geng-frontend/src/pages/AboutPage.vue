<template>
  <div id="aboutPage">
    <div class="bg-effects">
      <div class="grid-bg"></div>
      <div class="glow-effect"></div>
    </div>
    <div class="container">
      <div class="hero-section">
        <h1 class="title">关于云搭平台</h1>
        <p class="subtitle">说出来的创意，跑起来的应用</p>
      </div>

      <div class="content-card">
        <a-row :gutter="[32, 32]">
          <a-col
            :xs="24"
            :sm="24"
            :md="12"
            :lg="8"
            v-for="feature in features"
            :key="feature.title"
          >
            <div class="feature-item">
              <div class="feature-icon" :style="{ background: feature.color }">
                <component :is="feature.icon" />
              </div>
              <h3 class="feature-title">{{ feature.title }}</h3>
              <p class="feature-desc">{{ feature.desc }}</p>
            </div>
          </a-col>
        </a-row>
      </div>

      <div class="tech-section">
        <h2 class="section-title">技术栈</h2>
        <div class="tech-tags">
          <a-tag v-for="tech in techStack" :key="tech" color="blue">{{ tech }}</a-tag>
        </div>
      </div>

      <div class="contact-section">
        <h2 class="section-title">联系我们</h2>
        <div class="contact-links">
          <a-button type="primary" size="large" @click="openGitHub">
            <GithubOutlined /> GitHub
          </a-button>
          <a-button size="large" @click="openBlog"> <QqOutlined /> 个人博客 </a-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { h, onMounted, onUnmounted } from 'vue'
import {
  GithubOutlined,
  CodeOutlined,
  RocketOutlined,
  TeamOutlined,
  BulbOutlined,
  SafetyOutlined,
  QqOutlined,
} from '@ant-design/icons-vue'

const features = [
  {
    icon: h(BulbOutlined),
    title: '智能生成',
    desc: '基于 AI 大模型，只需输入需求描述，即可自动生成完整的应用代码',
    color: 'linear-gradient(135deg, #3b82f6, #6366f1)',
  },
  {
    icon: h(CodeOutlined),
    title: '代码可视化',
    desc: '实时预览生成效果，所见即所得，快速迭代应用功能',
    color: 'linear-gradient(135deg, #10b981, #059669)',
  },
  {
    icon: h(RocketOutlined),
    title: '一键部署',
    desc: '支持一键部署应用，快速上线你的作品到云端',
    color: 'linear-gradient(135deg, #8b5cf6, #7c3aed)',
  },
  {
    icon: h(TeamOutlined),
    title: '精选案例',
    desc: '汇集优质应用案例，参考学习快速上手',
    color: 'linear-gradient(135deg, #f59e0b, #d97706)',
  },
  {
    icon: h(SafetyOutlined),
    title: '安全可靠',
    desc: '完善的用户系统和权限管理，保障你的数据安全',
    color: 'linear-gradient(135deg, #ef4444, #dc2626)',
  },
  {
    icon: h(TeamOutlined),
    title: '开源共享',
    desc: '代码开源，欢迎社区贡献和改进',
    color: 'linear-gradient(135deg, #06b6d4, #0891b2)',
  },
]

const techStack = [
  'Vue 3',
  'TypeScript',
  'Vite',
  'Ant Design Vue',
  'Spring Boot',
  'MyBatis-Plus',
  'MySQL',
  'AI大模型',
  'WebSocket',
  'Docker',
]

// 鼠标跟随光效
const handleMouseMove = (e: MouseEvent) => {
  const { clientX, clientY } = e
  const { innerWidth, innerHeight } = window
  const x = (clientX / innerWidth) * 100
  const y = (clientY / innerHeight) * 100
  document.documentElement.style.setProperty('--mouse-x', `${x}%`)
  document.documentElement.style.setProperty('--mouse-y', `${y}%`)
}

onMounted(() => {
  document.addEventListener('mousemove', handleMouseMove)
})

onUnmounted(() => {
  document.removeEventListener('mousemove', handleMouseMove)
})

const openGitHub = () => {
  window.open('https://github.com/suny1798', '_blank')
}

const openBlog = () => {
  window.open('https://github.com/suny1798', '_blank')
}
</script>

<style scoped>
#aboutPage {
  min-height: 100vh;
  padding: 40px 24px 80px;
  background:
    linear-gradient(180deg, #f8fafc 0%, #f1f5f9 8%, #e2e8f0 20%, #cbd5e1 100%),
    radial-gradient(circle at 20% 80%, rgba(59, 130, 246, 0.15) 0%, transparent 50%),
    radial-gradient(circle at 80% 20%, rgba(139, 92, 246, 0.12) 0%, transparent 50%),
    radial-gradient(circle at 40% 40%, rgba(16, 185, 129, 0.08) 0%, transparent 50%);
  position: relative;
  overflow: hidden;
}

/* 网格背景 */
.bg-effects .grid-bg {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image:
    linear-gradient(rgba(59, 130, 246, 0.05) 1px, transparent 1px),
    linear-gradient(90deg, rgba(59, 130, 246, 0.05) 1px, transparent 1px),
    linear-gradient(rgba(139, 92, 246, 0.04) 1px, transparent 1px),
    linear-gradient(90deg, rgba(139, 92, 246, 0.04) 1px, transparent 1px);
  background-size:
    100px 100px,
    100px 100px,
    20px 20px,
    20px 20px;
  pointer-events: none;
  animation: gridFloat 20s ease-in-out infinite;
}

/* 动态光效 */
.bg-effects .glow-effect {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(
    600px circle at var(--mouse-x, 50%) var(--mouse-y, 50%),
    rgba(59, 130, 246, 0.1) 0%,
    rgba(139, 92, 246, 0.06) 40%,
    transparent 70%
  );
  pointer-events: none;
  animation: lightPulse 8s ease-in-out infinite alternate;
}

@keyframes gridFloat {
  0%,
  100% {
    transform: translate(0, 0);
  }
  50% {
    transform: translate(5px, 5px);
  }
}

@keyframes lightPulse {
  0% {
    opacity: 0.5;
  }
  100% {
    opacity: 1;
  }
}

.container {
  max-width: 1000px;
  margin: 0 auto;
  position: relative;
  z-index: 10;
}

/* 英雄区域 */
.hero-section {
  text-align: center;
  padding: 60px 0 50px;
}

.title {
  font-size: 42px;
  font-weight: 700;
  margin: 0 0 16px;
  background: linear-gradient(135deg, #3b82f6 0%, #8b5cf6 50%, #10b981 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  letter-spacing: -0.5px;
  animation: titleShimmer 3s ease-in-out infinite;
}

@keyframes titleShimmer {
  0%,
  100% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
}

.subtitle {
  font-size: 18px;
  color: #64748b;
  margin: 0;
}

/* 内容卡片 */
.content-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  padding: 48px 40px;
  box-shadow:
    0 25px 50px rgba(0, 0, 0, 0.12),
    0 0 0 1px rgba(255, 255, 255, 0.8) inset;
  margin-bottom: 40px;
  animation: cardAppear 0.6s ease-out;
}

@keyframes cardAppear {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 功能特性 */
.feature-item {
  text-align: center;
  padding: 24px 16px;
  border-radius: 16px;
  transition: all 0.3s;
  height: 100%;
}

.feature-item:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.12);
}

.feature-icon {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  font-size: 28px;
  color: #fff;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.feature-title {
  font-size: 18px;
  font-weight: 600;
  color: #1e293b;
  margin: 0 0 12px;
}

.feature-desc {
  font-size: 14px;
  color: #64748b;
  margin: 0;
  line-height: 1.6;
}

/* 技术栈 */
.tech-section,
.contact-section {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  padding: 40px;
  box-shadow:
    0 20px 40px rgba(0, 0, 0, 0.1),
    0 0 0 1px rgba(255, 255, 255, 0.8) inset;
  margin-bottom: 40px;
  animation: cardAppear 0.6s ease-out 0.2s both;
}

.section-title {
  font-size: 24px;
  font-weight: 600;
  color: #1e293b;
  margin: 0 0 24px;
  text-align: center;
}

.tech-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  justify-content: center;
}

:deep(.ant-tag) {
  padding: 6px 16px;
  font-size: 14px;
  border-radius: 20px;
  border: none;
  background: linear-gradient(135deg, #3b82f6, #6366f1);
  color: #fff;
}

/* 联系方式 */
.contact-links {
  display: flex;
  gap: 16px;
  justify-content: center;
}

:deep(.ant-btn-primary) {
  height: 48px;
  padding: 0 32px;
  font-size: 16px;
  border-radius: 12px;
  background: linear-gradient(135deg, #3b82f6 0%, #6366f1 100%);
  border: none;
  box-shadow: 0 4px 14px rgba(59, 130, 246, 0.4);
  transition: all 0.3s;
}

:deep(.ant-btn-primary:hover) {
  background: linear-gradient(135deg, #2563eb 0%, #4f46e5 100%);
  box-shadow: 0 6px 20px rgba(59, 130, 246, 0.5);
  transform: translateY(-2px);
}

:deep(.ant-btn-default) {
  height: 48px;
  padding: 0 32px;
  font-size: 16px;
  border-radius: 12px;
  border: 2px solid #e2e8f0;
  transition: all 0.3s;
}

:deep(.ant-btn-default:hover) {
  border-color: #3b82f6;
  color: #3b82f6;
  transform: translateY(-2px);
}

/* 响应式 */
@media (max-width: 768px) {
  .title {
    font-size: 28px;
  }

  .subtitle {
    font-size: 16px;
  }

  .content-card {
    padding: 32px 24px;
  }

  .tech-section,
  .contact-section {
    padding: 32px 24px;
  }

  .contact-links {
    flex-direction: column;
  }

  :deep(.ant-btn-primary),
  :deep(.ant-btn-default) {
    width: 100%;
  }
}
</style>

<template>
  <div id="userInfoPage">
    <div class="bg-effects">
      <div class="grid-bg"></div>
      <div class="glow-effect"></div>
    </div>
    <div class="container">
      <!-- 左侧：用户信息卡片 -->
      <div class="left">
        <div class="user-card">
          <div class="avatar-section">
            <a-avatar :src="userData.userAvatar" class="avatar" :size="100">
              <template #icon><UserOutlined /></template>
            </a-avatar>
            <div class="name">{{ userData.userName }}</div>
            <div class="account">
              {{ userData.userAccount }}
              <a-tag v-if="userData.userRole == 'admin'" color="red">管理员</a-tag>
              <a-tag v-else color="blue">普通用户</a-tag>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧：编辑表单 -->
      <div class="right">
        <a-card class="form-card" title="个人信息">
          <a-form :model="userData" layout="vertical" autocomplete="off" @finish="onFinish">
            <a-row :gutter="16">
              <a-col :span="12">
                <a-form-item
                  label="昵称"
                  name="userName"
                  :rules="[
                    { required: true, message: '请输入昵称!' },
                    { max: 10, message: '昵称长度不能超过10位' },
                  ]"
                >
                  <a-input v-model:value="userData.userName" placeholder="请输入昵称" size="large" />
                </a-form-item>
              </a-col>

              <a-col :span="12">
                <a-form-item label="账户">
                  <a-input disabled v-model:value="userData.userAccount" size="large" />
                </a-form-item>
              </a-col>
            </a-row>

            <a-row :gutter="16">
              <a-col :span="12">
                <a-form-item
                  label="原密码"
                  name="userOldPassword"
                  :rules="[{ min: 8, message: '密码长度不能少于8位' }]"
                >
                  <a-input-password
                    placeholder="为空则表示不更改密码"
                    v-model:value="userData.userOldPassword"
                    size="large"
                  />
                </a-form-item>
              </a-col>

              <a-col :span="12">
                <a-form-item label="新密码" :rules="[{ min: 8, message: '密码长度不能少于8位' }]">
                  <a-input-password v-model:value="userData.userPassword" placeholder="请输入新密码" size="large" />
                </a-form-item>
              </a-col>
            </a-row>

            <a-form-item label="简介">
              <a-textarea v-model:value="userData.userProfile" rows="4" placeholder="介绍一下自己吧~" :maxlength="100" show-count />
            </a-form-item>

            <div class="footer">
              <a-button type="primary" html-type="submit" :loading="loading" size="large">保存</a-button>
            </div>
          </a-form>
        </a-card>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { onMounted, reactive, ref, onUnmounted } from 'vue'
import { message } from 'ant-design-vue'
import { UserOutlined } from '@ant-design/icons-vue'
import router from '@/router'
import { useLoginUserStore } from '@/stores/loginUser.ts'
import { userEditInfo, userLogout } from '@/api/userController.ts'

const loading = ref(false)
const userData = reactive<API.UserEditRequest>({
  id: 0,
  userName: '',
  userAccount: '',
  userPassword: '',
  userOldPassword: '',
  userProfile: '',
  userAvatar: '',
  userRole: '',
})
const loginUserStore = useLoginUserStore()

// 鼠标跟随光效
const handleMouseMove = (e: MouseEvent) => {
  const { clientX, clientY } = e
  const { innerWidth, innerHeight } = window
  const x = (clientX / innerWidth) * 100
  const y = (clientY / innerHeight) * 100
  document.documentElement.style.setProperty('--mouse-x', `${x}%`)
  document.documentElement.style.setProperty('--mouse-y', `${y}%`)
}

onMounted(async () => {
  await loginUserStore.fetchLoginUser()
  userData.id = loginUserStore.loginUser.id
  userData.userName = loginUserStore.loginUser.userName
  userData.userAccount = loginUserStore.loginUser.userAccount
  userData.userProfile = loginUserStore.loginUser.userProfile
  userData.userAvatar = loginUserStore.loginUser.userAvatar
  userData.userRole = loginUserStore.loginUser.userRole
  document.addEventListener('mousemove', handleMouseMove)
})

onUnmounted(() => {
  document.removeEventListener('mousemove', handleMouseMove)
})

const onFinish = async (values: any) => {
  loading.value = true
  const res = await userEditInfo(userData)
  if (res.data.code === 0) {
    await loginUserStore.fetchLoginUser()
    if (!userData.userPassword) {
      message.success('修改成功!')
    } else {
      message.success('修改成功! 请重新登录')
      await userLogout(values)
      await router.push({
        path: '/user/login',
        replace: true,
      })
    }
  } else {
    message.error('修改失败，' + res.data.message)
  }
  loading.value = false
}
</script>

<style scoped>
#userInfoPage {
  min-height: 100vh;
  padding: 40px 24px;
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
  background:
    radial-gradient(
      600px circle at var(--mouse-x, 50%) var(--mouse-y, 50%),
      rgba(59, 130, 246, 0.1) 0%,
      rgba(139, 92, 246, 0.06) 40%,
      transparent 70%
    );
  pointer-events: none;
  animation: lightPulse 8s ease-in-out infinite alternate;
}

@keyframes gridFloat {
  0%, 100% { transform: translate(0, 0); }
  50% { transform: translate(5px, 5px); }
}

@keyframes lightPulse {
  0% { opacity: 0.5; }
  100% { opacity: 1; }
}

/* 整体布局 */
.container {
  display: flex;
  gap: 24px;
  max-width: 1000px;
  margin: 0 auto;
  position: relative;
  z-index: 10;
}

/* 左侧用户卡片 */
.left {
  width: 280px;
  flex-shrink: 0;
}

.user-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 32px 24px;
  box-shadow:
    0 20px 40px rgba(0, 0, 0, 0.12),
    0 0 0 1px rgba(255, 255, 255, 0.8) inset;
  animation: cardAppear 0.5s ease-out;
}

@keyframes cardAppear {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.avatar {
  margin-bottom: 16px;
  border: 4px solid #e2e8f0;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.name {
  font-size: 22px;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 8px;
}

.account {
  font-size: 13px;
  color: #64748b;
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 右侧表单卡片 */
.right {
  flex: 1;
  animation: cardAppear 0.5s ease-out 0.1s both;
}

.form-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow:
    0 20px 40px rgba(0, 0, 0, 0.12),
    0 0 0 1px rgba(255, 255, 255, 0.8) inset;
}

:deep(.ant-card-head) {
  border-bottom: 1px solid #f1f5f9;
  padding: 20px 24px;
}

:deep(.ant-card-head-title) {
  font-size: 18px;
  font-weight: 600;
  color: #1e293b;
}

:deep(.ant-card-body) {
  padding: 24px;
}

/* 表单样式 */
:deep(.ant-form-item-label > label) {
  font-weight: 500;
  color: #475569;
}

:deep(.ant-input),
:deep(.ant-input-affix-wrapper),
:deep(.ant-input-textarea textarea) {
  border-radius: 10px;
  padding: 10px 14px;
  font-size: 14px;
  border: 1px solid #e2e8f0;
  background: #f8fafc;
  transition: all 0.3s;
}

:deep(.ant-input:hover),
:deep(.ant-input-affix-wrapper:hover) {
  border-color: #3b82f6;
  background: #fff;
}

:deep(.ant-input:focus),
:deep(.ant-input-affix-wrapper-focused) {
  border-color: #3b82f6;
  background: #fff;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.15);
}

:deep(.ant-input[disabled]) {
  background: #f1f5f9;
  color: #94a3b8;
  cursor: not-allowed;
}

:deep(.ant-input-password-icon) {
  color: #94a3b8;
  transition: color 0.3s;
}

:deep(.ant-input-password-icon:hover) {
  color: #3b82f6;
}

:deep(.ant-input-textarea textarea) {
  resize: none;
}

/* 按钮样式 */
:deep(.ant-btn-primary) {
  height: 42px;
  font-size: 15px;
  font-weight: 600;
  border-radius: 10px;
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

:deep(.ant-btn-primary:active) {
  transform: translateY(0);
}

/* 标签样式 */
:deep(.ant-tag) {
  border-radius: 6px;
  font-size: 12px;
  padding: 2px 8px;
}

/* 按钮区域 */
.footer {
  text-align: right;
  margin-top: 24px;
}

/* 响应式 */
@media (max-width: 768px) {
  .container {
    flex-direction: column;
  }

  .left {
    width: 100%;
  }

  .user-card {
    padding: 24px;
  }

  .avatar {
    width: 80px !important;
    height: 80px !important;
  }

  .name {
    font-size: 18px;
  }
}
</style>

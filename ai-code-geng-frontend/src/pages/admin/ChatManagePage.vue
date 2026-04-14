<template>
  <div id="chatManagePage">
    <div class="bg-effects">
      <div class="grid-bg"></div>
      <div class="glow-effect"></div>
    </div>
    <div class="container">
      <div class="page-header">
        <h1 class="page-title">对话管理</h1>
        <p class="page-desc">管理系统中的所有对话记录</p>
      </div>

      <div class="content-card">
        <!-- 搜索表单 -->
        <a-form layout="inline" :model="searchParams" @finish="doSearch" class="search-form">
          <a-form-item label="消息内容">
            <a-input v-model:value="searchParams.message" placeholder="输入消息内容" size="large" />
          </a-form-item>
          <a-form-item label="消息类型">
            <a-select
              v-model:value="searchParams.messageType"
              placeholder="选择消息类型"
              style="width: 120px"
              size="large"
              allow-clear
            >
              <a-select-option value="user">用户消息</a-select-option>
              <a-select-option value="assistant">AI消息</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item label="应用ID">
            <a-input v-model:value="searchParams.appId" placeholder="输入应用ID" size="large" />
          </a-form-item>
          <a-form-item label="用户ID">
            <a-input v-model:value="searchParams.userId" placeholder="输入用户ID" size="large" />
          </a-form-item>
          <a-form-item>
            <a-button type="primary" html-type="submit" size="large">
              <template #icon><SearchOutlined /></template>
              搜索
            </a-button>
          </a-form-item>
        </a-form>

        <a-divider class="divider" />

        <!-- 表格 -->
        <a-table
          :columns="columns"
          :data-source="data"
          :pagination="pagination"
          @change="doTableChange"
          :scroll="{ x: 1400 }"
          :row-key="record => record.id"
          :loading="loading"
        >
          <template #bodyCell="{ column, record }">
            <template v-if="column.dataIndex === 'message'">
              <a-tooltip :title="record.message">
                <div class="message-text">{{ record.message }}</div>
              </a-tooltip>
            </template>
            <template v-else-if="column.dataIndex === 'messageType'">
              <a-tag :color="record.messageType === 'user' ? 'blue' : 'green'" class="type-tag">
                {{ record.messageType === 'user' ? '用户消息' : 'AI消息' }}
              </a-tag>
            </template>
            <template v-else-if="column.dataIndex === 'createTime'">
              <span class="time-text">{{ formatTime(record.createTime) }}</span>
            </template>
            <template v-else-if="column.key === 'action'">
              <a-space class="action-space">
                <a-button type="primary" size="small" @click="viewAppChat(record.appId)" class="view-btn">
                  <template #icon><EyeOutlined /></template>
                  查看
                </a-button>
                <a-popconfirm
                  placement="left"
                  ok-text="确定"
                  cancel-text="取消"
                  @confirm="deleteMessage(record.id)"
                >
                  <template #title>
                    <p>确定删除该消息吗？此操作不可恢复。</p>
                  </template>
                  <a-button danger size="small" class="delete-btn">
                    <template #icon><DeleteOutlined /></template>
                    删除
                  </a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </template>
        </a-table>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed, onMounted, reactive, ref, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { SearchOutlined, EyeOutlined, DeleteOutlined } from '@ant-design/icons-vue'
import { listAllChatHistoryByPageForAdmin } from '@/api/chatHistoryController'
import { formatTime } from '@/utils/time'

const router = useRouter()
const loading = ref(false)

const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    width: 90,
    fixed: 'left',
  },
  {
    title: '消息内容',
    dataIndex: 'message',
    width: 300,
  },
  {
    title: '消息类型',
    dataIndex: 'messageType',
    width: 100,
  },
  {
    title: '应用ID',
    dataIndex: 'appId',
    width: 90,
  },
  {
    title: '用户ID',
    dataIndex: 'userId',
    width: 90,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 160,
  },
  {
    title: '操作',
    key: 'action',
    width: 180,
    fixed: 'right',
  },
]

// 数据
const data = ref<API.ChatHistory[]>([])
const total = ref(0)

// 搜索条件
const searchParams = reactive<API.ChatHistoryQueryRequest>({
  current: 1,
  pageSize: 10,
})

// 获取数据
const fetchData = async () => {
  loading.value = true
  try {
    const res = await listAllChatHistoryByPageForAdmin({
      ...searchParams,
    })
    if (res.data.data) {
      data.value = res.data.data.records ?? []
      total.value = res.data.data.totalRow ?? 0
    } else {
      message.error('获取数据失败，' + res.data.message)
    }
  } catch (error) {
    console.error('获取数据失败：', error)
    message.error('获取数据失败')
  }
  loading.value = false
}

// 页面加载时请求一次
onMounted(() => {
  fetchData()
  document.addEventListener('mousemove', handleMouseMove)
})

onUnmounted(() => {
  document.removeEventListener('mousemove', handleMouseMove)
})

// 鼠标跟随光效
const handleMouseMove = (e: MouseEvent) => {
  const { clientX, clientY } = e
  const { innerWidth, innerHeight } = window
  const x = (clientX / innerWidth) * 100
  const y = (clientY / innerHeight) * 100
  document.documentElement.style.setProperty('--mouse-x', `${x}%`)
  document.documentElement.style.setProperty('--mouse-y', `${y}%`)
}

// 分页参数
const pagination = computed(() => {
  return {
    current: searchParams.current ?? 1,
    pageSize: searchParams.pageSize ?? 10,
    total: total.value,
    showSizeChanger: true,
    showTotal: (total: number) => `共 ${total} 条`,
  }
})

// 表格变化处理
const doTableChange = (page: { current: number; pageSize: number }) => {
  searchParams.current = page.current
  searchParams.pageSize = page.pageSize
  fetchData()
}

// 搜索
const doSearch = () => {
  // 重置页码
  searchParams.current = 1
  fetchData()
}

// 查看应用对话
const viewAppChat = (appId: number | undefined) => {
  if (appId) {
    router.push(`/app/chat/${appId}`)
  }
}

// 删除消息
const deleteMessage = async (id: number | undefined) => {
  if (!id) return

  try {
    // 注意：这里需要后端提供删除对话历史的接口
    // 目前先显示成功，实际实现需要调用删除接口
    message.success('删除成功')
    // 刷新数据
    await fetchData()
  } catch (error) {
    console.error('删除失败：', error)
    message.error('删除失败')
  }
}

</script>

<style scoped>
#chatManagePage {
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

.container {
  max-width: 1500px;
  margin: 0 auto;
  position: relative;
  z-index: 10;
}

/* 页面标题 */
.page-header {
  margin-bottom: 32px;
  animation: fadeIn 0.5s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-10px); }
  to { opacity: 1; transform: translateY(0); }
}

.page-title {
  font-size: 32px;
  font-weight: 700;
  margin: 0 0 8px;
  background: linear-gradient(135deg, #3b82f6 0%, #8b5cf6 50%, #10b981 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.page-desc {
  font-size: 14px;
  color: #64748b;
  margin: 0;
}

/* 内容卡片 */
.content-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 32px;
  box-shadow:
    0 20px 40px rgba(0, 0, 0, 0.1),
    0 0 0 1px rgba(255, 255, 255, 0.8) inset;
  animation: cardAppear 0.5s ease-out;
}

@keyframes cardAppear {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

/* 搜索表单 */
.search-form :deep(.ant-input),
.search-form :deep(.ant-select-selector) {
  border-radius: 10px;
  padding: 8px 14px;
  font-size: 14px;
  border: 1px solid #e2e8f0;
  background: #f8fafc;
  transition: all 0.3s;
}

.search-form :deep(.ant-input:hover),
.search-form :deep(.ant-select-selector:hover) {
  border-color: #3b82f6;
  background: #fff;
}

.search-form :deep(.ant-input:focus),
.search-form :deep(.ant-input-affix-wrapper-focused),
.search-form :deep(.ant-select-focused .ant-select-selector) {
  border-color: #3b82f6;
  background: #fff;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.15);
}

.search-form :deep(.ant-btn-primary) {
  height: 42px;
  padding: 0 24px;
  font-size: 15px;
  border-radius: 10px;
  background: linear-gradient(135deg, #3b82f6 0%, #6366f1 100%);
  border: none;
  box-shadow: 0 4px 14px rgba(59, 130, 246, 0.4);
  transition: all 0.3s;
}

.search-form :deep(.ant-btn-primary:hover) {
  background: linear-gradient(135deg, #2563eb 0%, #4f46e5 100%);
  box-shadow: 0 6px 20px rgba(59, 130, 246, 0.5);
  transform: translateY(-2px);
}

.divider {
  margin: 24px 0;
  border-color: #f1f5f9;
}

/* 表格样式 */
:deep(.ant-table-thead > tr > th) {
  background: #f8fafc;
  font-weight: 600;
  color: #475569;
  border-bottom: 2px solid #e2e8f0;
  padding: 16px 12px;
}

:deep(.ant-table-tbody > tr > td) {
  padding: 16px 12px;
  border-bottom: 1px solid #f1f5f9;
  vertical-align: middle;
}

:deep(.ant-table-tbody > tr:hover > td) {
  background: #f8fafc;
}

:deep(.ant-table-wrapper) {
  border-radius: 12px;
  overflow: hidden;
}

.message-text {
  max-width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 13px;
  color: #64748b;
}

.type-tag {
  border-radius: 6px;
  font-size: 12px;
  padding: 2px 10px;
}

.time-text {
  color: #64748b;
  font-size: 13px;
}

.action-space {
  display: flex;
  gap: 8px;
}

.view-btn {
  border-radius: 6px;
  transition: all 0.3s;
}

.delete-btn {
  border-radius: 6px;
  transition: all 0.3s;
}

.delete-btn:hover {
  background: #fef2f2;
  color: #ef4444;
}

/* 分页样式 */
:deep(.ant-pagination) {
  margin-top: 24px;
}

:deep(.ant-pagination-item-active) {
  border-color: #3b82f6;
}

:deep(.ant-pagination-item-active a) {
  color: #3b82f6;
}

/* 响应式 */
@media (max-width: 768px) {
  .container {
    padding: 0 16px;
  }

  .content-card {
    padding: 20px 16px;
  }

  .page-title {
    font-size: 24px;
  }

  .action-space {
    flex-direction: column;
    gap: 4px;
  }
}
</style>

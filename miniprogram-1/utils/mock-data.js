const homeData = {
  banner: {
    eyebrow: '学生服务中心',
    title: '学生服务与党团事务平台',
    subtitle: '当后端请求失败时，将使用本地演示数据兜底。',
    badge: '演示',
    meta: [
      { label: '模块数', value: '3' },
      { label: '状态', value: '已连接' },
      { label: '版本', value: 'V1' },
    ],
  },
  quickEntries: [
    {
      title: '知识库',
      desc: '查询文章与模板信息。',
      icon: '知',
      status: '可用',
      path: '/pages/knowledge/knowledge',
    },
    {
      title: '党团进度',
      desc: '查看当前阶段、记录与提醒。',
      icon: '党',
      status: '可用',
      path: '/pages/party-progress/party-progress',
    },
    {
      title: '请假申请',
      desc: '提交请假并跟踪审批进度。',
      icon: '假',
      status: '可用',
      path: '/pages/leave-list/leave-list',
    },
    {
      title: '通知消息',
      desc: '功能预留，后续接入。',
      icon: '通',
      status: '建设中',
    },
  ],
  todoStats: [
    { label: '未读', value: '0', hint: '消息' },
    { label: '提醒', value: '0', hint: '党团流程' },
    { label: '汇报', value: '0', hint: '待处理' },
  ],
  latestNotices: [],
  downloads: [],
  serviceHighlights: [
    {
      title: '学生服务',
      desc: '围绕查询、申请、跟踪三类流程构建。',
    },
    {
      title: '党团事务',
      desc: '当前演示版聚焦学生端流程查看。',
    },
    {
      title: '下一步',
      desc: '后续可扩展审批、荣誉展示与分析能力。',
    },
  ],
}

const knowledgeBaseData = {
  categories: ['全部'],
  articles: [],
  templates: [],
}

const partyProgressData = {
  profile: {
    name: '演示用户',
    major: '软件工程',
    className: '2023级',
  },
  currentStage: {
    stage: '加载中',
    description: '正在使用本地兜底数据。',
    updatedAt: '',
  },
  stages: [],
  reminders: [],
}

module.exports = {
  homeData,
  knowledgeBaseData,
  partyProgressData,
}

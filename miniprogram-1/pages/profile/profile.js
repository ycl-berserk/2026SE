const TAB_PAGES = [
  '/pages/index/index',
  '/pages/knowledge/knowledge',
  '/pages/party-progress/party-progress',
  '/pages/profile/profile',
]

Page({
  data: {
    profile: {
      name: '演示用户',
      studentId: '20230001',
      department: '软件工程学院',
      className: '2023级 1班',
    },
    shortcuts: [
      {
        title: '我的请假',
        desc: '查看请假申请与审批进度',
        path: '/pages/leave-list/leave-list',
      },
      {
        title: '通知中心',
        desc: '查看通知、知识文章与模板',
        path: '/pages/knowledge/knowledge',
      },
      {
        title: '党团事务',
        desc: '查看当前阶段与提醒事项',
        path: '/pages/party-progress/party-progress',
      },
    ],
    serviceInfo: [
      { label: '账号状态', value: '已登录' },
      { label: '身份角色', value: '学生' },
      { label: '当前版本', value: 'V1' },
    ],
  },

  onShortcutTap(event) {
    const { path } = event.currentTarget.dataset
    if (!path) {
      return
    }

    const navigate = TAB_PAGES.includes(path) ? wx.switchTab : wx.navigateTo
    navigate({ url: path })
  },
})

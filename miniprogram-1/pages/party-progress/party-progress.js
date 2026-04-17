const { partyProgressData } = require('../../utils/mock-data')
const { ensureLogin } = require('../../utils/auth')
const { request } = require('../../utils/request')

Page({
  data: {
    profile: partyProgressData.profile,
    currentStage: partyProgressData.currentStage,
    stages: partyProgressData.stages,
    reminders: partyProgressData.reminders,
  },

  onLoad() {
    this.loadPartyProgress()
  },

  async loadPartyProgress() {
    try {
      await ensureLogin()

      const [profile, overview, records, reminders] = await Promise.all([
        request({ url: '/api/student/profile' }),
        request({ url: '/api/party/me/overview' }),
        request({ url: '/api/party/me/records' }),
        request({ url: '/api/party/me/reminders' }),
      ])

      this.setData({
        profile: {
          name: profile.realName || '演示用户',
          major: profile.major || '',
          className: profile.className || '',
        },
        currentStage: {
          stage: overview.currentStageName || '未知阶段',
          description: `阶段编码：${overview.currentStageCode || '未知'}，待处理提醒：${overview.pendingReminders || 0} 条。`,
          updatedAt: '已同步',
        },
        stages: (records || []).map((item) => ({
          title: item.title || item.stageCode || '阶段记录',
          time: item.eventTime || '',
          desc: item.description || '',
          status: this.mapStageStatus(item.status),
          statusText: this.mapStageStatusText(item.status),
        })),
        reminders: (reminders || []).map((item) => ({
          title: item.title || '提醒事项',
          deadline: item.deadline || '',
          desc: item.content || '',
        })),
      })
    } catch (error) {
      console.error('Load party progress failed:', error)
      wx.showToast({
        title: '已切换到本地流程数据',
        icon: 'none',
      })
    }
  },

  mapStageStatus(status) {
    if (status === 1) {
      return 'done'
    }
    if (status === 0) {
      return 'current'
    }
    return 'pending'
  },

  mapStageStatusText(status) {
    if (status === 1) {
      return '已完成'
    }
    if (status === 0) {
      return '进行中'
    }
    return '待完成'
  },
})

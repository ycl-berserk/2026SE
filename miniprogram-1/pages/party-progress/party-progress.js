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
          name: profile.realName || 'Demo User',
          major: profile.major || '',
          className: profile.className || '',
        },
        currentStage: {
          stage: overview.currentStageName || 'Unknown',
          description: `Stage code: ${overview.currentStageCode || 'unknown'}, reminders: ${overview.pendingReminders || 0}.`,
          updatedAt: 'synced',
        },
        stages: (records || []).map((item) => ({
          title: item.title || item.stageCode || 'Record',
          time: item.eventTime || '',
          desc: item.description || '',
          status: this.mapStageStatus(item.status),
          statusText: this.mapStageStatusText(item.status),
        })),
        reminders: (reminders || []).map((item) => ({
          title: item.title || 'Reminder',
          deadline: item.deadline || '',
          desc: item.content || '',
        })),
      })
    } catch (error) {
      console.error('Load party progress failed:', error)
      wx.showToast({
        title: 'Use mock flow',
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
      return 'done'
    }
    if (status === 0) {
      return 'active'
    }
    return 'pending'
  },
})

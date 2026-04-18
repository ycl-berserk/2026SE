const { ensureLogin } = require('../../utils/auth')
const { request } = require('../../utils/request')

Page({
  data: {
    loading: false,
    activeStatus: 'all',
    tabs: [
      { code: 'all', text: '全部' },
      { code: '0', text: '待审批' },
      { code: '2', text: '已通过' },
      { code: '3', text: '已驳回' },
    ],
    applications: [],
    filteredApplications: [],
  },

  onShow() {
    this.loadApplications()
  },

  async loadApplications() {
    this.setData({ loading: true })
    try {
      await ensureLogin()
      const applications = await request({ url: '/api/leave/me/applications' })
      this.setData({ applications: applications || [] })
      this.applyFilter(this.data.activeStatus)
    } catch (error) {
      console.error('Load leave applications failed:', error)
      wx.showToast({ title: '加载失败', icon: 'none' })
    } finally {
      this.setData({ loading: false })
    }
  },

  onTabTap(event) {
    const { code } = event.currentTarget.dataset
    this.applyFilter(code)
  },

  applyFilter(statusCode) {
    const filtered =
      statusCode === 'all'
        ? this.data.applications
        : this.data.applications.filter((item) => String(item.status) === statusCode)

    this.setData({
      activeStatus: statusCode,
      filteredApplications: filtered,
    })
  },

  onCreateTap() {
    wx.navigateTo({ url: '/pages/leave-form/leave-form' })
  },

  onCardTap(event) {
    const { id } = event.currentTarget.dataset
    wx.navigateTo({ url: `/pages/leave-detail/leave-detail?id=${id}` })
  },
})

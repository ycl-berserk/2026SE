const { ensureLogin } = require('../../utils/auth')
const { request } = require('../../utils/request')

Page({
  data: {
    id: null,
    loading: true,
    detail: null,
  },

  onLoad(options) {
    if (!options.id) {
      wx.showToast({ title: '缺少申请编号', icon: 'none' })
      wx.navigateBack()
      return
    }

    this.setData({ id: options.id })
    this.loadDetail()
  },

  async loadDetail() {
    this.setData({ loading: true })
    try {
      await ensureLogin()
      const detail = await request({
        url: `/api/leave/me/applications/${this.data.id}`,
      })
      this.setData({ detail })
    } catch (error) {
      console.error('Load leave detail failed:', error)
      wx.showToast({ title: '加载失败', icon: 'none' })
    } finally {
      this.setData({ loading: false })
    }
  },
})

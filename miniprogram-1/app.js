const { ensureLogin } = require('./utils/auth')

App({
  async onLaunch() {
    const logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    try {
      const loginResult = await ensureLogin()
      this.globalData.userInfo = loginResult.user || null
      this.globalData.token = loginResult.token || ''
    } catch (error) {
      console.error('Initial login failed:', error)
      wx.showToast({
        title: '后端连接失败',
        icon: 'none',
      })
    }
  },

  globalData: {
    userInfo: null,
    token: '',
  },
})

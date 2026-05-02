const { getSession, redirectToLogin } = require('./utils/auth')

App({
  onLaunch() {
    const logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    const { token, user } = getSession()
    this.globalData.userInfo = user || null
    this.globalData.token = token || ''

    if (!token) {
      redirectToLogin()
    }
  },

  globalData: {
    userInfo: null,
    token: '',
  },
})

const { request } = require('./request')
const { TOKEN_KEY, USER_KEY, DEMO_LOGIN_CODE } = require('./config')

function wxLogin() {
  return new Promise((resolve, reject) => {
    wx.login({
      success: (res) => {
        if (res.code) {
          resolve(res.code)
          return
        }
        reject(new Error('No wx login code returned'))
      },
      fail: reject,
    })
  })
}

function clearLoginState() {
  wx.removeStorageSync(TOKEN_KEY)
  wx.removeStorageSync(USER_KEY)
}

async function ensureLogin(force = false) {
  if (force) {
    clearLoginState()
  }

  const cachedToken = wx.getStorageSync(TOKEN_KEY)
  const cachedUser = wx.getStorageSync(USER_KEY)

  if (!force && cachedToken && cachedUser) {
    return {
      token: cachedToken,
      user: cachedUser,
    }
  }

  let code = DEMO_LOGIN_CODE

  try {
    await wxLogin()
  } catch (error) {
    console.warn('wx.login failed, fallback to demo code:', error)
  }

  const loginData = await request({
    url: '/api/auth/wx-login',
    method: 'POST',
    data: { code },
    header: {
      'Content-Type': 'application/json',
    },
    withAuth: false,
  })

  if (!loginData || loginData.needBind || !loginData.token) {
    throw new Error('Login failed or account needs bind')
  }

  wx.setStorageSync(TOKEN_KEY, loginData.token)
  wx.setStorageSync(USER_KEY, loginData.user || null)

  return loginData
}

module.exports = {
  ensureLogin,
  clearLoginState,
}

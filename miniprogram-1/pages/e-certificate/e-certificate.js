const { ensureLogin } = require('../../utils/auth')
const { request } = require('../../utils/request')
const { BASE_URL, TOKEN_KEY } = require('../../utils/config')

Page({
  data: {
    applications: [],
    loading: false,
    formVisible: false,
    form: {
      title: '',
      reason: '',
      templateType: '在校证明',
    },
    submitting: false,
    templateTypes: ['在校证明', '学籍证明', '成绩证明', '毕业证明', '其他'],
  },

  onShow() {
    this.loadList()
  },

  async loadList() {
    this.setData({ loading: true })
    try {
      await ensureLogin()
      const data = await request({ url: '/api/certificate/me/applications' })
      this.setData({ applications: (data || []).map((item) => ({
        ...item,
        statusText: ['待审批', '审批中', '已通过', '已驳回'][item.status] || '未知',
        submitTime: (item.submitTime || '').replace('T', ' ').slice(0, 16),
        canDownload: item.status === 2 && !!item.certificateFileId,
      })) })
    } catch (error) {
      console.error('Load certificate list failed:', error)
    } finally {
      this.setData({ loading: false })
    }
  },

  showForm() {
    this.setData({ formVisible: true, 'form.title': '', 'form.reason': '', 'form.templateType': '在校证明' })
  },

  hideForm() {
    this.setData({ formVisible: false })
  },

  onTemplateChange(event) {
    this.setData({ 'form.templateType': this.data.templateTypes[event.detail.value] })
  },

  onFieldInput(event) {
    const { field } = event.currentTarget.dataset
    this.setData({ [`form.${field}`]: event.detail.value })
  },

  async onSubmit() {
    const { title, reason, templateType } = this.data.form
    if (!title.trim()) {
      wx.showToast({ title: '请输入证明标题', icon: 'none' })
      return
    }
    this.setData({ submitting: true })
    try {
      await request({
        url: '/api/certificate/me/applications',
        method: 'POST',
        data: { title: title.trim(), reason: reason.trim(), templateType },
      })
      wx.showToast({ title: '提交成功', icon: 'success' })
      this.hideForm()
      this.loadList()
    } catch (error) {
      wx.showToast({ title: error.message || '提交失败', icon: 'none' })
    } finally {
      this.setData({ submitting: false })
    }
  },

  downloadCertificate(event) {
    const fileId = event.currentTarget.dataset.fileid
    if (!fileId) {
      wx.showToast({ title: '证明文件未生成', icon: 'none' })
      return
    }
    const token = wx.getStorageSync(TOKEN_KEY)
    if (!token) {
      wx.showToast({ title: '未登录', icon: 'none' })
      return
    }

    const platform = wx.getDeviceInfo ? (wx.getDeviceInfo().platform || '') : ''
    wx.showLoading({ title: '下载中' })
    wx.downloadFile({
      url: `${BASE_URL}/api/files/${fileId}/download`,
      header: { Authorization: token },
      success: (res) => {
        wx.hideLoading()
        if (res.statusCode !== 200) {
          console.error('电子证明下载失败，HTTP 状态码：', res.statusCode, res)
          wx.showToast({ title: '下载失败', icon: 'none' })
          return
        }
        const filePath = res.filePath || res.tempFilePath
        wx.openDocument({
          filePath,
          fileType: 'pdf',
          showMenu: true,
          fail: (error) => {
            console.error('电子证明打开失败：', error, filePath)
            if (platform === 'devtools') {
              wx.showModal({
                title: '证明已下载',
                content: `开发者工具可能无法预览 PDF，请在真机查看。当前文件路径：${filePath}`,
                showCancel: false,
              })
              return
            }
            wx.showToast({ title: '打开失败', icon: 'none' })
          },
        })
      },
      fail: (error) => {
        wx.hideLoading()
        console.error('电子证明下载失败：', error)
        wx.showToast({ title: '下载失败', icon: 'none' })
      },
    })
  },
})

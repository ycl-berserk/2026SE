const { ensureLogin } = require('../../utils/auth')
const { request } = require('../../utils/request')
const { BASE_URL, TOKEN_KEY } = require('../../utils/config')

Page({
  data: {
    loading: true,
    article: null,
  },

  onLoad(options) {
    const { id } = options || {}
    if (!id) {
      wx.showToast({ title: '缺少文章编号', icon: 'none' })
      this.setData({ loading: false })
      return
    }
    this.loadArticleDetail(id)
  },

  async loadArticleDetail(id) {
    this.setData({ loading: true })

    try {
      await ensureLogin()
      const article = await request({ url: `/api/knowledge/articles/${id}` })
      const tags = this.splitTags(article.tags)

      this.setData({
        article: {
          id: article.id,
          title: article.title || '',
          summary: article.summary || '',
          categoryName: article.categoryName || '未分类',
          source: article.source || '平台',
          answer: article.answer || '',
          content: article.content || '',
          publishTime: article.publishTime || '',
          viewCount: article.viewCount || 0,
          fileId: article.fileId || null,
          contentMode: article.contentMode || 'file',
          editorType: article.editorType || '',
          renderedContent: article.renderedContent || '',
          contentType: this.typeLabel(article.contentType),
          tags,
          targetText: this.targetText(article),
          scenarioCodes: article.scenarioCodes || '',
        },
      })
    } catch (error) {
      console.error('Load article detail failed:', error)
      wx.showToast({ title: '加载失败', icon: 'none' })
    } finally {
      this.setData({ loading: false })
    }
  },

  splitTags(tags) {
    if (!tags) return []
    return String(tags).split(/[,，]/).map((item) => item.trim()).filter(Boolean)
  },

  typeLabel(type) {
    const labels = { policy: '政策', process: '流程', faq: '问答', guide: '指南' }
    return labels[type] || '指南'
  },

  targetText(article) {
    const parts = []
    if (article.targetGrades) parts.push(`年级：${article.targetGrades}`)
    if (article.targetMajors) parts.push(`专业：${article.targetMajors}`)
    if (article.targetPoliticalStatuses) parts.push(`政治面貌：${article.targetPoliticalStatuses}`)
    if (article.targetPartyStages) parts.push(`党团阶段：${article.targetPartyStages}`)
    return parts.join('；') || '适用于全体学生'
  },

  onDownloadTap() {
    const article = this.data.article
    if (!article) return
    const token = wx.getStorageSync(TOKEN_KEY)
    if (!token) {
      wx.showToast({ title: '登录已失效', icon: 'none' })
      return
    }
    if (article.contentMode === 'editor') {
      this.downloadUrl(`${BASE_URL}/api/knowledge/articles/${article.id}/source`, token, '源文件')
      return
    }
    if (!article.fileId) {
      wx.showToast({ title: '暂无资料文件', icon: 'none' })
      return
    }
    this.downloadUrl(`${BASE_URL}/api/files/${article.fileId}/download`, token, '资料')
  },

  downloadUrl(url, token, label) {
    const platform = this.getPlatform()
    if (platform === 'devtools') {
      this.readTextFileInDevtools(url, token, label)
      return
    }

    const downloadOptions = {
      url,
      header: { Authorization: token },
    }

    wx.showLoading({ title: '下载中' })
    wx.downloadFile({
      ...downloadOptions,
      success: (res) => {
        wx.hideLoading()
        if (res.statusCode !== 200) {
          console.error(`${label}下载失败，HTTP 状态码：`, res.statusCode, res)
          wx.showToast({ title: '下载失败', icon: 'none' })
          return
        }
        wx.openDocument({
          filePath: res.tempFilePath,
          showMenu: true,
          fail: () => wx.showToast({ title: '打开失败', icon: 'none' }),
        })
      },
      fail: (error) => {
        wx.hideLoading()
        console.error(`${label}下载失败：`, error)
        wx.showToast({ title: '下载失败', icon: 'none' })
      },
    })
  },

  getPlatform() {
    if (wx.getDeviceInfo) {
      return wx.getDeviceInfo().platform || ''
    }
    return ''
  },

  readTextFileInDevtools(url, token, label) {
    wx.showLoading({ title: '读取中' })
    wx.request({
      url,
      header: { Authorization: token },
      responseType: 'text',
      success: (res) => {
        wx.hideLoading()
        if (res.statusCode !== 200) {
          console.error(`${label}读取失败，HTTP 状态码：`, res.statusCode, res)
          wx.showToast({ title: '读取失败', icon: 'none' })
          return
        }

        const content = typeof res.data === 'string' ? res.data : JSON.stringify(res.data, null, 2)
        console.log(`${label}文件内容：\n`, content)
        wx.setClipboardData({
          data: content,
          success: () => {
            wx.showModal({
              title: `${label}已读取`,
              content: '开发者工具中已直接读取文件内容，并复制到剪贴板；完整内容也已输出到 Console。',
              showCancel: false,
            })
          },
          fail: (error) => {
            console.warn(`${label}内容复制失败：`, error)
            wx.showModal({
              title: `${label}已读取`,
              content: '开发者工具中已直接读取文件内容，完整内容已输出到 Console。',
              showCancel: false,
            })
          },
        })
      },
      fail: (error) => {
        wx.hideLoading()
        console.error(`${label}读取失败：`, error)
        wx.showToast({ title: '读取失败', icon: 'none' })
      },
    })
  },

  handleDevtoolsDownload(filePath, label) {
    console.log(`${label}已下载到开发者工具临时路径：`, filePath)

    wx.getFileSystemManager().readFile({
      filePath,
      encoding: 'utf8',
      success: (fileRes) => {
        console.log(`${label}文件内容：\n`, fileRes.data)
        wx.setClipboardData({
          data: fileRes.data,
          success: () => {
            wx.showModal({
              title: `${label}已读取`,
              content: '开发者工具的 http://tmp 临时路径不能直接在浏览器打开。文件内容已复制到剪贴板，也已输出到 Console。',
              showCancel: false,
            })
          },
          fail: (error) => {
            console.warn(`${label}内容复制失败：`, error)
            wx.showModal({
              title: `${label}已读取`,
              content: '开发者工具的 http://tmp 临时路径不能直接在浏览器打开。文件内容已输出到 Console。',
              showCancel: false,
            })
          },
        })
      },
      fail: (error) => {
        console.error(`${label}临时文件读取失败：`, error)
        this.showDevtoolsDownloadTip(filePath, label)
      },
    })
  },

  showDevtoolsDownloadTip(filePath, label) {
    wx.showModal({
      title: `${label}已下载`,
      content: `开发者工具只能拿到小程序临时路径，不能复制到浏览器打开。请在真机中打开，或在 Console 查看 tempFilePath：${filePath}`,
      showCancel: false,
    })
  },
})

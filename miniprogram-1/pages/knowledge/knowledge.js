const { knowledgeBaseData } = require('../../utils/mock-data')
const { ensureLogin } = require('../../utils/auth')
const { request } = require('../../utils/request')
const { BASE_URL, TOKEN_KEY } = require('../../utils/config')

Page({
  data: {
    keyword: '',
    leaveActions: [
      {
        title: '发起请假',
        desc: '填写请假时间、事由与联系方式',
        path: '/pages/leave-form/leave-form',
      },
      {
        title: '我的请假',
        desc: '查看申请记录与当前审批进度',
        path: '/pages/leave-list/leave-list',
      },
    ],
    categories: knowledgeBaseData.categories,
    selectedCategory: '鍏ㄩ儴',
    articles: knowledgeBaseData.articles,
    visibleArticles: knowledgeBaseData.articles,
    templates: knowledgeBaseData.templates,
  },

  onLoad() {
    this.loadKnowledgeData()
  },

  async loadKnowledgeData() {
    try {
      await ensureLogin()

      const [articleData, templateData] = await Promise.all([
        request({ url: '/api/knowledge/articles' }),
        request({ url: '/api/knowledge/templates' }),
      ])

      const articles = (articleData.records || articleData.list || []).map((item) => ({
        id: item.id,
        category: item.categoryName || '未分类',
        source: '平台',
        title: item.title,
        summary: item.summary || '',
        answer: item.summary || '点击进入详情查看',
        keywords: [item.title, item.summary || ''].join(' ').split(/\s+/).filter(Boolean),
      }))

      const categories = ['鍏ㄩ儴', ...new Set(articles.map((item) => item.category))]
      const templates = (templateData || []).map((item) => ({
        id: item.id,
        name: item.name,
        desc: item.description || '',
        format: item.format || '文件',
        fileId: item.fileId || null,
      }))

      this.setData({
        categories,
        selectedCategory: '鍏ㄩ儴',
        articles,
        visibleArticles: articles,
        templates,
      })
    } catch (error) {
      console.error('Load knowledge data failed:', error)
      wx.showToast({
        title: '已切换到本地数据',
        icon: 'none',
      })
    }
  },

  onKeywordInput(event) {
    this.setData({
      keyword: event.detail.value.trim(),
    })

    this.applyFilters()
  },

  onCategoryTap(event) {
    this.setData({
      selectedCategory: event.currentTarget.dataset.category,
    })

    this.applyFilters()
  },

  applyFilters() {
    const { keyword, selectedCategory, articles } = this.data
    const normalizedKeyword = keyword.toLowerCase()

    const visibleArticles = articles.filter((item) => {
      const matchCategory = selectedCategory === '鍏ㄩ儴' || item.category === selectedCategory
      const matchKeyword = !normalizedKeyword
        || item.title.toLowerCase().includes(normalizedKeyword)
        || item.summary.toLowerCase().includes(normalizedKeyword)
        || item.keywords.join(' ').toLowerCase().includes(normalizedKeyword)

      return matchCategory && matchKeyword
    })

    this.setData({ visibleArticles })
  },

  onLeaveActionTap(event) {
    const { path } = event.currentTarget.dataset
    if (!path) {
      return
    }

    wx.navigateTo({ url: path })
  },

  onTemplateTap(event) {
    const { fileId, name } = event.currentTarget.dataset
    if (!fileId) {
      wx.showToast({
        title: `${name}暂无文件`,
        icon: 'none',
      })
      return
    }

    const token = wx.getStorageSync(TOKEN_KEY)
    if (!token) {
      wx.showToast({
        title: '登录已失效',
        icon: 'none',
      })
      return
    }

    wx.showLoading({ title: '下载中' })
    wx.downloadFile({
      url: `${BASE_URL}/api/files/${fileId}/download`,
      header: {
        Authorization: token,
      },
      success: (res) => {
        if (res.statusCode !== 200) {
          wx.hideLoading()
          wx.showToast({
            title: '下载失败',
            icon: 'none',
          })
          return
        }

        wx.hideLoading()

        const platform = (wx.getSystemInfoSync && wx.getSystemInfoSync().platform) || ''
        if (platform === 'devtools') {
          wx.showToast({
            title: '开发者工具不支持预览，请在真机打开',
            icon: 'none',
          })
          return
        }

        wx.openDocument({
          filePath: res.tempFilePath,
          showMenu: true,
          fail: (error) => {
            console.error('Open document failed:', error)
            wx.showToast({
              title: '打开失败',
              icon: 'none',
            })
          },
        })
      },
      fail: (error) => {
        console.error('Download template failed:', error)
        wx.hideLoading()
        wx.showToast({
          title: '下载失败',
          icon: 'none',
        })
      },
    })
  },

  onArticleTap(event) {
    const { id } = event.currentTarget.dataset
    if (!id) {
      return
    }

    wx.navigateTo({
      url: `/pages/knowledge-detail/knowledge-detail?id=${id}`,
    })
  },
})

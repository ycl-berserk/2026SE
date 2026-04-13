const { ensureLogin } = require('../../utils/auth')
const { request } = require('../../utils/request')

Page({
  data: {
    loading: true,
    article: null,
  },

  onLoad(options) {
    const { id } = options || {}
    if (!id) {
      wx.showToast({
        title: 'Missing article id',
        icon: 'none',
      })
      this.setData({ loading: false })
      return
    }

    this.loadArticleDetail(id)
  },

  async loadArticleDetail(id) {
    this.setData({ loading: true })

    try {
      await ensureLogin()
      const article = await request({
        url: `/api/knowledge/articles/${id}`,
      })

      this.setData({
        article: {
          id: article.id,
          title: article.title || '',
          summary: article.summary || '',
          categoryName: article.categoryName || 'Uncategorized',
          source: article.source || 'API',
          answer: article.answer || '',
          content: article.content || '',
          publishTime: article.publishTime || '',
          viewCount: article.viewCount || 0,
        },
      })
    } catch (error) {
      console.error('Load article detail failed:', error)
      wx.showToast({
        title: 'Load failed',
        icon: 'none',
      })
    } finally {
      this.setData({ loading: false })
    }
  },
})

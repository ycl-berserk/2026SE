const { knowledgeBaseData } = require('../../utils/mock-data')
const { ensureLogin } = require('../../utils/auth')
const { request } = require('../../utils/request')

Page({
  data: {
    keyword: '',
    categories: knowledgeBaseData.categories,
    selectedCategory: 'All',
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
        category: item.categoryName || 'Uncategorized',
        source: 'API',
        title: item.title,
        summary: item.summary || '',
        answer: item.summary || 'Open detail page later.',
        keywords: [item.title, item.summary || ''].join(' ').split(/\s+/).filter(Boolean),
      }))

      const categories = ['All', ...new Set(articles.map((item) => item.category))]
      const templates = (templateData || []).map((item) => ({
        id: item.id,
        name: item.name,
        desc: item.description || '',
        format: item.format || 'FILE',
      }))

      this.setData({
        categories,
        selectedCategory: 'All',
        articles,
        visibleArticles: articles,
        templates,
      })
    } catch (error) {
      console.error('Load knowledge data failed:', error)
      wx.showToast({
        title: 'Use mock list',
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
      const matchCategory = selectedCategory === 'All' || item.category === selectedCategory
      const matchKeyword = !normalizedKeyword
        || item.title.toLowerCase().includes(normalizedKeyword)
        || item.summary.toLowerCase().includes(normalizedKeyword)
        || item.keywords.join(' ').toLowerCase().includes(normalizedKeyword)

      return matchCategory && matchKeyword
    })

    this.setData({ visibleArticles })
  },

  onTemplateTap(event) {
    wx.showToast({
      title: `${event.currentTarget.dataset.name} pending`,
      icon: 'none',
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

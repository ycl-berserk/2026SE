const { knowledgeBaseData } = require('../../utils/mock-data')

Page({
  data: {
    keyword: '',
    categories: knowledgeBaseData.categories,
    selectedCategory: '全部',
    articles: knowledgeBaseData.articles,
    visibleArticles: knowledgeBaseData.articles,
    templates: knowledgeBaseData.templates,
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
      const matchCategory = selectedCategory === '全部' || item.category === selectedCategory
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
      title: `${event.currentTarget.dataset.name}待接入`,
      icon: 'none',
    })
  },
})

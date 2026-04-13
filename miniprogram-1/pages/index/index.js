const { homeData } = require('../../utils/mock-data')
const { ensureLogin } = require('../../utils/auth')
const { request } = require('../../utils/request')

Page({
  data: {
    banner: homeData.banner,
    quickEntries: homeData.quickEntries,
    todoStats: homeData.todoStats,
    latestNotices: homeData.latestNotices,
    downloads: homeData.downloads,
    serviceHighlights: homeData.serviceHighlights,
    loading: false,
  },

  onLoad() {
    this.loadHomeData()
  },

  async loadHomeData() {
    this.setData({ loading: true })

    try {
      await ensureLogin()
      const data = await request({ url: '/api/home' })

      this.setData({
        banner: this.buildBanner(data.banner),
        quickEntries: this.buildQuickEntries(data.quickEntries),
        todoStats: this.buildTodoStats(data.todoStats),
        latestNotices: this.buildLatestNotices(data.latestNotices),
        downloads: this.buildDownloads(data.downloads),
      })
    } catch (error) {
      console.error('Load home data failed:', error)
      wx.showToast({
        title: 'Use mock home',
        icon: 'none',
      })
    } finally {
      this.setData({ loading: false })
    }
  },

  buildBanner(remoteBanner = {}) {
    return {
      ...homeData.banner,
      title: remoteBanner.title || homeData.banner.title,
      subtitle: remoteBanner.subtitle || homeData.banner.subtitle,
    }
  },

  buildQuickEntries(remoteEntries = []) {
    const pathMap = {
      knowledge: '/pages/knowledge/knowledge',
      party: '/pages/party-progress/party-progress',
    }

    if (!remoteEntries.length) {
      return homeData.quickEntries
    }

    return remoteEntries.map((item) => ({
      title: item.name || item.code || 'Entry',
      desc: `Code: ${item.code || 'unknown'}`,
      icon: (item.name || item.code || 'E').slice(0, 1),
      status: pathMap[item.code] ? 'Ready' : 'Pending',
      path: pathMap[item.code] || '',
    }))
  },

  buildTodoStats(remoteStats = {}) {
    return [
      {
        label: 'Unread',
        value: String(remoteStats.unreadMessages || 0),
        hint: 'messages',
      },
      {
        label: 'Reminders',
        value: String(remoteStats.upcomingDeadlines || 0),
        hint: 'party flow',
      },
      {
        label: 'Reports',
        value: String(remoteStats.pendingReports || 0),
        hint: 'pending',
      },
    ]
  },

  buildLatestNotices(remoteNotices = []) {
    return (remoteNotices || []).map((item) => ({
      tag: item.tag || 'Notice',
      date: item.publishDate || item.date || '',
      title: item.title || 'Untitled',
      summary: item.summary || '',
    }))
  },

  buildDownloads(remoteDownloads = []) {
    return (remoteDownloads || []).map((item) => ({
      name: item.name || 'Template',
      desc: item.description || '',
    }))
  },

  handleEntryTap(event) {
    const { path, title } = event.currentTarget.dataset

    if (path) {
      wx.navigateTo({ url: path })
      return
    }

    wx.showToast({
      title: `${title} pending`,
      icon: 'none',
    })
  },

  handleDownloadTap(event) {
    const { name } = event.currentTarget.dataset

    wx.showToast({
      title: `${name} pending`,
      icon: 'none',
    })
  },
})

const { homeData } = require('../../utils/mock-data')

Page({
  data: {
    banner: homeData.banner,
    quickEntries: homeData.quickEntries,
    todoStats: homeData.todoStats,
    latestNotices: homeData.latestNotices,
    downloads: homeData.downloads,
    serviceHighlights: homeData.serviceHighlights,
  },

  handleEntryTap(event) {
    const { path, title } = event.currentTarget.dataset

    if (path) {
      wx.navigateTo({ url: path })
      return
    }

    wx.showToast({
      title: `${title}待接入`,
      icon: 'none',
    })
  },

  handleDownloadTap(event) {
    const { name } = event.currentTarget.dataset

    wx.showToast({
      title: `${name}接口待接入`,
      icon: 'none',
    })
  },
})

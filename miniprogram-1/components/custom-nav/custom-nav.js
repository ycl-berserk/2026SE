Component({
  properties: {
    title: {
      type: String,
      value: '',
    },
    titleOffset: {
      type: Number,
      value: 0,
      observer(value) {
        this.updateTitleTop(value)
      },
    },
  },

  data: {
    navBarHeight: 56,
    navTitleTop: 26,
    titleTop: 26,
    canBack: false,
  },

  lifetimes: {
    attached() {
      this.initMetrics()
    },
  },

  methods: {
    initMetrics() {
      const info = wx.getWindowInfo ? wx.getWindowInfo() : wx.getSystemInfoSync()
      let menu = null
      try {
        menu = wx.getMenuButtonBoundingClientRect ? wx.getMenuButtonBoundingClientRect() : null
      } catch (e) {}

      const statusBarHeight = info.statusBarHeight || 0
      const navBarHeight = menu && menu.bottom ? menu.bottom + 6 : statusBarHeight + 48
      const navTitleTop = menu && menu.top ? Math.max(statusBarHeight + 2, menu.top - 6) : statusBarHeight + 8
      const canBack = getCurrentPages().length > 1

      this.setData({
        navBarHeight,
        navTitleTop,
        titleTop: navTitleTop + (Number(this.data.titleOffset) || 0),
        canBack,
      })
    },

    updateTitleTop(offset) {
      this.setData({
        titleTop: this.data.navTitleTop + (Number(offset) || 0),
      })
    },

    onBack() {
      if (getCurrentPages().length > 1) {
        wx.navigateBack()
      } else {
        wx.switchTab({ url: '/pages/index/index' })
      }
    },
  },
})

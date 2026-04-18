const { ensureLogin } = require('../../utils/auth')
const { request } = require('../../utils/request')

Page({
  data: {
    submitting: false,
    title: '',
    reason: '',
    leaveStartDate: '',
    leaveEndDate: '',
    contactPhone: '',
  },

  onTitleInput(event) {
    this.setData({ title: event.detail.value })
  },

  onReasonInput(event) {
    this.setData({ reason: event.detail.value })
  },

  onStartDateChange(event) {
    this.setData({ leaveStartDate: event.detail.value })
  },

  onEndDateChange(event) {
    this.setData({ leaveEndDate: event.detail.value })
  },

  onPhoneInput(event) {
    this.setData({ contactPhone: event.detail.value })
  },

  async onSubmit() {
    if (!this.validateForm()) {
      return
    }

    this.setData({ submitting: true })
    try {
      await ensureLogin()
      const payload = {
        title: this.data.title.trim(),
        reason: this.data.reason.trim(),
        leaveStartDate: this.data.leaveStartDate,
        leaveEndDate: this.data.leaveEndDate,
        contactPhone: this.data.contactPhone.trim(),
      }
      await request({
        url: '/api/leave/me/applications',
        method: 'POST',
        data: payload,
      })
      wx.showToast({ title: '提交成功', icon: 'success' })
      setTimeout(() => {
        wx.redirectTo({ url: '/pages/leave-list/leave-list' })
      }, 500)
    } catch (error) {
      console.error('Create leave application failed:', error)
      wx.showToast({ title: error.message || '提交失败', icon: 'none' })
    } finally {
      this.setData({ submitting: false })
    }
  },

  validateForm() {
    const { title, reason, leaveStartDate, leaveEndDate, contactPhone } = this.data
    if (!title.trim() || !reason.trim() || !leaveStartDate || !leaveEndDate || !contactPhone.trim()) {
      wx.showToast({ title: '请完整填写表单', icon: 'none' })
      return false
    }
    if (leaveEndDate < leaveStartDate) {
      wx.showToast({ title: '结束日期不能早于开始日期', icon: 'none' })
      return false
    }
    return true
  },
})

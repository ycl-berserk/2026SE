const { partyProgressData } = require('../../utils/mock-data')

Page({
  data: {
    profile: partyProgressData.profile,
    currentStage: partyProgressData.currentStage,
    stages: partyProgressData.stages,
    reminders: partyProgressData.reminders,
  },
})

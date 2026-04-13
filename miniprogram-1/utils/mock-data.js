const homeData = {
  banner: {
    eyebrow: 'STUDENT SERVICE HUB',
    title: 'Student Service and Party Affairs Platform',
    subtitle: 'Local demo data used as a fallback when backend requests fail.',
    badge: 'Demo',
    meta: [
      { label: 'Modules', value: '3' },
      { label: 'Status', value: 'Connected' },
      { label: 'Version', value: 'V1' },
    ],
  },
  quickEntries: [
    {
      title: 'Knowledge Base',
      desc: 'Query articles and template metadata.',
      icon: 'K',
      status: 'Ready',
      path: '/pages/knowledge/knowledge',
    },
    {
      title: 'Party Progress',
      desc: 'View current stage, records and reminders.',
      icon: 'P',
      status: 'Ready',
      path: '/pages/party-progress/party-progress',
    },
    {
      title: 'Certificates',
      desc: 'Reserved for later integration.',
      icon: 'C',
      status: 'Pending',
    },
    {
      title: 'Notifications',
      desc: 'Reserved for later integration.',
      icon: 'N',
      status: 'Pending',
    },
  ],
  todoStats: [
    { label: 'Unread', value: '0', hint: 'messages' },
    { label: 'Reminders', value: '0', hint: 'party flow' },
    { label: 'Reports', value: '0', hint: 'pending' },
  ],
  latestNotices: [],
  downloads: [],
  serviceHighlights: [
    {
      title: 'Student Services',
      desc: 'Built around query, apply and track workflows.',
    },
    {
      title: 'Party Affairs',
      desc: 'Current demo focuses on the student-side process view.',
    },
    {
      title: 'Next Steps',
      desc: 'Approvals, honors and analytics can be added later.',
    },
  ],
}

const knowledgeBaseData = {
  categories: ['All'],
  articles: [],
  templates: [],
}

const partyProgressData = {
  profile: {
    name: 'Demo User',
    major: 'Software Engineering',
    className: '2023',
  },
  currentStage: {
    stage: 'Loading',
    description: 'Using local fallback data.',
    updatedAt: '',
  },
  stages: [],
  reminders: [],
}

module.exports = {
  homeData,
  knowledgeBaseData,
  partyProgressData,
}

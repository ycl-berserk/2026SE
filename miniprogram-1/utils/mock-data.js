const homeData = {
  banner: {
    eyebrow: 'STUDENT SERVICE HUB',
    title: '学院学生综合服务与党团管理平台',
    subtitle: '聚合事务办理、政策查询、流程追踪与通知服务，先落地学生端高频入口。',
    badge: '需求 V1.0',
    meta: [
      { label: '覆盖模块', value: '10 个' },
      { label: '服务要求', value: '8 类' },
      { label: '优先能力', value: 'P0 / P1' },
    ],
  },
  quickEntries: [
    {
      title: '政策知识库',
      desc: '搜索高频事务，查看标准答案与办理建议。',
      icon: '问',
      status: '已实现',
      path: '/pages/knowledge/knowledge',
    },
    {
      title: '党团进度',
      desc: '查看当前阶段、已完成事项和近期提醒。',
      icon: '党',
      status: '已实现',
      path: '/pages/party-progress/party-progress',
    },
    {
      title: '电子证明',
      desc: '证明申请、请假审批、盖章流程统一入口。',
      icon: '证',
      status: '待接入',
    },
    {
      title: '精准通知',
      desc: '按年级、专业、毕业状态做定向消息触达。',
      icon: '讯',
      status: '待接入',
    },
  ],
  todoStats: [
    { label: '待提交', value: '1', hint: '思想汇报' },
    { label: '审批中', value: '2', hint: '证明 / 请假' },
    { label: '新通知', value: '3', hint: '定向推送' },
  ],
  latestNotices: [
    {
      tag: '就业',
      date: '04-02',
      title: '2026 届毕业生三方协议集中答疑安排',
      summary: '面向毕业生推送，后续支持按毕业状态和专业标签定向送达。',
    },
    {
      tag: '党团',
      date: '04-01',
      title: '积极分子第二季度思想汇报提交提醒',
      summary: '对应党团流程关键节点，后续可在消息中心和微信订阅里同步提醒。',
    },
  ],
  downloads: [
    { name: '请假申请模板', desc: '用于学生请假场景的标准表单。' },
    { name: '经费预算表', desc: '用于活动申报、预算填报与归档。' },
    { name: '活动简报模板', desc: '用于党团活动总结和材料整理。' },
  ],
  serviceHighlights: [
    {
      title: '学生服务',
      desc: '围绕查询、办理、追踪三条主链路设计，首页先承接高频事务入口。',
    },
    {
      title: '党团管理',
      desc: '采用标准化时间线表达阶段状态，便于学生查看、老师核验和后续配置。',
    },
    {
      title: '后续扩展',
      desc: '电子审批、荣誉展示、画像与学业预警可在现有导航结构上继续扩展。',
    },
  ],
}

const knowledgeBaseData = {
  categories: ['全部', '党团流程', '学籍事务', '奖助资助', '日常服务'],
  articles: [
    {
      category: '党团流程',
      source: '制度摘要',
      title: '入党申请后下一步通常是什么？',
      summary: '学生最关心的是提交申请书后多久进入下一阶段，以及需要做哪些准备。',
      answer: '一般先进入申请人阶段，后续根据培养考察安排参加培训、提交思想汇报，并由老师或支部持续跟踪。',
      keywords: ['入党', '申请书', '流程', '积极分子'],
    },
    {
      category: '奖助资助',
      source: '辅导说明',
      title: '奖助学金材料一般需要准备哪些内容？',
      summary: '不同项目字段会有差异，但通常都需要基础身份信息、成绩或综测信息、佐证材料。',
      answer: '建议优先查看当次通知要求，准备成绩证明、获奖证书、家庭情况说明等，并在截止前完成线上提交。',
      keywords: ['奖学金', '助学金', '材料', '综测'],
    },
    {
      category: '学籍事务',
      source: '办理指引',
      title: '休学复学应从哪里开始办理？',
      summary: '该类敏感事务应优先返回标准流程与官方办理入口，不直接给出不确定结论。',
      answer: '应先查看学院发布的办理通知或联系辅导员确认条件，平台后续可提供表单入口、材料清单和状态查询。',
      keywords: ['休学', '复学', '学籍', '办理'],
    },
  ],
  templates: [
    { name: '证明申请单', desc: '统一证明开具模板。', format: 'DOCX' },
    { name: '请假条模板', desc: '请假审批附件模板。', format: 'DOCX' },
    { name: '预算申报表', desc: '活动预算与报销准备表。', format: 'XLSX' },
  ],
}

const partyProgressData = {
  profile: {
    name: '张同学',
    major: '计算机科学与技术',
    className: '2023 级 2 班',
  },
  currentStage: {
    stage: '积极分子',
    description: '当前已完成申请和初步培养，等待下一次集中培训安排与阶段考察。',
    updatedAt: '2026-04-02 18:00',
  },
  stages: [
    {
      title: '入党申请人',
      time: '2025-09-20',
      desc: '已提交入党申请书，材料审核完成。',
      status: 'done',
      statusText: '已完成',
    },
    {
      title: '积极分子',
      time: '2026-03-15',
      desc: '进入积极分子培养阶段，需按要求提交思想汇报。',
      status: 'current',
      statusText: '当前阶段',
    },
    {
      title: '发展对象',
      time: '待定',
      desc: '完成后续培训、考察和材料核验后进入下一阶段。',
      status: 'pending',
      statusText: '未开始',
    },
    {
      title: '预备党员',
      time: '待定',
      desc: '根据支部流程进入预备党员阶段并继续考察。',
      status: 'pending',
      statusText: '未开始',
    },
    {
      title: '正式党员',
      time: '待定',
      desc: '转正完成后归档，支持长期查询。',
      status: 'pending',
      statusText: '未开始',
    },
  ],
  reminders: [
    {
      title: '第二季度思想汇报',
      deadline: '截止 2026-04-15',
      desc: '建议在截止前 3 天再次推送提醒，并支持附件上传与提交记录查询。',
    },
    {
      title: '党课培训签到确认',
      deadline: '待学院排期',
      desc: '后续可关联培训场次、签到结果和补训记录。',
    },
  ],
}

module.exports = {
  homeData,
  knowledgeBaseData,
  partyProgressData,
}

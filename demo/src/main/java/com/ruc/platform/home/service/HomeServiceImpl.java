package com.ruc.platform.home.service;

import com.ruc.platform.home.vo.HomeVO;
import com.ruc.platform.home.vo.TodoStatsVO;
import com.ruc.platform.notice.mapper.UserMessageMapper;
import com.ruc.platform.party.entity.PartyReminder;
import com.ruc.platform.party.mapper.PartyReminderMapper;
import com.ruc.platform.party.mapper.PartyReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final UserMessageMapper userMessageMapper;
    private final PartyReminderMapper partyReminderMapper;
    private final PartyReportMapper partyReportMapper;

    @Override
    public HomeVO getHomeData(Long userId) {
        HomeVO homeVO = new HomeVO();

        Map<String, String> banner = new HashMap<>();
        banner.put("title", "欢迎使用学院服务平台");
        banner.put("subtitle", "便捷获取政策信息与党团服务");
        homeVO.setBanner(banner);

        List<Map<String, String>> quickEntries = new ArrayList<>();
        quickEntries.add(createEntry("knowledge", "知识库", "book"));
        quickEntries.add(createEntry("party", "党团进度", "flag"));
        quickEntries.add(createEntry("notice", "通知消息", "bell"));
        quickEntries.add(createEntry("certificate", "证明申请", "file"));
        homeVO.setQuickEntries(quickEntries);

        homeVO.setTodoStats(getTodoStats(userId));
        homeVO.setLatestNotices(new ArrayList<>());
        homeVO.setDownloads(new ArrayList<>());
        return homeVO;
    }

    private Map<String, String> createEntry(String code, String name, String icon) {
        Map<String, String> entry = new HashMap<>();
        entry.put("code", code);
        entry.put("name", name);
        entry.put("icon", icon);
        return entry;
    }

    private TodoStatsVO getTodoStats(Long userId) {
        TodoStatsVO stats = new TodoStatsVO();
        Long unreadCount = userMessageMapper.countUnreadByUserId(userId);
        List<PartyReminder> reminders = partyReminderMapper.selectPendingByUserId(userId);
        stats.setUnreadMessages(unreadCount == null ? 0 : unreadCount.intValue());
        stats.setUpcomingDeadlines(reminders.size());
        stats.setPendingReports(0);
        return stats;
    }
}

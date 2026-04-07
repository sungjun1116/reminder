package sungjun.ai.reminder.dto;

import sungjun.ai.reminder.domain.ReminderList;

import java.time.LocalDateTime;

public record ReminderListResponse(
        Long id,
        String name,
        String color,
        String icon,
        Long groupId,
        Integer sortOrder,
        LocalDateTime createdAt
) {
    public static ReminderListResponse from(ReminderList reminderList) {
        return new ReminderListResponse(
                reminderList.getId(),
                reminderList.getName(),
                reminderList.getColor(),
                reminderList.getIcon(),
                reminderList.getGroupId(),
                reminderList.getSortOrder(),
                reminderList.getCreatedAt()
        );
    }
}

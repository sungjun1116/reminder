package sungjun.ai.reminder.dto;

import sungjun.ai.reminder.domain.Reminder;

import java.time.LocalDateTime;

public record ReminderResponse(
        Long id,
        Long listId,
        String title,
        Boolean completed,
        Integer sortOrder,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ReminderResponse from(Reminder reminder) {
        return new ReminderResponse(
                reminder.getId(),
                reminder.getListId(),
                reminder.getTitle(),
                reminder.getCompleted(),
                reminder.getSortOrder(),
                reminder.getCreatedAt(),
                reminder.getUpdatedAt()
        );
    }
}


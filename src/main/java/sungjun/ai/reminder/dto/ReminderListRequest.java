package sungjun.ai.reminder.dto;

import jakarta.validation.constraints.NotBlank;

public record ReminderListRequest(
        @NotBlank String name,
        String color,
        String icon,
        Long groupId
) {
}

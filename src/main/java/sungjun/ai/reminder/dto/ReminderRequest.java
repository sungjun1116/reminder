package sungjun.ai.reminder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReminderRequest(
        @NotBlank String title
) {
}


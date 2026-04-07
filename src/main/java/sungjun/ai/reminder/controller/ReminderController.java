package sungjun.ai.reminder.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sungjun.ai.reminder.dto.ReminderRequest;
import sungjun.ai.reminder.dto.ReminderResponse;
import sungjun.ai.reminder.service.ports.in.ReminderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;

    @GetMapping("/api/lists/{listId}/reminders")
    public List<ReminderResponse> getByListId(@PathVariable Long listId) {
        return reminderService.findByListId(listId).stream()
                .map(ReminderResponse::from)
                .toList();
    }

    @PostMapping("/api/lists/{listId}/reminders")
    @ResponseStatus(HttpStatus.CREATED)
    public ReminderResponse create(@PathVariable Long listId, @RequestBody @Valid ReminderRequest request) {
        return ReminderResponse.from(
                reminderService.create(listId, request.title())
        );
    }

    @PutMapping("/api/reminders/{id}")
    public ReminderResponse update(@PathVariable Long id, @RequestBody @Valid ReminderRequest request) {
        return ReminderResponse.from(
                reminderService.update(id, request.title())
        );
    }

    @DeleteMapping("/api/reminders/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reminderService.delete(id);
    }
}


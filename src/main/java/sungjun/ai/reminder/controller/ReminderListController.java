package sungjun.ai.reminder.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sungjun.ai.reminder.dto.ReminderListRequest;
import sungjun.ai.reminder.dto.ReminderListResponse;
import sungjun.ai.reminder.service.ports.in.ReminderListService;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
@RequiredArgsConstructor
public class ReminderListController {

    private final ReminderListService reminderListService;

    @GetMapping
    public List<ReminderListResponse> getAll() {
        return reminderListService.findAll().stream()
                .map(ReminderListResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ReminderListResponse getOne(@PathVariable Long id) {
        return ReminderListResponse.from(reminderListService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReminderListResponse create(@RequestBody @Valid ReminderListRequest request) {
        return ReminderListResponse.from(
                reminderListService.create(request.name(), request.color(), request.icon(), request.groupId())
        );
    }

    @PutMapping("/{id}")
    public ReminderListResponse update(@PathVariable Long id, @RequestBody @Valid ReminderListRequest request) {
        return ReminderListResponse.from(
                reminderListService.update(id, request.name(), request.color(), request.icon(), request.groupId())
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reminderListService.delete(id);
    }
}

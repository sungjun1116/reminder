package sungjun.ai.reminder.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sungjun.ai.reminder.domain.Reminder;
import sungjun.ai.reminder.repository.ReminderRepository;
import sungjun.ai.reminder.service.ports.in.ReminderService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultReminderService implements ReminderService {

    private final ReminderRepository reminderRepository;

    @Override
    public List<Reminder> findByListId(Long listId) {
        return reminderRepository.findByListIdOrderBySortOrderAsc(listId);
    }

    @Override
    public Reminder findById(Long id) {
        return reminderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reminder not found: " + id));
    }

    @Override
    @Transactional
    public Reminder create(Long listId, String title) {
        Integer maxSortOrder = reminderRepository.findMaxSortOrderByListId(listId);
        Reminder reminder = Reminder.create(listId, title, maxSortOrder + 1);
        return reminderRepository.save(reminder);
    }

    @Override
    @Transactional
    public Reminder update(Long id, String title) {
        Reminder reminder = findById(id);
        reminder.update(title);
        return reminder;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Reminder reminder = findById(id);
        reminderRepository.delete(reminder);
    }
}


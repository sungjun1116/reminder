package sungjun.ai.reminder.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sungjun.ai.reminder.domain.ReminderList;
import sungjun.ai.reminder.repository.ReminderListRepository;
import sungjun.ai.reminder.service.ports.in.ReminderListService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultReminderListService implements ReminderListService {

    private final ReminderListRepository reminderListRepository;

    @Override
    public List<ReminderList> findAll() {
        return reminderListRepository.findAll();
    }

    @Override
    public ReminderList findById(Long id) {
        return reminderListRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ReminderList not found: " + id));
    }

    @Override
    @Transactional
    public ReminderList create(String name, String color, String icon, Long groupId) {
        int nextSortOrder = reminderListRepository.findMaxSortOrder() + 1;
        ReminderList reminderList = ReminderList.create(name, color, icon, groupId, nextSortOrder);
        return reminderListRepository.save(reminderList);
    }

    @Override
    @Transactional
    public ReminderList update(Long id, String name, String color, String icon, Long groupId) {
        ReminderList reminderList = findById(id);
        reminderList.update(name, color, icon, groupId);
        return reminderList;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ReminderList reminderList = findById(id);
        reminderListRepository.delete(reminderList);
    }
}

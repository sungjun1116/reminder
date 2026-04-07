package sungjun.ai.reminder.service.ports.in;

import sungjun.ai.reminder.domain.Reminder;

import java.util.List;

public interface ReminderService {

    List<Reminder> findByListId(Long listId);

    Reminder findById(Long id);

    Reminder create(Long listId, String title);

    Reminder update(Long id, String title);

    void delete(Long id);
}


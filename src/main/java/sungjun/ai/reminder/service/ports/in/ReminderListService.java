package sungjun.ai.reminder.service.ports.in;

import sungjun.ai.reminder.domain.ReminderList;

import java.util.List;

public interface ReminderListService {

    List<ReminderList> findAll();

    ReminderList findById(Long id);

    ReminderList create(String name, String color, String icon, Long groupId);

    ReminderList update(Long id, String name, String color, String icon, Long groupId);

    void delete(Long id);
}

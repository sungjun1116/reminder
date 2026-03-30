package sungjun.ai.reminder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sungjun.ai.reminder.domain.ReminderList;

import java.util.Optional;

public interface ReminderListRepository extends JpaRepository<ReminderList, Long> {

    @Query("SELECT COALESCE(MAX(r.sortOrder), -1) FROM ReminderList r")
    int findMaxSortOrder();
}

package sungjun.ai.reminder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sungjun.ai.reminder.domain.Reminder;

import java.util.List;
import java.util.Optional;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    List<Reminder> findByListIdOrderBySortOrderAsc(Long listId);

    @Query("SELECT COALESCE(MAX(r.sortOrder), -1) FROM Reminder r WHERE r.listId = :listId")
    Integer findMaxSortOrderByListId(Long listId);

    Optional<Reminder> findTopByListIdOrderBySortOrderDesc(Long listId);
}


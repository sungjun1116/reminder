package sungjun.ai.reminder.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sungjun.ai.reminder.domain.Reminder;
import sungjun.ai.reminder.domain.ReminderList;
import sungjun.ai.reminder.service.ports.in.ReminderListService;
import sungjun.ai.reminder.service.ports.in.ReminderService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@DisplayName("DefaultReminderService")
class DefaultReminderServiceTest {

    @Autowired
    ReminderService reminderService;

    @Autowired
    ReminderListService reminderListService;

    private Long listId;

    @BeforeEach
    void setUp() {
        ReminderList list = reminderListService.create("테스트 목록", null, null, null);
        listId = list.getId();
    }

    @Nested
    @DisplayName("create()")
    class Create {

        @Test
        @DisplayName("미리 알림을 생성하면 저장된 엔티티를 반환한다")
        void 미리_알림_생성() {
            Reminder result = reminderService.create(listId, "테스트 항목");

            assertThat(result.getId()).isNotNull();
            assertThat(result.getListId()).isEqualTo(listId);
            assertThat(result.getTitle()).isEqualTo("테스트 항목");
            assertThat(result.getCompleted()).isFalse();
            assertThat(result.getCreatedAt()).isNotNull();
        }

        @Test
        @DisplayName("첫 번째 미리 알림의 sortOrder는 0이다")
        void 첫_미리_알림_sortOrder() {
            Reminder result = reminderService.create(listId, "첫 번째 항목");

            assertThat(result.getSortOrder()).isEqualTo(0);
        }

        @Test
        @DisplayName("미리 알림을 순서대로 생성하면 sortOrder가 1씩 증가한다")
        void 순차_생성_시_sortOrder_증가() {
            Reminder first = reminderService.create(listId, "첫 번째");
            Reminder second = reminderService.create(listId, "두 번째");
            Reminder third = reminderService.create(listId, "세 번째");

            assertThat(second.getSortOrder()).isEqualTo(first.getSortOrder() + 1);
            assertThat(third.getSortOrder()).isEqualTo(second.getSortOrder() + 1);
        }
    }

    @Nested
    @DisplayName("findByListId()")
    class FindByListId {

        @Test
        @DisplayName("목록의 모든 미리 알림을 반환한다")
        void 목록별_조회() {
            reminderService.create(listId, "항목 1");
            reminderService.create(listId, "항목 2");

            List<Reminder> result = reminderService.findByListId(listId);

            assertThat(result).hasSize(2);
        }

        @Test
        @DisplayName("미리 알림이 없으면 빈 리스트를 반환한다")
        void 빈_목록_조회() {
            List<Reminder> result = reminderService.findByListId(listId);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findById()")
    class FindById {

        @Test
        @DisplayName("존재하는 id로 조회하면 해당 미리 알림을 반환한다")
        void 단건_조회() {
            Reminder saved = reminderService.create(listId, "테스트 항목");

            Reminder result = reminderService.findById(saved.getId());

            assertThat(result.getId()).isEqualTo(saved.getId());
            assertThat(result.getTitle()).isEqualTo("테스트 항목");
        }

        @Test
        @DisplayName("존재하지 않는 id로 조회하면 예외가 발생한다")
        void 존재하지_않는_id_조회() {
            assertThatThrownBy(() -> reminderService.findById(999L))
                    .isInstanceOf(EntityNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("update()")
    class Update {

        @Test
        @DisplayName("제목을 변경할 수 있다")
        void 미리_알림_수정() {
            Reminder saved = reminderService.create(listId, "원래 제목");

            Reminder result = reminderService.update(saved.getId(), "변경된 제목");

            assertThat(result.getTitle()).isEqualTo("변경된 제목");
        }

        @Test
        @DisplayName("존재하지 않는 id를 수정하면 예외가 발생한다")
        void 존재하지_않는_id_수정() {
            assertThatThrownBy(() -> reminderService.update(999L, "제목"))
                    .isInstanceOf(EntityNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("delete()")
    class Delete {

        @Test
        @DisplayName("미리 알림을 삭제하면 조회되지 않는다")
        void 미리_알림_삭제() {
            Reminder saved = reminderService.create(listId, "삭제할 항목");

            reminderService.delete(saved.getId());

            assertThatThrownBy(() -> reminderService.findById(saved.getId()))
                    .isInstanceOf(EntityNotFoundException.class);
        }

        @Test
        @DisplayName("존재하지 않는 id를 삭제하면 예외가 발생한다")
        void 존재하지_않는_id_삭제() {
            assertThatThrownBy(() -> reminderService.delete(999L))
                    .isInstanceOf(EntityNotFoundException.class);
        }
    }
}


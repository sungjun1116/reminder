package sungjun.ai.reminder.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sungjun.ai.reminder.domain.ReminderList;
import sungjun.ai.reminder.service.ports.in.ReminderListService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@DisplayName("DefaultReminderListService")
class DefaultReminderListServiceTest {

    @Autowired
    ReminderListService reminderListService;

    @Nested
    @DisplayName("create()")
    class Create {

        @Test
        @DisplayName("목록을 생성하면 저장된 엔티티를 반환한다")
        void 목록_생성() {
            ReminderList result = reminderListService.create("업무", "#007AFF", "briefcase", null);

            assertThat(result.getId()).isNotNull();
            assertThat(result.getName()).isEqualTo("업무");
            assertThat(result.getColor()).isEqualTo("#007AFF");
            assertThat(result.getIcon()).isEqualTo("briefcase");
            assertThat(result.getGroupId()).isNull();
            assertThat(result.getCreatedAt()).isNotNull();
        }

        @Test
        @DisplayName("첫 번째 목록의 sortOrder는 0이다")
        void 첫_목록_sortOrder() {
            ReminderList result = reminderListService.create("업무", null, null, null);

            assertThat(result.getSortOrder()).isEqualTo(0);
        }

        @Test
        @DisplayName("목록을 순서대로 생성하면 sortOrder가 1씩 증가한다")
        void 순차_생성_시_sortOrder_증가() {
            ReminderList first = reminderListService.create("업무", null, null, null);
            ReminderList second = reminderListService.create("개인", null, null, null);
            ReminderList third = reminderListService.create("장보기", null, null, null);

            assertThat(second.getSortOrder()).isEqualTo(first.getSortOrder() + 1);
            assertThat(third.getSortOrder()).isEqualTo(second.getSortOrder() + 1);
        }
    }

    @Nested
    @DisplayName("findAll()")
    class FindAll {

        @Test
        @DisplayName("저장된 모든 목록을 반환한다")
        void 전체_조회() {
            reminderListService.create("업무", "#007AFF", "briefcase", null);
            reminderListService.create("개인", "#34C759", "user", null);

            List<ReminderList> result = reminderListService.findAll();

            assertThat(result).hasSize(2);
        }

        @Test
        @DisplayName("저장된 목록이 없으면 빈 리스트를 반환한다")
        void 빈_목록_조회() {
            List<ReminderList> result = reminderListService.findAll();

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findById()")
    class FindById {

        @Test
        @DisplayName("존재하는 id로 조회하면 해당 목록을 반환한다")
        void 단건_조회() {
            ReminderList saved = reminderListService.create("업무", "#007AFF", "briefcase", null);

            ReminderList result = reminderListService.findById(saved.getId());

            assertThat(result.getId()).isEqualTo(saved.getId());
            assertThat(result.getName()).isEqualTo("업무");
        }

        @Test
        @DisplayName("존재하지 않는 id로 조회하면 예외가 발생한다")
        void 존재하지_않는_id_조회() {
            assertThatThrownBy(() -> reminderListService.findById(999L))
                    .isInstanceOf(EntityNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("update()")
    class Update {

        @Test
        @DisplayName("이름, 색상, 아이콘, 그룹을 변경할 수 있다")
        void 목록_수정() {
            ReminderList saved = reminderListService.create("업무", "#007AFF", "briefcase", null);

            ReminderList result = reminderListService.update(saved.getId(), "개인 업무", "#34C759", "user", null);

            assertThat(result.getName()).isEqualTo("개인 업무");
            assertThat(result.getColor()).isEqualTo("#34C759");
            assertThat(result.getIcon()).isEqualTo("user");
        }

        @Test
        @DisplayName("존재하지 않는 id를 수정하면 예외가 발생한다")
        void 존재하지_않는_id_수정() {
            assertThatThrownBy(() -> reminderListService.update(999L, "업무", null, null, null))
                    .isInstanceOf(EntityNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("delete()")
    class Delete {

        @Test
        @DisplayName("목록을 삭제하면 조회되지 않는다")
        void 목록_삭제() {
            ReminderList saved = reminderListService.create("업무", "#007AFF", "briefcase", null);

            reminderListService.delete(saved.getId());

            assertThatThrownBy(() -> reminderListService.findById(saved.getId()))
                    .isInstanceOf(EntityNotFoundException.class);
        }

        @Test
        @DisplayName("존재하지 않는 id를 삭제하면 예외가 발생한다")
        void 존재하지_않는_id_삭제() {
            assertThatThrownBy(() -> reminderListService.delete(999L))
                    .isInstanceOf(EntityNotFoundException.class);
        }
    }
}

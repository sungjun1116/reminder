package sungjun.ai.reminder.domain.list;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import sungjun.ai.reminder.domain.ReminderList;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ReminderList 엔티티")
class ReminderListTest {

    @Nested
    @DisplayName("create()")
    class Create {

        @Test
        @DisplayName("모든 필드를 전달하면 정상적으로 생성된다")
        void 모든_필드_생성() {
            ReminderList list = ReminderList.create("업무", "#007AFF", "briefcase", 1L, 0);

            assertThat(list.getName()).isEqualTo("업무");
            assertThat(list.getColor()).isEqualTo("#007AFF");
            assertThat(list.getIcon()).isEqualTo("briefcase");
            assertThat(list.getGroupId()).isEqualTo(1L);
            assertThat(list.getSortOrder()).isEqualTo(0);
        }

        @Test
        @DisplayName("color, icon, groupId 없이 생성할 수 있다")
        void 선택_필드_없이_생성() {
            ReminderList list = ReminderList.create("장보기", null, null, null, 0);

            assertThat(list.getName()).isEqualTo("장보기");
            assertThat(list.getColor()).isNull();
            assertThat(list.getIcon()).isNull();
            assertThat(list.getGroupId()).isNull();
        }

        @Test
        @DisplayName("생성 시 createdAt이 현재 시각으로 설정된다")
        void 생성_시_createdAt_설정() {
            LocalDateTime before = LocalDateTime.now();

            ReminderList list = ReminderList.create("개인", "#34C759", "user", null, 0);

            assertThat(list.getCreatedAt()).isNotNull();
            assertThat(list.getCreatedAt()).isAfterOrEqualTo(before);
            assertThat(list.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
        }
    }

    @Nested
    @DisplayName("update()")
    class Update {

        @Test
        @DisplayName("name, color, icon, groupId를 변경할 수 있다")
        void 필드_업데이트() {
            ReminderList list = ReminderList.create("업무", "#007AFF", "briefcase", null, 0);

            list.update("개인 업무", "#34C759", "user", 2L);

            assertThat(list.getName()).isEqualTo("개인 업무");
            assertThat(list.getColor()).isEqualTo("#34C759");
            assertThat(list.getIcon()).isEqualTo("user");
            assertThat(list.getGroupId()).isEqualTo(2L);
        }

        @Test
        @DisplayName("color, icon, groupId를 null로 초기화할 수 있다")
        void 선택_필드_null_업데이트() {
            ReminderList list = ReminderList.create("업무", "#007AFF", "briefcase", 1L, 0);

            list.update("업무", null, null, null);

            assertThat(list.getColor()).isNull();
            assertThat(list.getIcon()).isNull();
            assertThat(list.getGroupId()).isNull();
        }

        @Test
        @DisplayName("update() 후 createdAt은 변경되지 않는다")
        void update_후_createdAt_불변() {
            ReminderList list = ReminderList.create("업무", "#007AFF", "briefcase", null, 0);
            LocalDateTime createdAt = list.getCreatedAt();

            list.update("업무 수정", "#FF3B30", "star", null);

            assertThat(list.getCreatedAt()).isEqualTo(createdAt);
        }

        @Test
        @DisplayName("sortOrder는 update()로 변경되지 않는다")
        void sortOrder는_update_대상이_아니다() {
            ReminderList list = ReminderList.create("업무", "#007AFF", "briefcase", null, 5);

            list.update("업무 수정", "#FF3B30", "star", null);

            assertThat(list.getSortOrder()).isEqualTo(5);
        }
    }

    @Nested
    @DisplayName("updateSortOrder()")
    class UpdateSortOrder {

        @Test
        @DisplayName("sortOrder를 변경할 수 있다")
        void sortOrder_업데이트() {
            ReminderList list = ReminderList.create("업무", "#007AFF", "briefcase", null, 0);

            list.updateSortOrder(5);

            assertThat(list.getSortOrder()).isEqualTo(5);
        }

        @Test
        @DisplayName("updateSortOrder() 후 createdAt은 변경되지 않는다")
        void updateSortOrder_후_createdAt_불변() {
            ReminderList list = ReminderList.create("업무", "#007AFF", "briefcase", null, 0);
            LocalDateTime createdAt = list.getCreatedAt();

            list.updateSortOrder(10);

            assertThat(list.getCreatedAt()).isEqualTo(createdAt);
        }
    }
}

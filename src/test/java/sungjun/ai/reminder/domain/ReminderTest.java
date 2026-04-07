package sungjun.ai.reminder.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Reminder 엔티티")
class ReminderTest {

    @Nested
    @DisplayName("create()")
    class Create {

        @Test
        @DisplayName("필수 필드를 전달하면 정상적으로 생성된다")
        void 필수_필드_생성() {
            Reminder reminder = Reminder.create(1L, "테스트 항목", 0);

            assertThat(reminder.getListId()).isEqualTo(1L);
            assertThat(reminder.getTitle()).isEqualTo("테스트 항목");
            assertThat(reminder.getSortOrder()).isEqualTo(0);
        }

        @Test
        @DisplayName("생성 시 completed는 false로 초기화된다")
        void 생성_시_completed_false() {
            Reminder reminder = Reminder.create(1L, "테스트 항목", 0);

            assertThat(reminder.getCompleted()).isFalse();
        }

        @Test
        @DisplayName("생성 시 createdAt과 updatedAt이 현재 시각으로 설정된다")
        void 생성_시_시간_설정() {
            LocalDateTime before = LocalDateTime.now();

            Reminder reminder = Reminder.create(1L, "테스트 항목", 0);

            assertThat(reminder.getCreatedAt()).isNotNull();
            assertThat(reminder.getCreatedAt()).isAfterOrEqualTo(before);
            assertThat(reminder.getUpdatedAt()).isNotNull();
            assertThat(reminder.getUpdatedAt()).isAfterOrEqualTo(before);
        }
    }

    @Nested
    @DisplayName("update()")
    class Update {

        @Test
        @DisplayName("title을 변경할 수 있다")
        void title_업데이트() {
            Reminder reminder = Reminder.create(1L, "원래 제목", 0);

            reminder.update("변경된 제목");

            assertThat(reminder.getTitle()).isEqualTo("변경된 제목");
        }

        @Test
        @DisplayName("update() 후 updatedAt이 갱신된다")
        void update_후_updatedAt_갱신() {
            Reminder reminder = Reminder.create(1L, "테스트", 0);
            LocalDateTime originalUpdatedAt = reminder.getUpdatedAt();

            // 시간 차이를 만들기 위해 약간의 지연
            try { Thread.sleep(10); } catch (InterruptedException ignored) {}

            reminder.update("변경");

            assertThat(reminder.getUpdatedAt()).isAfter(originalUpdatedAt);
        }

        @Test
        @DisplayName("update() 후 createdAt은 변경되지 않는다")
        void update_후_createdAt_불변() {
            Reminder reminder = Reminder.create(1L, "테스트", 0);
            LocalDateTime createdAt = reminder.getCreatedAt();

            reminder.update("변경");

            assertThat(reminder.getCreatedAt()).isEqualTo(createdAt);
        }
    }

    @Nested
    @DisplayName("toggleComplete()")
    class ToggleComplete {

        @Test
        @DisplayName("completed가 false일 때 토글하면 true가 된다")
        void false에서_true로_토글() {
            Reminder reminder = Reminder.create(1L, "테스트", 0);

            reminder.toggleComplete();

            assertThat(reminder.getCompleted()).isTrue();
        }

        @Test
        @DisplayName("completed가 true일 때 토글하면 false가 된다")
        void true에서_false로_토글() {
            Reminder reminder = Reminder.create(1L, "테스트", 0);
            reminder.toggleComplete(); // false -> true

            reminder.toggleComplete(); // true -> false

            assertThat(reminder.getCompleted()).isFalse();
        }

        @Test
        @DisplayName("toggleComplete() 후 updatedAt이 갱신된다")
        void toggleComplete_후_updatedAt_갱신() {
            Reminder reminder = Reminder.create(1L, "테스트", 0);
            LocalDateTime originalUpdatedAt = reminder.getUpdatedAt();

            try { Thread.sleep(10); } catch (InterruptedException ignored) {}

            reminder.toggleComplete();

            assertThat(reminder.getUpdatedAt()).isAfter(originalUpdatedAt);
        }
    }

    @Nested
    @DisplayName("updateSortOrder()")
    class UpdateSortOrder {

        @Test
        @DisplayName("sortOrder를 변경할 수 있다")
        void sortOrder_업데이트() {
            Reminder reminder = Reminder.create(1L, "테스트", 0);

            reminder.updateSortOrder(5);

            assertThat(reminder.getSortOrder()).isEqualTo(5);
        }

        @Test
        @DisplayName("updateSortOrder() 후 updatedAt이 갱신된다")
        void updateSortOrder_후_updatedAt_갱신() {
            Reminder reminder = Reminder.create(1L, "테스트", 0);
            LocalDateTime originalUpdatedAt = reminder.getUpdatedAt();

            try { Thread.sleep(10); } catch (InterruptedException ignored) {}

            reminder.updateSortOrder(5);

            assertThat(reminder.getUpdatedAt()).isAfter(originalUpdatedAt);
        }
    }
}


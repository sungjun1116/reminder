package sungjun.ai.reminder.common;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import sungjun.ai.reminder.domain.Reminder;
import sungjun.ai.reminder.domain.ReminderList;
import sungjun.ai.reminder.repository.ReminderListRepository;
import sungjun.ai.reminder.repository.ReminderRepository;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ReminderListRepository reminderListRepository;
    private final ReminderRepository reminderRepository;

    @Override
    public void run(String... args) {
        if (reminderListRepository.count() > 0) {
            return; // 이미 데이터가 있으면 초기화 스킵
        }

        // 목록 생성
        ReminderList work = reminderListRepository.save(
                ReminderList.create("업무", "#007AFF", "briefcase", null, 0)
        );
        ReminderList personal = reminderListRepository.save(
                ReminderList.create("개인", "#34C759", "user", null, 1)
        );
        ReminderList shopping = reminderListRepository.save(
                ReminderList.create("장보기", "#FF9500", "shopping-cart", null, 2)
        );

        // 업무 목록 미리 알림
        reminderRepository.save(Reminder.create(work.getId(), "프로젝트 기획서 작성", 0));
        reminderRepository.save(Reminder.create(work.getId(), "팀 미팅 준비", 1));
        reminderRepository.save(Reminder.create(work.getId(), "이메일 확인", 2));

        // 개인 목록 미리 알림
        reminderRepository.save(Reminder.create(personal.getId(), "운동하기", 0));
        reminderRepository.save(Reminder.create(personal.getId(), "독서 30분", 1));

        // 장보기 목록 미리 알림
        reminderRepository.save(Reminder.create(shopping.getId(), "우유", 0));
        reminderRepository.save(Reminder.create(shopping.getId(), "계란", 1));
        reminderRepository.save(Reminder.create(shopping.getId(), "식빵", 2));
    }
}


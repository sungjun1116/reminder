package sungjun.ai.reminder.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sungjun.ai.reminder.domain.ReminderList;
import sungjun.ai.reminder.service.ports.in.ReminderListService;
import sungjun.ai.reminder.service.ports.in.ReminderService;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("ReminderController")
class ReminderControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired ReminderListService reminderListService;
    @Autowired ReminderService reminderService;

    private Long listId;
    private Long reminderId;

    @BeforeEach
    void setUp() {
        ReminderList list = reminderListService.create("테스트 목록", null, null, null);
        listId = list.getId();
        reminderId = reminderService.create(listId, "테스트 항목").getId();
    }

    @Nested
    @DisplayName("GET /api/lists/{listId}/reminders")
    class GetByListId {

        @Test
        @DisplayName("목록의 미리 알림을 배열로 반환한다")
        void 목록별_조회() throws Exception {
            mockMvc.perform(get("/api/lists/{listId}/reminders", listId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                    .andExpect(jsonPath("$[0].title").value("테스트 항목"))
                    .andExpect(jsonPath("$[0].listId").value(listId));
        }
    }

    @Nested
    @DisplayName("POST /api/lists/{listId}/reminders")
    class Create {

        @Test
        @DisplayName("유효한 요청으로 미리 알림을 생성하면 201과 생성된 항목을 반환한다")
        void 미리_알림_생성() throws Exception {
            String requestBody = """
                    {"title": "새 항목"}
                    """;

            mockMvc.perform(post("/api/lists/{listId}/reminders", listId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.title").value("새 항목"))
                    .andExpect(jsonPath("$.listId").value(listId))
                    .andExpect(jsonPath("$.completed").value(false));
        }

        @Test
        @DisplayName("title이 없으면 400을 반환한다")
        void title_없이_생성() throws Exception {
            String requestBody = """
                    {"title": ""}
                    """;

            mockMvc.perform(post("/api/lists/{listId}/reminders", listId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400));
        }
    }

    @Nested
    @DisplayName("PUT /api/reminders/{id}")
    class Update {

        @Test
        @DisplayName("유효한 요청으로 수정하면 200과 수정된 항목을 반환한다")
        void 미리_알림_수정() throws Exception {
            String requestBody = """
                    {"title": "수정된 제목"}
                    """;

            mockMvc.perform(put("/api/reminders/{id}", reminderId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(reminderId))
                    .andExpect(jsonPath("$.title").value("수정된 제목"));
        }

        @Test
        @DisplayName("존재하지 않는 id를 수정하면 404를 반환한다")
        void 존재하지_않는_id_수정() throws Exception {
            String requestBody = """
                    {"title": "제목"}
                    """;

            mockMvc.perform(put("/api/reminders/{id}", 999L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("DELETE /api/reminders/{id}")
    class Delete {

        @Test
        @DisplayName("존재하는 id를 삭제하면 204를 반환한다")
        void 미리_알림_삭제() throws Exception {
            mockMvc.perform(delete("/api/reminders/{id}", reminderId))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("존재하지 않는 id를 삭제하면 404를 반환한다")
        void 존재하지_않는_id_삭제() throws Exception {
            mockMvc.perform(delete("/api/reminders/{id}", 999L))
                    .andExpect(status().isNotFound());
        }
    }
}


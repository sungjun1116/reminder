package sungjun.ai.reminder.controller;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sungjun.ai.reminder.dto.ReminderListRequest;
import sungjun.ai.reminder.service.ports.in.ReminderListService;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("ReminderListController")
class ReminderListControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired ReminderListService reminderListService;

    private Long savedId;

    @BeforeEach
    void setUp() {
        savedId = reminderListService.create("업무", "#007AFF", "briefcase", null).getId();
    }

    @Nested
    @DisplayName("GET /api/lists")
    class GetAll {

        @Test
        @DisplayName("저장된 목록을 배열로 반환한다")
        void 전체_조회() throws Exception {
            mockMvc.perform(get("/api/lists"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                    .andExpect(jsonPath("$[0].name").value("업무"))
                    .andExpect(jsonPath("$[0].color").value("#007AFF"));
        }
    }

    @Nested
    @DisplayName("GET /api/lists/{id}")
    class GetOne {

        @Test
        @DisplayName("존재하는 id로 조회하면 해당 목록을 반환한다")
        void 단건_조회() throws Exception {
            mockMvc.perform(get("/api/lists/{id}", savedId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(savedId))
                    .andExpect(jsonPath("$.name").value("업무"))
                    .andExpect(jsonPath("$.sortOrder").isNumber())
                    .andExpect(jsonPath("$.createdAt").isNotEmpty());
        }

        @Test
        @DisplayName("존재하지 않는 id로 조회하면 404를 반환한다")
        void 존재하지_않는_id() throws Exception {
            mockMvc.perform(get("/api/lists/{id}", 999L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.message").isNotEmpty());
        }
    }

    @Nested
    @DisplayName("POST /api/lists")
    class Create {

        @Test
        @DisplayName("유효한 요청으로 목록을 생성하면 201과 생성된 목록을 반환한다")
        void 목록_생성() throws Exception {
            ReminderListRequest request = new ReminderListRequest("개인", "#34C759", "user", null);

            mockMvc.perform(post("/api/lists")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.name").value("개인"))
                    .andExpect(jsonPath("$.color").value("#34C759"))
                    .andExpect(jsonPath("$.icon").value("user"));
        }

        @Test
        @DisplayName("name이 없으면 400을 반환한다")
        void name_없이_생성() throws Exception {
            ReminderListRequest request = new ReminderListRequest("", null, null, null);

            mockMvc.perform(post("/api/lists")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400));
        }
    }

    @Nested
    @DisplayName("PUT /api/lists/{id}")
    class Update {

        @Test
        @DisplayName("유효한 요청으로 수정하면 200과 수정된 목록을 반환한다")
        void 목록_수정() throws Exception {
            ReminderListRequest request = new ReminderListRequest("개인 업무", "#FF9500", "star", null);

            mockMvc.perform(put("/api/lists/{id}", savedId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(savedId))
                    .andExpect(jsonPath("$.name").value("개인 업무"))
                    .andExpect(jsonPath("$.color").value("#FF9500"));
        }

        @Test
        @DisplayName("존재하지 않는 id를 수정하면 404를 반환한다")
        void 존재하지_않는_id_수정() throws Exception {
            ReminderListRequest request = new ReminderListRequest("업무", null, null, null);

            mockMvc.perform(put("/api/lists/{id}", 999L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("DELETE /api/lists/{id}")
    class Delete {

        @Test
        @DisplayName("존재하는 id를 삭제하면 204를 반환한다")
        void 목록_삭제() throws Exception {
            mockMvc.perform(delete("/api/lists/{id}", savedId))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("존재하지 않는 id를 삭제하면 404를 반환한다")
        void 존재하지_않는_id_삭제() throws Exception {
            mockMvc.perform(delete("/api/lists/{id}", 999L))
                    .andExpect(status().isNotFound());
        }
    }
}

package com.fitness.track.exercise;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ExerciseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsHistorySortedByDateDescendingWithDateWeightAndRepetitions() throws Exception {
        createWorkout("2024-02-01", """
                { "exercise": "Overhead Press", "weightKg": 40.0, "repetitions": 8 }
                """);
        createWorkout("2024-02-10", """
                { "exercise": "Overhead Press", "weightKg": 42.5, "repetitions": 6 }
                """);

        mockMvc.perform(get("/api/exercises/{name}/history", "Overhead Press"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].date").value("2024-02-10"))
                .andExpect(jsonPath("$[0].weightKg").value(42.5))
                .andExpect(jsonPath("$[0].repetitions").value(6))
                .andExpect(jsonPath("$[1].date").value("2024-02-01"));
    }

    @Test
    void returnsEmptyListNotNotFoundForUnknownExercise() throws Exception {
        mockMvc.perform(get("/api/exercises/{name}/history", "Nonexistent Exercise"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    private void createWorkout(String date, String setJson) throws Exception {
        String requestBody = """
                {
                  "date": "%s",
                  "sets": [%s]
                }
                """.formatted(date, setJson);

        mockMvc.perform(post("/api/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }
}

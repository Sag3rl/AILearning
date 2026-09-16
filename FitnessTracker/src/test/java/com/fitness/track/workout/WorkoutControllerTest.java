package com.fitness.track.workout;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WorkoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsWorkoutAndReturns201WithLocationAndId() throws Exception {
        String requestBody = """
                {
                  "sets": [
                    { "exercise": "Bench Press", "weightKg": 80.0, "repetitions": 5 },
                    { "exercise": "Squat", "weightKg": 100.0, "repetitions": 3 }
                  ]
                }
                """;

        mockMvc.perform(post("/api/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.sets.length()").value(2))
                .andExpect(jsonPath("$.sets[0].exercise").value("Bench Press"))
                .andExpect(jsonPath("$.sets[0].weightKg").value(80.0))
                .andExpect(jsonPath("$.sets[0].repetitions").value(5));
    }

    @Test
    void createdWorkoutCanBeRetrievedById() throws Exception {
        String requestBody = """
                {
                  "sets": [
                    { "exercise": "Deadlift", "weightKg": 120.0, "repetitions": 1 }
                  ]
                }
                """;

        String location = mockMvc.perform(post("/api/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sets[0].exercise").value("Deadlift"));
    }

    @Test
    void returns404ForUnknownWorkoutId() throws Exception {
        mockMvc.perform(get("/api/workouts/{id}", 999999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejectsNonPositiveWeightWith400() throws Exception {
        String requestBody = """
                {
                  "sets": [
                    { "exercise": "Bench Press", "weightKg": 0, "repetitions": 5 }
                  ]
                }
                """;

        mockMvc.perform(post("/api/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejectsRepetitionsBelowOneWith400() throws Exception {
        String requestBody = """
                {
                  "sets": [
                    { "exercise": "Bench Press", "weightKg": 80.0, "repetitions": 0 }
                  ]
                }
                """;

        mockMvc.perform(post("/api/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejectsEmptySetsWith400() throws Exception {
        String requestBody = """
                {
                  "sets": []
                }
                """;

        mockMvc.perform(post("/api/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejectsBlankExerciseNameWith400() throws Exception {
        String requestBody = """
                {
                  "sets": [
                    { "exercise": "", "weightKg": 80.0, "repetitions": 5 }
                  ]
                }
                """;

        mockMvc.perform(post("/api/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}

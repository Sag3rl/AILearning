package com.fitness.track.exercise;

import com.fitness.track.workout.WorkoutRepository;
import com.fitness.track.workout.WorkoutSet;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExerciseServiceTest {

    private final WorkoutRepository workoutRepository = new WorkoutRepository();
    private final ExerciseService exerciseService = new ExerciseService(workoutRepository);

    @Test
    void returnsSetsForExerciseSortedByDateDescending() {
        workoutRepository.save(LocalDate.of(2024, 1, 10), List.of(new WorkoutSet("Bench Press", 80.0, 5)));
        workoutRepository.save(LocalDate.of(2024, 1, 20), List.of(new WorkoutSet("Bench Press", 85.0, 3)));
        workoutRepository.save(LocalDate.of(2024, 1, 15), List.of(
                new WorkoutSet("Bench Press", 82.5, 4),
                new WorkoutSet("Squat", 100.0, 5)));

        List<ExerciseSetHistoryEntry> history = exerciseService.getHistory("Bench Press");

        assertEquals(3, history.size());
        assertEquals(LocalDate.of(2024, 1, 20), history.get(0).date());
        assertEquals(LocalDate.of(2024, 1, 15), history.get(1).date());
        assertEquals(LocalDate.of(2024, 1, 10), history.get(2).date());
        assertEquals(85.0, history.get(0).weightKg());
        assertEquals(3, history.get(0).repetitions());
    }

    @Test
    void matchingIsCaseInsensitive() {
        workoutRepository.save(LocalDate.of(2024, 1, 10), List.of(new WorkoutSet("Bench Press", 80.0, 5)));

        assertEquals(1, exerciseService.getHistory("bench press").size());
        assertEquals(1, exerciseService.getHistory("BENCH PRESS").size());
    }

    @Test
    void returnsEmptyListForUnknownExercise() {
        workoutRepository.save(LocalDate.of(2024, 1, 10), List.of(new WorkoutSet("Bench Press", 80.0, 5)));

        assertTrue(exerciseService.getHistory("Deadlift").isEmpty());
    }

    @Test
    void returnsEmptyListWhenNoWorkoutsExist() {
        assertTrue(exerciseService.getHistory("Bench Press").isEmpty());
    }
}

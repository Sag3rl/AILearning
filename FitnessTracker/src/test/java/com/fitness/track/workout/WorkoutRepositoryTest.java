package com.fitness.track.workout;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class WorkoutRepositoryTest {

    private final WorkoutRepository repository = new WorkoutRepository();

    @Test
    void savedWorkoutIsAssignedAUniqueIdAndCanBeFound() {
        Workout first = repository.save(List.of(new WorkoutSet("Bench Press", 80.0, 5)));
        Workout second = repository.save(List.of(new WorkoutSet("Squat", 100.0, 3)));

        assertNotNull(first.id());
        assertNotEquals(first.id(), second.id());
        assertEquals(Optional.of(first), repository.findById(first.id()));
        assertEquals(Optional.of(second), repository.findById(second.id()));
    }

    @Test
    void findByIdReturnsEmptyForUnknownId() {
        assertTrue(repository.findById(-1L).isEmpty());
    }
}

package com.fitness.track.workout;

import java.time.LocalDate;
import java.util.List;

public record Workout(Long id, LocalDate date, List<WorkoutSet> sets) {
}

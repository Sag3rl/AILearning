package com.fitness.track.workout;

import java.util.List;

public record Workout(Long id, List<WorkoutSet> sets) {
}

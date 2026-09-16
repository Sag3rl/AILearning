package com.fitness.track.exercise;

import java.time.LocalDate;

public record ExerciseSetHistoryEntry(LocalDate date, double weightKg, int repetitions) {
}

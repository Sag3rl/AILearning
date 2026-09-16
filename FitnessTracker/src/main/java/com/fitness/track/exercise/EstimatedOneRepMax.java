package com.fitness.track.exercise;

import java.time.LocalDate;

public record EstimatedOneRepMax(double estimatedOneRepMaxKg, double weightKg, int repetitions, LocalDate date) {
}

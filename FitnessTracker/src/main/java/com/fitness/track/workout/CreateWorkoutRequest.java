package com.fitness.track.workout;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.List;

public record CreateWorkoutRequest(@NotNull LocalDate date, @NotEmpty @Valid List<SetRequest> sets) {

    public record SetRequest(
            @NotBlank String exercise,
            @Positive double weightKg,
            @Min(1) int repetitions) {
    }
}

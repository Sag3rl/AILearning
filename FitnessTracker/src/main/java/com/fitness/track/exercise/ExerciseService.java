package com.fitness.track.exercise;

import com.fitness.track.workout.WorkoutRepository;
import com.fitness.track.workout.WorkoutSet;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ExerciseService {

    private final WorkoutRepository workoutRepository;

    public ExerciseService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    public List<ExerciseSetHistoryEntry> getHistory(String exerciseName) {
        return findSetsForExercise(exerciseName)
                .map(dated -> new ExerciseSetHistoryEntry(dated.date(), dated.set().weightKg(), dated.set().repetitions()))
                .sorted(Comparator.comparing(ExerciseSetHistoryEntry::date).reversed())
                .toList();
    }

    public Optional<EstimatedOneRepMax> getBestOneRepMax(String exerciseName) {
        return findSetsForExercise(exerciseName)
                .map(dated -> new EstimatedOneRepMax(
                        estimateOneRepMax(dated.set().weightKg(), dated.set().repetitions()),
                        dated.set().weightKg(),
                        dated.set().repetitions(),
                        dated.date()))
                .max(Comparator.comparingDouble(EstimatedOneRepMax::estimatedOneRepMaxKg));
    }

    private Stream<DatedSet> findSetsForExercise(String exerciseName) {
        return workoutRepository.findAll().stream()
                .flatMap(workout -> workout.sets().stream()
                        .filter(set -> set.exercise().equalsIgnoreCase(exerciseName))
                        .map(set -> new DatedSet(workout.date(), set)));
    }

    private static double estimateOneRepMax(double weightKg, int repetitions) {
        if (repetitions == 1) {
            return weightKg;
        }
        return weightKg * (1 + repetitions / 30.0);
    }

    private record DatedSet(LocalDate date, WorkoutSet set) {
    }
}

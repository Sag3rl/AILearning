package com.fitness.track.exercise;

import com.fitness.track.workout.WorkoutRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ExerciseService {

    private final WorkoutRepository workoutRepository;

    public ExerciseService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    public List<ExerciseSetHistoryEntry> getHistory(String exerciseName) {
        return workoutRepository.findAll().stream()
                .flatMap(workout -> workout.sets().stream()
                        .filter(set -> set.exercise().equalsIgnoreCase(exerciseName))
                        .map(set -> new ExerciseSetHistoryEntry(workout.date(), set.weightKg(), set.repetitions())))
                .sorted(Comparator.comparing(ExerciseSetHistoryEntry::date).reversed())
                .toList();
    }
}

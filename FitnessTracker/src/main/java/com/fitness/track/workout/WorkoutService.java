package com.fitness.track.workout;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;

    public WorkoutService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    public Workout createWorkout(CreateWorkoutRequest request) {
        List<WorkoutSet> sets = request.sets().stream()
                .map(set -> new WorkoutSet(set.exercise(), set.weightKg(), set.repetitions()))
                .toList();
        return workoutRepository.save(sets);
    }

    public Optional<Workout> getWorkout(Long id) {
        return workoutRepository.findById(id);
    }
}

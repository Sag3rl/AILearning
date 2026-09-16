package com.fitness.track.workout;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class WorkoutRepository {

    private final Map<Long, Workout> workouts = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(0);

    public Workout save(LocalDate date, List<WorkoutSet> sets) {
        long id = idSequence.incrementAndGet();
        Workout workout = new Workout(id, date, sets);
        workouts.put(id, workout);
        return workout;
    }

    public Optional<Workout> findById(Long id) {
        return Optional.ofNullable(workouts.get(id));
    }

    public Collection<Workout> findAll() {
        return workouts.values();
    }
}

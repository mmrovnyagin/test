package ru.mmsk.fastcounters.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

@Service
public class CounterService {
    private final ConcurrentHashMap<String, LongAdder> counters = new ConcurrentHashMap<>();

    public void add(String name) {
        counters.computeIfAbsent(name, k -> new LongAdder()).increment();
    }

    public Long get(String name) {
        var value = counters.get(name);
        return value == null ? null: value.sum();
    }

    public void delete(String name) {
        counters.remove(name);
    }

    public synchronized Long getAllCountersValue() {
        return counters.values().stream()
                .map(LongAdder::sum)
                .reduce(Long::sum).orElse(0L);
    }

    public synchronized List<String> getAllCountersName() {
            return new ArrayList<>(counters.keySet());
    }

    public void create(String name) {
        counters.putIfAbsent(name, new LongAdder());
    }
}

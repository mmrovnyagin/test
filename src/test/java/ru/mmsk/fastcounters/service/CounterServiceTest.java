package ru.mmsk.fastcounters.service;

import org.junit.Test;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

public class CounterServiceTest {
    CounterService counterService = new CounterService();
    ExecutorService executorService = Executors.newFixedThreadPool(20);
    final static int tries = 1000;

    @Test
    public void testCreateNewCounter() {
        final var latch = new CountDownLatch(tries);
        for (int i = 0; i < tries; i++) {
            executorService.submit(() -> {
                counterService.create("uniq");
                latch.countDown();
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(List.of("uniq"), counterService.getAllCountersName());
    }

    @Test
    public void testIncrement() {
        final var latch = new CountDownLatch(tries);
        for (int i = 0; i < tries; i++) {
            executorService.submit(() -> {
                counterService.add("uniq");
                latch.countDown();
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(tries, counterService.get("uniq").longValue());
    }

    @Test
    public void testGetAllNames() {
        final var latch = new CountDownLatch(tries);
        for (int i = 0; i < tries/2; i++ ) {
            executorService.submit(() -> {
                counterService.add(UUID.randomUUID().toString());
                latch.countDown();
            });
        }
        for (int i = 0; i < tries/2; i++ ) {
            executorService.submit(() -> {
                counterService.add("uniq");
                latch.countDown();
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(tries/2 + 1, counterService.getAllCountersName().size());
        assertEquals(tries/2, counterService.get("uniq").longValue());
    }

    @Test
    public void test_getAllNames_single() {
        counterService.add("uniq1");
        counterService.add("uniq2");
        counterService.add("uniq3");
        counterService.add("uniq4");

        assertEquals(List.of("uniq1", "uniq2", "uniq3", "uniq4"), counterService.getAllCountersName());
    }

    @Test
    public void deleteCounter() {
        counterService.add("uniq");
        counterService.add("uniq");
        counterService.add("uniq");

        assertEquals(3, counterService.get("uniq").longValue());

        counterService.delete("uniq");

        assertEquals(0, counterService.getAllCountersName().size());
    }


    @Test
    public void testGetAllCountersValue() {
        final var latch = new CountDownLatch(tries);

        final var names = List.of("name1", "name2");
        for (int i = 0; i < tries/2; i++ ) {
            executorService.submit(() -> {
                counterService.add("name1");
                latch.countDown();
            });
        }
        for (int i = 0; i < tries/2; i++ ) {
            executorService.submit(() -> {
                counterService.add("name2");
                latch.countDown();
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final var results = counterService.getAllCountersName();
        results.sort(Comparator.naturalOrder());
        assertEquals(tries, counterService.getAllCountersValue().longValue());
        assertEquals(names, results);
    }
}

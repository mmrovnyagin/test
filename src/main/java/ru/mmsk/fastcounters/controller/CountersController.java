package ru.mmsk.fastcounters.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.mmsk.fastcounters.service.CounterService;

import java.util.List;


@RestController
@RequestMapping("counters")
@RequiredArgsConstructor
public class CountersController {

    private final CounterService counterService;

    @GetMapping(path = {"/{name}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Long getByName(@PathVariable String name) {
        return counterService.get(name);
    }

    @PostMapping(path = "/{name}")
    public void createByName(@PathVariable String name) {
        counterService.create(name);
    }

    @PostMapping(path = "/{name}/inc")
    public void increment(@PathVariable String name) {
        counterService.add(name);
    }
    @DeleteMapping(path = "/{name}")
    public void deleteByName(@PathVariable String name) {
        counterService.delete(name);
    }

    @GetMapping(path = "/sumValue")
    public Long getSum() {
        return counterService.getAllCountersValue();
    }

    @GetMapping(path = "/all")
    public List<String> getNames() {
        return counterService.getAllCountersName();
    }
}

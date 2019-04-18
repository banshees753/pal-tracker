package io.pivotal.pal.tracker;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private TimeEntryRepository repo;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController (TimeEntryRepository timeEntryRepository, MeterRegistry meterRegistry){
        this.repo = timeEntryRepository;

        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable long id, @RequestBody TimeEntry entry){
        TimeEntry timeEntry = repo.update(id, entry);
        if(timeEntry != null){
            actionCounter.increment();
            return new ResponseEntity(timeEntry, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);

    }

    @PostMapping
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry timeEntry = repo.create(timeEntryToCreate);
        actionCounter.increment();
        timeEntrySummary.record(repo.list().size());

        return new ResponseEntity(timeEntry, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable long id) {
        TimeEntry timeEntry = repo.find(id);
        if(timeEntry != null){
            actionCounter.increment();
            return new ResponseEntity(timeEntry, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        actionCounter.increment();
        List<TimeEntry> timeEntries = repo.list();
        return new ResponseEntity(timeEntries, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable long id) {
        repo.delete(id);
        actionCounter.increment();
        timeEntrySummary.record(repo.list().size());

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}

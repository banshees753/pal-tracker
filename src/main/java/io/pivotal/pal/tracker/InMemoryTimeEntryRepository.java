package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private long latestTimeEntryId = 0L;
    private HashMap<Long, TimeEntry> timeEntries;

    public InMemoryTimeEntryRepository() {
        timeEntries = new HashMap<>();
    }

    public TimeEntry create(TimeEntry entry) {
        latestTimeEntryId = latestTimeEntryId + 1;
        TimeEntry timeEntry = new TimeEntry(latestTimeEntryId, entry.getProjectId(), entry.getUserId(), entry.getDate(), entry.getHours());

        timeEntries.put(latestTimeEntryId, timeEntry);

        return timeEntry;
    }

    public TimeEntry update(long id, TimeEntry entry) {

        if (timeEntries.containsKey(id)) {
            TimeEntry timeEntry = new TimeEntry(id, entry.getProjectId(), entry.getUserId(), entry.getDate(), entry.getHours());
            timeEntries.remove(id);
            timeEntries.put(latestTimeEntryId, timeEntry);
            return timeEntry;
        }
            return null;
    }

    public void delete(long id) {
        timeEntries.remove(id);
    }

    public TimeEntry find(long id) {
        if(timeEntries.containsKey(id)){
            return timeEntries.get(id);
        }
            return null;
    }

    public List<TimeEntry> list() {
        return new ArrayList<>(timeEntries.values());
    }
}

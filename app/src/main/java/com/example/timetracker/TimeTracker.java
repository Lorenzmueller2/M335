package com.example.timetracker;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class TimeTracker {
    private List<TimeEntry> timeEntries = new ArrayList<>();

    public void addTimeEntry(TimeEntry entry) {
        timeEntries.add(entry);
    }

    public List<TimeEntry> getTimeEntries() {
        return timeEntries;
    }
}

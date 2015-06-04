package com.thoughtworks.appsec.xssDemo;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class InMemoryGuestBook implements GuestBook {

    private List<GuestBookEntry> entries = new ArrayList<>();

    public void addEntry(final String contents) {
        synchronized (this) {
            entries.add(new GuestBookEntry(contents));
        }
    }

    public List<GuestBookEntry> getEntries() {
        synchronized (this) {
            return Collections.unmodifiableList(entries);
        }
    }

    public int clearEntries() {
        synchronized (this) {
            int count = entries.size();
            entries.clear();
            return count;
        }
    }
}

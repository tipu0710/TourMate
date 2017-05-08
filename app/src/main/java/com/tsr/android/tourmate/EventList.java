package com.tsr.android.tourmate;

import java.util.List;

/**
 * Created by tsult on 08-May-17.
 */

public class EventList {
    private String eventName;
    private String destination;
    private String budget;
    private DateFiender fromDateFinder;
    private DateFiender toDateFinder;

    public EventList(String eventName, String destination, String budget, DateFiender fromDateFinder, DateFiender toDateFinder) {
        this.eventName = eventName;
        this.destination = destination;
        this.budget = budget;
        this.fromDateFinder = fromDateFinder;
        this.toDateFinder = toDateFinder;
    }

    public EventList() {
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public DateFiender getFromDateFinder() {
        return fromDateFinder;
    }

    public void setFromDateFinder(DateFiender fromDateFinder) {
        this.fromDateFinder = fromDateFinder;
    }

    public DateFiender getToDateFinder() {
        return toDateFinder;
    }

    public void setToDateFinder(DateFiender toDateFinder) {
        this.toDateFinder = toDateFinder;
    }
}

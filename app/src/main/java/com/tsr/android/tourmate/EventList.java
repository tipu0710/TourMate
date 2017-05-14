package com.tsr.android.tourmate;

/**
 * Created by tsult on 08-May-17.
 */

public class EventList {
    private String eventName;
    private String destination;
    private String budget;
    private DateFinder fromDateFinder;
    private DateFinder toDateFinder;

    public EventList(String eventName, String destination, String budget, DateFinder fromDateFinder, DateFinder toDateFinder) {
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

    public DateFinder getFromDateFinder() {
        return fromDateFinder;
    }

    public void setFromDateFinder(DateFinder fromDateFinder) {
        this.fromDateFinder = fromDateFinder;
    }

    public DateFinder getToDateFinder() {
        return toDateFinder;
    }

    public void setToDateFinder(DateFinder toDateFinder) {
        this.toDateFinder = toDateFinder;
    }
}

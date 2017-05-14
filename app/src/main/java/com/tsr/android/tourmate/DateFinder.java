package com.tsr.android.tourmate;

/**
 * Created by tsult on 08-May-17.
 */

public class DateFinder {
    long year,mounth,day;

    public DateFinder(long year, long mounth, long day) {
        this.year = year;
        this.mounth = mounth;
        this.day = day;
    }

    public DateFinder() {
    }

    public long getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public long getMounth() {
        return mounth;
    }

    public void setMounth(int mounth) {
        this.mounth = mounth;
    }

    public long getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}

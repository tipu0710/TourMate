package com.tsr.android.tourmate;

/**
 * Created by tsult on 08-May-17.
 */

public class DateFiender {
    int year,mounth,day;

    public DateFiender(int year, int mounth, int day) {
        this.year = year;
        this.mounth = mounth;
        this.day = day;
    }

    public DateFiender() {
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMounth() {
        return mounth;
    }

    public void setMounth(int mounth) {
        this.mounth = mounth;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}

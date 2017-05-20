package com.tsr.android.tourmate;

/**
 * Created by tsult on 19-May-17.
 */

class AllExpense {
    private String title;
    private int cost;

    public AllExpense() {
    }

    public AllExpense(String title, int cost) {
        this.title = title;
        this.cost = cost;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

}

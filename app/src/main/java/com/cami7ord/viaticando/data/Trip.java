package com.cami7ord.viaticando.data;

import java.util.List;

public class Trip {

    private int tripId;
    private List<Integer> expensesIds;
    private double budget;
    private String startDate;
    private String endDate;
    private String destiny;
    private String description;
    private int statusId;
    private int employeeId;

    public Trip() {

    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public List<Integer> getExpensesIds() {
        return expensesIds;
    }

    public void setExpensesIds(List<Integer> expensesIds) {
        this.expensesIds = expensesIds;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDestiny() {
        return destiny;
    }

    public void setDestiny(String destiny) {
        this.destiny = destiny;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
}

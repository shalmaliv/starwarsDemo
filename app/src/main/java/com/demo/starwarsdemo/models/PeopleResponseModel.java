package com.demo.starwarsdemo.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class PeopleResponseModel {

    @JsonProperty("count")
    private int count;

    @JsonProperty("next")
    private String next;

    @JsonProperty("previous")
    private String previous;

    @JsonProperty("results")
    private ArrayList<PeopleModel> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public ArrayList<PeopleModel> getResults() {
        return results;
    }

    public void setResults(ArrayList<PeopleModel> results) {
        this.results = results;
    }
}

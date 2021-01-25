package com.example.notebook;

import java.util.Comparator;

import model.Data;
public class Timesdown implements Comparator<Data> {


    @Override
    public int compare(Data o1, Data o2) {

        return -o1.getTimes().compareTo(o2.getTimes());
    }
}
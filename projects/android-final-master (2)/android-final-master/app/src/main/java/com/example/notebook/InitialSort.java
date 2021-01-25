package com.example.notebook;


import java.util.Comparator;

import model.Data;

/**
 *
 * @author xiaanming
 *
 */
public class InitialSort implements Comparator<Data> {

    public int compare(Data o1, Data o2) {
        //这里主要是用来对ListView里面的数据根据字母表来排序
        return o1.getTitle().toUpperCase().compareTo(o2.getTitle().toUpperCase());
    }
}



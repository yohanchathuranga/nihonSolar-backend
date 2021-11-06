/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nihon.entity;

import yohan.commons.filters.DOPropertyFilter;
import java.util.ArrayList;

/**
 *
 * @author yohan
 */
public class DOCountRequest {

    boolean distinct;
    ArrayList<DOPropertyFilter> filterData = new ArrayList<>();

    public ArrayList<DOPropertyFilter> getFilterData() {
        return filterData;
    }

    public void setFilterData(ArrayList<DOPropertyFilter> filterData) {
        this.filterData = filterData;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cust.mailsender.controller;

import java.util.ArrayList;
import javax.swing.AbstractListModel;

/**
 *
 * @author Zed
 */
public class FilterListModel extends AbstractListModel {

    ArrayList items;
    ArrayList filterItems;
    private String filterText = "";
    public FilterListModel(String text) {
        super();
        filterText = text;
        items = new ArrayList();
        filterItems = new ArrayList();
    }

    public Object getElementAt(int index) {
        if (index < filterItems.size()) {
            return filterItems.get(index);
        } else {
            return null;
        }
    }

    public int getSize() {
        return filterItems.size();
    }

    public void addElement(Object o) {
        items.add(o);
        refilter();

    }

    public void refilter() {
        filterItems.clear();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).toString().indexOf(getFilterText(), 0) != -1) {
                filterItems.add(items.get(i));
            }
        }
        fireContentsChanged(this, 0, getSize());
    }
     // FilterField inner class listed below

    /**
     * @return the filterText
     */
    public String getFilterText() {
        return filterText;
    }

    /**
     * @param filterText the filterText to set
     */
    public void setFilterText(String filterText) {
        this.filterText = filterText;
    }

}

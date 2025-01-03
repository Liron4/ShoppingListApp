package com.example.test3hit.models;

public class ShoppingItem {
    private String name;
    private boolean isChecked;

    public ShoppingItem() {
        // Default constructor required for calls to DataSnapshot.getValue(ShoppingItem.class)
    }

    public ShoppingItem(String name, boolean isChecked) {
        this.name = name;
        this.isChecked = isChecked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}

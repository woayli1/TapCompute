package com.gc.tapCompute.bean;

/**
 * Description: Description
 * Author: gc
 * CreateDate: 2024/7/12 10:41
 * Version: 1.0
 */
public class DataBean {

    String name;
    String attack;
    String cost;

    public DataBean(String name, String attack, String cost) {
        this.name = name;
        this.attack = attack;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttack() {
        return attack;
    }

    public void setAttack(String attack) {
        this.attack = attack;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}

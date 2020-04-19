package com.park.api.entity;

/**
 * Created By lxr on 2020/4/17
 **/
public class TurnGroup {

    Integer id;
    Integer big_turn_id;
    String position;
    String data;
    Integer order;
    String xbbg;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBig_turn_id() {
        return big_turn_id;
    }

    public void setBig_turn_id(Integer big_turn_id) {
        this.big_turn_id = big_turn_id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getXbbg() {
        return xbbg;
    }

    public void setXbbg(String xbbg) {
        this.xbbg = xbbg;
    }
}

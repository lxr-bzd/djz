package com.park.api.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GameGroup {

    private  Integer id;
    private Integer frow;
    private Integer state;

    private  String hz;
    private  String zd;
    private  String fz;
    private  String hzqh;

    private String sb;

    private  String fz_count;

    private String trend;
}

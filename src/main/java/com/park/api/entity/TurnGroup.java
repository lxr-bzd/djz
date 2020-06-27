package com.park.api.entity;

import lombok.Data;

/**
 * Created By lxr on 2020/4/17
 **/
@Data
public class TurnGroup {

    Integer id;
    Integer big_turn_id;
    Integer group_num;
    Integer turnStart;
    Integer turnEnd;

    String xbhzA;
    Integer xbhzA_Trend;

    String xbhzB;
    Integer xbhzB_Trend;

    String xbhzC;
    Integer xbhzC_Trend;

}

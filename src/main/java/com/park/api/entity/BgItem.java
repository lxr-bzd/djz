package com.park.api.entity;

import lombok.Data;

/**
 * Created By lxr on 2020/6/21
 **/
@Data
public class BgItem {

    String name = "";

    Long[] bg = new Long[]{0l,0l};
    Long bgSum = 0l;
    Long[] Jg = new Long[]{0l,0l} ;
    Long jgSum = 0l;
    Integer JgType;
    Integer jgTrend;

}

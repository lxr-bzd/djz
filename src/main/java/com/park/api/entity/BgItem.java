package com.park.api.entity;

import com.alibaba.fastjson.JSONArray;
import com.park.api.utils.ArrayUtils;
import lombok.Data;

import java.util.List;

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


    public String toItemStr(){
        Long[] array = new Long[]{getBg()[0],getBg()[1],getBgSum(),getJgSum()};
        return JSONArray.toJSONString(array);
    }

    public static BgItem parseItem(String text){
        List<Long> list = JSONArray.parseArray(text,Long.class);
        BgItem item = new BgItem();
        item.setBg(new Long[]{list.get(0),list.get(1)});
        item.setBgSum(list.get(2));
        item.setJgSum(list.get(3));
        return item;
    }
}

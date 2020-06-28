package com.park.api.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created By lxr on 2020/6/21
 **/
@Data
public class TurnGroupResult {

    Integer turnNum;
    Long[] xbbgA ;//取大的
    Long[] xbbgB ;;//取小的
    Long[] xbbgC ;;//各取

    BigDecimal[] bgbgA ;//取大的
    BigDecimal[] bgbgB ;;//取小的
    BigDecimal[] bgbgC ;;//各取

}

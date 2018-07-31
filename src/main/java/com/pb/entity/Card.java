package com.pb.entity;

import com.pb.annotation.JsonValueMapping;
import lombok.Builder;
import lombok.Data;

/**
 *
 */
@Data
@Builder
public class Card {

    private Integer id;
    /**
     * 卡号
     */
    private String number;
    /**
     * 状态
     */
    @JsonValueMapping(value = "statusName", script = "com.pb.constants.StatusDictionary.of(arg0)")
    private Integer status;
}

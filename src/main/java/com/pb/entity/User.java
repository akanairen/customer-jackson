package com.pb.entity;

import com.pb.annotation.JsonValueMapping;
import lombok.Builder;
import lombok.Data;

/**
 * @author PengBin
 */
@Data
@Builder
public class User {

    private Integer id;

    /**
     * 用户名
     */
    private String username;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 性别
     */
    @JsonValueMapping(value = "genderName", script = "com.pb.constants.GenderDictionary.of(arg0)")
    //@JsonSerialize(using = GenderFieldSerializer.class)
    private Integer gender;

    @JsonValueMapping(value = "statusName", script = "com.pb.constants.StatusDictionary.of(arg0)")
    private Integer status;

}

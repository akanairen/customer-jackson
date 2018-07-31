## 自定义 Jackson 序列化注解，动态添加属性，转换字典值

采用 JAVA 的 JavaScript 引擎处理 JAVA 表达式

示例：

```java
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
```

输出：

```json
[
    {
        "id": 0,
        "username": "jack",
        "nickname": "杰克",
        "gender": 1,
        "genderName": "男性",
        "status": -1,
        "statusName": "失效"
    },
    {
        "id": 1,
        "username": "lucy",
        "nickname": "露西",
        "gender": 0,
        "genderName": "女性",
        "status": 0,
        "statusName": "正常"
    },
    {
        "id": 2,
        "username": "mandy",
        "nickname": "曼迪",
        "gender": 2,
        "genderName": "中性",
        "status": 0,
        "statusName": "正常"
    }
]
```


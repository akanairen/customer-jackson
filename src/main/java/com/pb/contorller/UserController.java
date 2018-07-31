package com.pb.contorller;

import com.pb.entity.Card;
import com.pb.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.pb.constants.StatusDictionary.INVALID;
import static com.pb.constants.StatusDictionary.NORMAL;

/**
 * @author PengBin
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping
    @ResponseBody
    public List<User> users() {
        List<User> userList = new ArrayList<>();
        userList.add(User.builder().id(0).username("jack").nickname("杰克").gender(1).status(INVALID.getCode()).build());
        userList.add(User.builder().id(1).username("lucy").nickname("露西").gender(0).status(NORMAL.getCode()).build());
        userList.add(User.builder().id(2).username("mandy").nickname("曼迪").gender(2).status(NORMAL.getCode()).build());

        return userList;
    }

    @GetMapping("/card")
    public List<Card> cards() {
        List<Card> cardList = new ArrayList<>();
        cardList.add(Card.builder().id(0).number("001").status(NORMAL.getCode()).build());
        cardList.add(Card.builder().id(1).number("002").status(NORMAL.getCode()).build());
        cardList.add(Card.builder().id(2).number("003").status(INVALID.getCode()).build());

        return cardList;
    }
}

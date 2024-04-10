package org.example.redissession.entity;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 验证有集合的对象
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserInfo implements Serializable {
    String userName;
    List<String> urlList;
    List<String> functionList;

    public UserInfo() {
    }
}

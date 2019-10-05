package study.tobi.spring3.chapter5.user.entity;

import lombok.*;
import study.tobi.spring3.chapter5.user.enumerate.Level;

/**
 * Created by Yoo Ju Jin(yjj@hanuritien.com)
 * Created Date : 08/09/2019
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {
    private String id;
    private String name;
    private String password;

    private Level level;
    private int   login;
    private int   recommend;
}

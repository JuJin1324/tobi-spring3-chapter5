package study.tobi.spring3.chapter5.user.dao;

import study.tobi.spring3.chapter5.user.entity.User;

import java.util.List;

/**
 * Created by Yoo Ju Jin(yjj@hanuritien.com)
 * Created Date : 2019-10-04
 */
public interface UserDao {
    int add(User user);

    User get(String id);

    List<User> getAll();

    int deleteAll();

    int getCount();

    int update(User user1);
}

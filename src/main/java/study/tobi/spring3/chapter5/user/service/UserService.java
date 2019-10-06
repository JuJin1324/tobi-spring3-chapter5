package study.tobi.spring3.chapter5.user.service;

import lombok.NoArgsConstructor;
import lombok.Setter;
import study.tobi.spring3.chapter5.user.dao.UserDao;
import study.tobi.spring3.chapter5.user.entity.User;
import study.tobi.spring3.chapter5.user.enumerate.Level;

import java.util.List;

/**
 * Created by Yoo Ju Jin(yjj@hanuritien.com)
 * Created Date : 2019-10-05
 */

@Setter
@NoArgsConstructor
public class UserService {

    private UserDao userDao;

    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            if (canUpdateLevel(user)) {
                upgradeLevel(user);
            }
        }
    }

    private boolean canUpdateLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC: return (user.getLogin() >= 50);
            case SILVER: return (user.getRecommend() >= 30);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level : " + currentLevel);
        }
    }

    private void upgradeLevel(User user) {
        if (user.getLevel() == Level.BASIC) user.setLevel(Level.SILVER);
        else if (user.getLevel() == Level.SILVER) user.setLevel(Level.GOLD);
        userDao.update(user);
    }

    public void add(User user) {
        if (user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }
}

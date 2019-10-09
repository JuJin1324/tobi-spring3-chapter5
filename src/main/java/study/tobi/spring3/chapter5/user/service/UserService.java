package study.tobi.spring3.chapter5.user.service;

import lombok.NoArgsConstructor;
import lombok.Setter;
import study.tobi.spring3.chapter5.user.dao.UserDao;
import study.tobi.spring3.chapter5.user.entity.User;
import study.tobi.spring3.chapter5.user.enumerate.Level;
import study.tobi.spring3.chapter5.user.policy.UserLevelUpgradePolicy;

import java.util.List;

/**
 * Created by Yoo Ju Jin(yjj@hanuritien.com)
 * Created Date : 2019-10-05
 */

@Setter
@NoArgsConstructor
public class UserService {

    private UserDao                userDao;
    private UserLevelUpgradePolicy levelUpgradePolicy;

    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            if (levelUpgradePolicy.canUpgradeLevel(user)) {
                levelUpgradePolicy.upgradeLevel(user);
            }
        }
    }

    public void add(User user) {
        if (user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }
}

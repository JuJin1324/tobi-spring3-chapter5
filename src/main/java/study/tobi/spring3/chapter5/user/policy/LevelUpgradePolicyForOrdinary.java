package study.tobi.spring3.chapter5.user.policy;

import lombok.NoArgsConstructor;
import lombok.Setter;
import study.tobi.spring3.chapter5.user.dao.UserDao;
import study.tobi.spring3.chapter5.user.entity.User;
import study.tobi.spring3.chapter5.user.enumerate.Level;

/**
 * Created by Yoo Ju Jin(yjj@hanuritien.com)
 * Created Date : 2019-10-07
 */

@Setter
@NoArgsConstructor
public class LevelUpgradePolicyForOrdinary implements UserLevelUpgradePolicy {

    private UserDao userDao;

    @Override
    public boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC:
                return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER:
                return (user.getRecommend() >= MIN_RECOMMAND_FOR_GOLD);
            case GOLD:
                return false;
            default:
                throw new IllegalArgumentException("Unknown Level : " + currentLevel);
        }
    }

    @Override
    public void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }
}

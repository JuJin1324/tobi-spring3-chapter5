package study.tobi.spring3.chapter5.user.policy;

import study.tobi.spring3.chapter5.user.entity.User;

/**
 * Created by Yoo Ju Jin(yjj@hanuritien.com)
 * Created Date : 2019-10-07
 */

public interface UserLevelUpgradePolicy {

    int MIN_LOGCOUNT_FOR_SILVER = 50;
    int MIN_RECOMMAND_FOR_GOLD  = 30;

    boolean canUpgradeLevel(User user);

    void upgradeLevel(User user);
}

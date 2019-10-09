package study.tobi.spring3.chapter5.user.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import study.tobi.spring3.chapter5.user.dao.UserDao;
import study.tobi.spring3.chapter5.user.entity.User;
import study.tobi.spring3.chapter5.user.enumerate.Level;
import study.tobi.spring3.chapter5.user.policy.UserLevelUpgradePolicy;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static study.tobi.spring3.chapter5.user.policy.UserLevelUpgradePolicy.MIN_LOGCOUNT_FOR_SILVER;
import static study.tobi.spring3.chapter5.user.policy.UserLevelUpgradePolicy.MIN_RECOMMAND_FOR_GOLD;

/**
 * Created by Yoo Ju Jin(yjj@hanuritien.com)
 * Created Date : 2019-10-05
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {

    @Autowired
    UserDao                userDao;
    @Autowired
    UserService            userService;
    @Autowired
    UserLevelUpgradePolicy userLevelUpgradePolicy;

    List<User> users;
    private String test1;

    @Before
    public void setUp() {
        users = Arrays.asList(
                new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
                new User("erwins", "신승한", "p3", Level.SILVER, 60, MIN_RECOMMAND_FOR_GOLD - 1),
                new User("madnite1", "이상호", "p4", Level.SILVER, 60, MIN_RECOMMAND_FOR_GOLD),
                new User("green", "오민큐 ", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }


    @Test
    public void upgradeLevels() {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if (upgraded) {
            assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
        } else {
            assertThat(userUpdate.getLevel(), is(user.getLevel()));
        }
    }

    @Test
    public void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4);      // Level : GOLD
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);        // Level : null

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);      // Level should be BASIC

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
        assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
    }
}
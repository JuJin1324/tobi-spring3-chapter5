package study.tobi.spring3.chapter5.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import study.tobi.spring3.chapter5.user.dao.UserDao;
import study.tobi.spring3.chapter5.user.dao.UserDaoJdbc;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.internal.matchers.IsCollectionContaining.hasItem;
import static org.junit.matchers.JUnitMatchers.either;

/**
 * Created by Yoo Ju Jin(yjj@hanuritien.com)
 * Created Date : 2019-09-23
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/junit.xml")
public class JUnitTest {
    @Autowired
    ApplicationContext context;

    @Autowired
    UserDao autowiredDao;

    static Set<JUnitTest>     testObjects = new HashSet<>();
    static ApplicationContext contextObject;

    static UserDao daoObject;

    @Test
    public void verifyCreateJUnitTestObjectPerMethod_andCreateContextObjectOnlyOne1() {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);

        assertThat(contextObject == null || contextObject == this.context, is(true));
        contextObject = this.context;
    }

    @Test
    public void verifyCreateJUnitTestObjectPerMethod_andCreateContextObjectOnlyOne2() {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);

        assertTrue(contextObject == null || contextObject == this.context);
        contextObject = this.context;
    }

    @Test
    public void verifyCreateJUnitTestObjectPerMethod_andCreateContextObjectOnlyOne3() {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);

        assertThat(contextObject, either(is(nullValue())).or(is(this.context)));
        contextObject = this.context;
    }

    @Test
    public void compareAutowiredToGetBean() {

        UserDao getBeanDao = context.getBean("userDao", UserDaoJdbc.class);
        assertThat(getBeanDao, is(autowiredDao));
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void confirmUndefinedBean() {

        UserDao dao = context.getBean("undefined", UserDaoJdbc.class);
    }

    @Test
    public void verifyOnlyOneDaoObjectCreated1() {
        assertThat(daoObject, either(is(nullValue())).or(is(autowiredDao)));
        daoObject = autowiredDao;
    }

    @Test
    public void verifyOnlyOneDaoObjectCreated2() {
        assertThat(daoObject, either(is(nullValue())).or(is(autowiredDao)));
        daoObject = autowiredDao;
    }
}

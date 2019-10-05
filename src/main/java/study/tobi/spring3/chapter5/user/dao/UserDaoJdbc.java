package study.tobi.spring3.chapter5.user.dao;

import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import study.tobi.spring3.chapter5.user.entity.User;
import study.tobi.spring3.chapter5.user.enumerate.Level;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Yoo Ju Jin(yjj@hanuritien.com)
 * Created Date : 08/09/2019
 */

@NoArgsConstructor
public class UserDaoJdbc implements UserDao {

    private JdbcTemplate    jdbcTemplate;
    private RowMapper<User> userRowMapper;

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        userRowMapper = new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                /*
                 * 이미 queryForObject 템플릿 내부에서 resultSet을 한번 next() 호출 후에
                 * RowMapper 콜백 메서드를 호출하기 때문에 따로 resultSet.next() 메서드를 호출하지 않고
                 * 바로 사용한다.
                 */
                User user = new User();
                user.setId(resultSet.getString("id"));
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
                user.setLevel(Level.valueOf(resultSet.getInt("level")));
                user.setLogin(resultSet.getInt("login"));
                user.setRecommend(resultSet.getInt("recommend"));

                return user;
            }
        };
    }

    @Override
    public User get(String id) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?",
                new Object[]{id},
                userRowMapper);
    }

    /* 클라이언트 : 전략 인터페이스인 StatementStrategy의 구현체를 컨텍스트로 주입 */
    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM users");
    }

    @Override
    public void add(final User user) {
        jdbcTemplate.update("INSERT INTO users(id, name, password, level, login, recommend) VALUES (?, ?, ?, ?, ?, ?)",
                user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForInt("SELECT count(*) FROM users");
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update("UPDATE users SET name = ?, password = ?, level = ?, login = ?, recommend = ? WHERE id = ?",
                user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getId());
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users ORDER BY id",
                userRowMapper);
    }
}

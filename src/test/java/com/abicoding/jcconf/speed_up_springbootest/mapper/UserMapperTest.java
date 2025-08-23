package com.abicoding.jcconf.speed_up_springbootest.mapper;

import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.UserDbDto;
import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void insert_and_selectById() {
        UserDbDto insertUser = given_UserDbDto();
        userMapper.insert(insertUser);

        UserDbDto foundUser = userMapper.selectById(insertUser.getId());
        assertThat(foundUser)
                .isNotNull()
                .isEqualTo(insertUser);
    }

    private UserDbDto given_UserDbDto() {
        long now = Instant.now().toEpochMilli();
        UserDbDto insertUser = new UserDbDto();
        insertUser.setUsername("xidada");
        insertUser.setCreatedAt(now);
        insertUser.setUpdatedAt(now);
        return insertUser;
    }
}
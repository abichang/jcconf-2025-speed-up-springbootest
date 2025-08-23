package com.abicoding.jcconf.speed_up_springbootest.repository;

import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.UserDbDto;
import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.UserMapper;
import com.abicoding.jcconf.speed_up_springbootest.adapter.repository.UserRepositoryImpl;
import com.abicoding.jcconf.speed_up_springbootest.entity.User;
import com.abicoding.jcconf.speed_up_springbootest.service.UserNotFoundException;
import com.abicoding.jcconf.speed_up_springbootest.service.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class UserRepositoryImplTest {

    private final UserMapper userMapper = mock(UserMapper.class);
    private final UserRepository userRepository = new UserRepositoryImpl(userMapper);

    @Test
    void getById_all_ok() {
        UserDbDto userDbDto = given_user_in_db();

        User actualUser = userRepository.getById(userDbDto.getId());

        assertThat(actualUser.getId()).isEqualTo(userDbDto.getId());
        assertThat(actualUser.getUsername()).isEqualTo(userDbDto.getUsername());
        assertThat(actualUser.getCreatedAt()).isEqualTo(Instant.ofEpochMilli(userDbDto.getCreatedAt()));
        assertThat(actualUser.getUpdatedAt()).isEqualTo(Instant.ofEpochMilli(userDbDto.getUpdatedAt()));
    }

    private UserDbDto given_user_in_db() {
        UserDbDto user = new UserDbDto();
        user.setId(1L);
        user.setUsername("testuser");
        user.setCreatedAt(1640995200000L);
        user.setUpdatedAt(1640995200000L);
        Mockito.doReturn(user).when(userMapper).selectById(user.getId());
        return user;
    }

    @Test
    void getById_user_not_existed() {
        Long notFoundUserId = 404L;

        Mockito.doReturn(null)
                .when(userMapper)
                .selectById(notFoundUserId);

        assertThatThrownBy(() -> userRepository.getById(notFoundUserId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("userId=" + notFoundUserId);
    }

}
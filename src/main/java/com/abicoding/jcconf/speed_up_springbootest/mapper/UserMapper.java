package com.abicoding.jcconf.speed_up_springbootest.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("""
            SELECT id, username, created_at, updated_at 
            FROM users 
            WHERE id = #{id}
            """)
    UserDbDto selectById(Long id);

    @Insert("""
            INSERT INTO users (username, created_at, updated_at) 
            VALUES (#{username}, #{createdAt}, #{updatedAt})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(UserDbDto user);
}
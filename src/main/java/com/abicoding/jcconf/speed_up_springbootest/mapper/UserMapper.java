package com.abicoding.jcconf.speed_up_springbootest.mapper;

import com.abicoding.jcconf.speed_up_springbootest.dto.UserDbDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    
    @Select("""
            SELECT id, username, created_at, updated_at 
            FROM user 
            WHERE id = #{id}
            """)
    UserDbDto selectById(Long id);
    
    @Insert("""
            INSERT INTO user (username, created_at, updated_at) 
            VALUES (#{username}, #{createdAt}, #{updatedAt})
            """)
    void insert(UserDbDto user);
}
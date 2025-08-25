package com.abicoding.jcconf.speed_up_springbootest.adapter.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface WalletMapper {

    @Select("""
            SELECT user_id, gold, version, created_at, updated_at 
            FROM wallet 
            WHERE user_id = #{userId}
            """)
    WalletDbDto selectByUserId(Long userId);

    @Insert("""
            INSERT INTO wallet (user_id, gold, version, created_at, updated_at) 
            VALUES (#{userId}, #{gold}, #{version}, #{createdAt}, #{updatedAt})
            """)
    void insert(WalletDbDto wallet);

    @Update("""
            UPDATE wallet 
            SET gold = gold + #{amount}, updated_at = #{updatedAt} 
            WHERE user_id = #{userId}
            """)
    void addGold(Long userId, Long amount, Long updatedAt);

    @Update("""
            UPDATE wallet 
            SET gold = #{gold}, version = version + 1, updated_at = #{updatedAt} 
            WHERE user_id = #{userId} AND version = #{version}
            """)
    int update(WalletDbDto wallet);
}
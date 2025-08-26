package com.abicoding.jcconf.speed_up_springbootest.common;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface GetAllTableNamesMapper {
    @Select("SHOW TABLES")
    String[] getAllTableNames();
}

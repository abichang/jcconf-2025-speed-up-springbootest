package com.abicoding.jcconf.speed_up_springbootest.common;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TruncateTableMapper {

    @Update(
            "TRUNCATE TABLE ${tableName}"
    )
    void truncateTable(String tableName);
}

package com.ihxlife.qyhgateway.mapper;

import com.ihxlife.qyhgateway.dto.AccesstokenInfo;
import com.ihxlife.qyhgateway.dto.AccesstokenInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AccesstokenInfoMapper {
    int countByExample(AccesstokenInfoExample example);

    int deleteByExample(AccesstokenInfoExample example);

    int deleteByPrimaryKey(String id);

    int insert(AccesstokenInfo record);

    int insertSelective(AccesstokenInfo record);

    List<AccesstokenInfo> selectByExample(AccesstokenInfoExample example);

    AccesstokenInfo selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AccesstokenInfo record, @Param("example") AccesstokenInfoExample example);

    int updateByExample(@Param("record") AccesstokenInfo record, @Param("example") AccesstokenInfoExample example);

    int updateByPrimaryKeySelective(AccesstokenInfo record);

    int updateByPrimaryKey(AccesstokenInfo record);
}
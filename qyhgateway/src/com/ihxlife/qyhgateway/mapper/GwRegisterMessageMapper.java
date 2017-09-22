package com.ihxlife.qyhgateway.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ihxlife.qyhgateway.dto.GwRegisterMessage;
import com.ihxlife.qyhgateway.dto.GwRegisterMessageExample;

public interface GwRegisterMessageMapper {
    int countByExample(GwRegisterMessageExample example);

    int deleteByExample(GwRegisterMessageExample example);

    int deleteByPrimaryKey(String seqno);

    int insert(GwRegisterMessage record);

    int insertSelective(GwRegisterMessage record);

    List<GwRegisterMessage> selectByExample(GwRegisterMessageExample example);

    GwRegisterMessage selectByPrimaryKey(String seqno);

    int updateByExampleSelective(@Param("record") GwRegisterMessage record, @Param("example") GwRegisterMessageExample example);

    int updateByExample(@Param("record") GwRegisterMessage record, @Param("example") GwRegisterMessageExample example);

    int updateByPrimaryKeySelective(GwRegisterMessage record);

    int updateByPrimaryKey(GwRegisterMessage record);
}
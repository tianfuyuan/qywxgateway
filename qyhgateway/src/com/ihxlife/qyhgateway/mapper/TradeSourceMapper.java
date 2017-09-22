package com.ihxlife.qyhgateway.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ihxlife.qyhgateway.dto.TradeSource;
import com.ihxlife.qyhgateway.dto.TradeSourceExample;

/**
 * 交易来源mapper
 * @author Administrator
 *
 */
public interface TradeSourceMapper {
	/**
	 * 根据条件查询交易来源信息条数
	 * @param example
	 * @return
	 */
    int countByExample(TradeSourceExample example);

    /**
     * 根据条件删除交易来源信息
     * @param example
     * @return
     */
    int deleteByExample(TradeSourceExample example);

    /**
     * 根据主键删除交易来源信息
     * @param seqno
     * @return
     */
    int deleteByPrimaryKey(String seqno);

    /**
     * 新增交易来源信息
     * @param record
     * @return
     */
    int insert(TradeSource record);

    /**
     * 新增交易来源信息对象中不为空的字段值
     * @param record
     * @return
     */
    int insertSelective(TradeSource record);

    /**
     * 根据条件查询交易来源信息
     * @param example
     * @return
     */
    List<TradeSource> selectByExample(TradeSourceExample example);

    /**
     * 根据主键查询交易来源信息
     * @param seqno
     * @return
     */
    TradeSource selectByPrimaryKey(String seqno);

    /**
     * 根据条件修改交易来源信息对象中不为空的字段值
     * @param record
     * @param example
     * @return
     */
    int updateByExampleSelective(@Param("record") TradeSource record, @Param("example") TradeSourceExample example);

    /**
     * 根据条件修改交易来源信息
     * @param record
     * @param example
     * @return
     */
    int updateByExample(@Param("record") TradeSource record, @Param("example") TradeSourceExample example);

    /**
     * 根据主键修改交易来源信息对象中不为空的字段值
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(TradeSource record);

    /**
     * 根据主键修改交易来源信息
     * @param record
     * @return
     */
    int updateByPrimaryKey(TradeSource record);
}
package com.ihxlife.qyhgateway.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ihxlife.qyhgateway.dto.InterfaceConfig;
import com.ihxlife.qyhgateway.dto.InterfaceConfigExample;

/**
 * 接口配置信息mapper
 * @author Administrator
 *
 */
public interface InterfaceConfigMapper {
	/**
	 * 根据条件查询接口配置信息条数
	 * @param example
	 * @return
	 */
    int countByExample(InterfaceConfigExample example);

    /**
     * 根据条件删除接口配置信息
     * @param example
     * @return
     */
    int deleteByExample(InterfaceConfigExample example);

    /**
     * 根据主键删除接口配置信息
     * @param id
     * @return
     */
    int deleteByPrimaryKey(String id);

    /**
     * 新增接口配置信息
     * @param record
     * @return
     */
    int insert(InterfaceConfig record);

    /**
     * 新增接口配置信息对象中不为空的字段值
     * @param record
     * @return
     */
    int insertSelective(InterfaceConfig record);

    /**
     * 根据条件查询接口配置信息
     * @param example
     * @return
     */
    List<InterfaceConfig> selectByExample(InterfaceConfigExample example);

    /**
     * 根据主键获取接口配置信息
     * @param id
     * @return
     */
    InterfaceConfig selectByPrimaryKey(String id);
    
    /**
     * 根据条件修改接口配置信息对象中不为空的字段值
     * @param record
     * @param example
     * @return
     */
    int updateByExampleSelective(@Param("record") InterfaceConfig record, @Param("example") InterfaceConfigExample example);

    /**
     * 根据条件修改接口配置信息
     * @param record
     * @param example
     * @return
     */
    int updateByExample(@Param("record") InterfaceConfig record, @Param("example") InterfaceConfigExample example);

    /**
     * 根据主键修改接口配置信息对象中不为空的字段值
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(InterfaceConfig record);

    /**
     * 根据主键修改接口配置信息
     * @param record
     * @return
     */
    int updateByPrimaryKey(InterfaceConfig record);
}
package com.ihxlife.qyhgateway.dto;

import java.util.Date;

/**
 * 接口配置信息实体类
 * @author Administrator
 *
 */
public class InterfaceConfig extends BaseClass{
	/**
	 * 主键id
	 */
    private String id;

    /**
     * 跳转url
     */
    private String indexurl;

    /**
     * 重定向地址
     */
    private String redirecturl;

    /**
     * access_token的ID
     */
    private String tokenId;

    /**
     * 详情
     */
    private String detail;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 更新时间
     */
    private Date updatetime;

    /**
     * 备用字段1
     */
    private String spare1;

    /**
     * 备用字段2
     */
    private String spare2;

    /**
     * 备用字段3
     */
    private String spare3;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIndexurl() {
        return indexurl;
    }

    public void setIndexurl(String indexurl) {
        this.indexurl = indexurl;
    }

    public String getRedirecturl() {
        return redirecturl;
    }

    public void setRedirecturl(String redirecturl) {
        this.redirecturl = redirecturl;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getSpare1() {
        return spare1;
    }

    public void setSpare1(String spare1) {
        this.spare1 = spare1;
    }

    public String getSpare2() {
        return spare2;
    }

    public void setSpare2(String spare2) {
        this.spare2 = spare2;
    }

    public String getSpare3() {
        return spare3;
    }

    public void setSpare3(String spare3) {
        this.spare3 = spare3;
    }
}
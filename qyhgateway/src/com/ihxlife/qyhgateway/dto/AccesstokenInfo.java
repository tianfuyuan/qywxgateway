package com.ihxlife.qyhgateway.dto;

import java.util.Date;

public class AccesstokenInfo {
	
	/**
	 * 主键id
	 */
    private String id;

    /**
     * 企业号corpid
     */
    private String corpid;

    /**
     * 企业号secret
     */
    private String secret;

    /**
     * 详情
     */
    private String detail;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 修改时间
     */
    private Date updatetime;

    /**
     * 腾讯生成的accessToken
     */
    private String accessToken;

    /**
     * accessToken 有效时间
     */
    private String tokenExpiresIn;

    /**
     * 腾讯生成的ticket
     */
    private String ticket;

    /**
     * ticket有效时间
     */
    private String ticketExpiresIn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorpid() {
        return corpid;
    }

    public void setCorpid(String corpid) {
        this.corpid = corpid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenExpiresIn() {
        return tokenExpiresIn;
    }

    public void setTokenExpiresIn(String tokenExpiresIn) {
        this.tokenExpiresIn = tokenExpiresIn;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getTicketExpiresIn() {
        return ticketExpiresIn;
    }

    public void setTicketExpiresIn(String ticketExpiresIn) {
        this.ticketExpiresIn = ticketExpiresIn;
    }
}
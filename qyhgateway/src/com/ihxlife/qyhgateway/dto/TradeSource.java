package com.ihxlife.qyhgateway.dto;

import java.util.Date;


/**
 * 交易来源信息表
 * @author Administrator
 *
 */
public class TradeSource extends BaseClass{
	/**
	 * 主键
	 */
    private String seqno;

    /**
     * 交易来源 （唯一）
     */
    private String tradeSource;

    /**
     * 生成的32位秘钥
     */
    private String checkKey;

    /**
     * 
     */
    private String remark;

    /**
     * 录入时间
     */
    private Date inputdate;

    /**
     * 修改时间
     */
    private Date updatedate;

    /**
     * 备用字段2
     */
    private String spare2;

    /**
     * 备用字段3
     */
    private String spare3;

    public String getSeqno() {
        return seqno;
    }

    public void setSeqno(String seqno) {
        this.seqno = seqno;
    }

    public String getTradeSource() {
        return tradeSource;
    }

    public void setTradeSource(String tradeSource) {
        this.tradeSource = tradeSource;
    }

    public String getCheckKey() {
        return checkKey;
    }

    public void setCheckKey(String checkKey) {
        this.checkKey = checkKey;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getInputdate() {
        return inputdate;
    }

    public void setInputdate(Date inputdate) {
        this.inputdate = inputdate;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
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
package com.ihxlife.qyhgateway.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TradeSourceExample{
	
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TradeSourceExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andSeqnoIsNull() {
            addCriterion("seqno is null");
            return (Criteria) this;
        }

        public Criteria andSeqnoIsNotNull() {
            addCriterion("seqno is not null");
            return (Criteria) this;
        }

        public Criteria andSeqnoEqualTo(String value) {
            addCriterion("seqno =", value, "seqno");
            return (Criteria) this;
        }

        public Criteria andSeqnoNotEqualTo(String value) {
            addCriterion("seqno <>", value, "seqno");
            return (Criteria) this;
        }

        public Criteria andSeqnoGreaterThan(String value) {
            addCriterion("seqno >", value, "seqno");
            return (Criteria) this;
        }

        public Criteria andSeqnoGreaterThanOrEqualTo(String value) {
            addCriterion("seqno >=", value, "seqno");
            return (Criteria) this;
        }

        public Criteria andSeqnoLessThan(String value) {
            addCriterion("seqno <", value, "seqno");
            return (Criteria) this;
        }

        public Criteria andSeqnoLessThanOrEqualTo(String value) {
            addCriterion("seqno <=", value, "seqno");
            return (Criteria) this;
        }

        public Criteria andSeqnoLike(String value) {
            addCriterion("seqno like", value, "seqno");
            return (Criteria) this;
        }

        public Criteria andSeqnoNotLike(String value) {
            addCriterion("seqno not like", value, "seqno");
            return (Criteria) this;
        }

        public Criteria andSeqnoIn(List<String> values) {
            addCriterion("seqno in", values, "seqno");
            return (Criteria) this;
        }

        public Criteria andSeqnoNotIn(List<String> values) {
            addCriterion("seqno not in", values, "seqno");
            return (Criteria) this;
        }

        public Criteria andSeqnoBetween(String value1, String value2) {
            addCriterion("seqno between", value1, value2, "seqno");
            return (Criteria) this;
        }

        public Criteria andSeqnoNotBetween(String value1, String value2) {
            addCriterion("seqno not between", value1, value2, "seqno");
            return (Criteria) this;
        }

        public Criteria andTradeSourceIsNull() {
            addCriterion("trade_source is null");
            return (Criteria) this;
        }

        public Criteria andTradeSourceIsNotNull() {
            addCriterion("trade_source is not null");
            return (Criteria) this;
        }

        public Criteria andTradeSourceEqualTo(String value) {
            addCriterion("trade_source =", value, "tradeSource");
            return (Criteria) this;
        }

        public Criteria andTradeSourceNotEqualTo(String value) {
            addCriterion("trade_source <>", value, "tradeSource");
            return (Criteria) this;
        }

        public Criteria andTradeSourceGreaterThan(String value) {
            addCriterion("trade_source >", value, "tradeSource");
            return (Criteria) this;
        }

        public Criteria andTradeSourceGreaterThanOrEqualTo(String value) {
            addCriterion("trade_source >=", value, "tradeSource");
            return (Criteria) this;
        }

        public Criteria andTradeSourceLessThan(String value) {
            addCriterion("trade_source <", value, "tradeSource");
            return (Criteria) this;
        }

        public Criteria andTradeSourceLessThanOrEqualTo(String value) {
            addCriterion("trade_source <=", value, "tradeSource");
            return (Criteria) this;
        }

        public Criteria andTradeSourceLike(String value) {
            addCriterion("trade_source like", value, "tradeSource");
            return (Criteria) this;
        }

        public Criteria andTradeSourceNotLike(String value) {
            addCriterion("trade_source not like", value, "tradeSource");
            return (Criteria) this;
        }

        public Criteria andTradeSourceIn(List<String> values) {
            addCriterion("trade_source in", values, "tradeSource");
            return (Criteria) this;
        }

        public Criteria andTradeSourceNotIn(List<String> values) {
            addCriterion("trade_source not in", values, "tradeSource");
            return (Criteria) this;
        }

        public Criteria andTradeSourceBetween(String value1, String value2) {
            addCriterion("trade_source between", value1, value2, "tradeSource");
            return (Criteria) this;
        }

        public Criteria andTradeSourceNotBetween(String value1, String value2) {
            addCriterion("trade_source not between", value1, value2, "tradeSource");
            return (Criteria) this;
        }

        public Criteria andCheckKeyIsNull() {
            addCriterion("check_key is null");
            return (Criteria) this;
        }

        public Criteria andCheckKeyIsNotNull() {
            addCriterion("check_key is not null");
            return (Criteria) this;
        }

        public Criteria andCheckKeyEqualTo(String value) {
            addCriterion("check_key =", value, "checkKey");
            return (Criteria) this;
        }

        public Criteria andCheckKeyNotEqualTo(String value) {
            addCriterion("check_key <>", value, "checkKey");
            return (Criteria) this;
        }

        public Criteria andCheckKeyGreaterThan(String value) {
            addCriterion("check_key >", value, "checkKey");
            return (Criteria) this;
        }

        public Criteria andCheckKeyGreaterThanOrEqualTo(String value) {
            addCriterion("check_key >=", value, "checkKey");
            return (Criteria) this;
        }

        public Criteria andCheckKeyLessThan(String value) {
            addCriterion("check_key <", value, "checkKey");
            return (Criteria) this;
        }

        public Criteria andCheckKeyLessThanOrEqualTo(String value) {
            addCriterion("check_key <=", value, "checkKey");
            return (Criteria) this;
        }

        public Criteria andCheckKeyLike(String value) {
            addCriterion("check_key like", value, "checkKey");
            return (Criteria) this;
        }

        public Criteria andCheckKeyNotLike(String value) {
            addCriterion("check_key not like", value, "checkKey");
            return (Criteria) this;
        }

        public Criteria andCheckKeyIn(List<String> values) {
            addCriterion("check_key in", values, "checkKey");
            return (Criteria) this;
        }

        public Criteria andCheckKeyNotIn(List<String> values) {
            addCriterion("check_key not in", values, "checkKey");
            return (Criteria) this;
        }

        public Criteria andCheckKeyBetween(String value1, String value2) {
            addCriterion("check_key between", value1, value2, "checkKey");
            return (Criteria) this;
        }

        public Criteria andCheckKeyNotBetween(String value1, String value2) {
            addCriterion("check_key not between", value1, value2, "checkKey");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNull() {
            addCriterion("remark is null");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("remark is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("remark =", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("remark <>", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("remark >", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("remark >=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("remark <", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("remark <=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("remark like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("remark not like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("remark in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("remark not in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("remark between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("remark not between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andInputdateIsNull() {
            addCriterion("inputdate is null");
            return (Criteria) this;
        }

        public Criteria andInputdateIsNotNull() {
            addCriterion("inputdate is not null");
            return (Criteria) this;
        }

        public Criteria andInputdateEqualTo(Date value) {
            addCriterion("inputdate =", value, "inputdate");
            return (Criteria) this;
        }

        public Criteria andInputdateNotEqualTo(Date value) {
            addCriterion("inputdate <>", value, "inputdate");
            return (Criteria) this;
        }

        public Criteria andInputdateGreaterThan(Date value) {
            addCriterion("inputdate >", value, "inputdate");
            return (Criteria) this;
        }

        public Criteria andInputdateGreaterThanOrEqualTo(Date value) {
            addCriterion("inputdate >=", value, "inputdate");
            return (Criteria) this;
        }

        public Criteria andInputdateLessThan(Date value) {
            addCriterion("inputdate <", value, "inputdate");
            return (Criteria) this;
        }

        public Criteria andInputdateLessThanOrEqualTo(Date value) {
            addCriterion("inputdate <=", value, "inputdate");
            return (Criteria) this;
        }

        public Criteria andInputdateIn(List<Date> values) {
            addCriterion("inputdate in", values, "inputdate");
            return (Criteria) this;
        }

        public Criteria andInputdateNotIn(List<Date> values) {
            addCriterion("inputdate not in", values, "inputdate");
            return (Criteria) this;
        }

        public Criteria andInputdateBetween(Date value1, Date value2) {
            addCriterion("inputdate between", value1, value2, "inputdate");
            return (Criteria) this;
        }

        public Criteria andInputdateNotBetween(Date value1, Date value2) {
            addCriterion("inputdate not between", value1, value2, "inputdate");
            return (Criteria) this;
        }

        public Criteria andUpdatedateIsNull() {
            addCriterion("updatedate is null");
            return (Criteria) this;
        }

        public Criteria andUpdatedateIsNotNull() {
            addCriterion("updatedate is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatedateEqualTo(Date value) {
            addCriterion("updatedate =", value, "updatedate");
            return (Criteria) this;
        }

        public Criteria andUpdatedateNotEqualTo(Date value) {
            addCriterion("updatedate <>", value, "updatedate");
            return (Criteria) this;
        }

        public Criteria andUpdatedateGreaterThan(Date value) {
            addCriterion("updatedate >", value, "updatedate");
            return (Criteria) this;
        }

        public Criteria andUpdatedateGreaterThanOrEqualTo(Date value) {
            addCriterion("updatedate >=", value, "updatedate");
            return (Criteria) this;
        }

        public Criteria andUpdatedateLessThan(Date value) {
            addCriterion("updatedate <", value, "updatedate");
            return (Criteria) this;
        }

        public Criteria andUpdatedateLessThanOrEqualTo(Date value) {
            addCriterion("updatedate <=", value, "updatedate");
            return (Criteria) this;
        }

        public Criteria andUpdatedateIn(List<Date> values) {
            addCriterion("updatedate in", values, "updatedate");
            return (Criteria) this;
        }

        public Criteria andUpdatedateNotIn(List<Date> values) {
            addCriterion("updatedate not in", values, "updatedate");
            return (Criteria) this;
        }

        public Criteria andUpdatedateBetween(Date value1, Date value2) {
            addCriterion("updatedate between", value1, value2, "updatedate");
            return (Criteria) this;
        }

        public Criteria andUpdatedateNotBetween(Date value1, Date value2) {
            addCriterion("updatedate not between", value1, value2, "updatedate");
            return (Criteria) this;
        }

        public Criteria andSpare2IsNull() {
            addCriterion("spare2 is null");
            return (Criteria) this;
        }

        public Criteria andSpare2IsNotNull() {
            addCriterion("spare2 is not null");
            return (Criteria) this;
        }

        public Criteria andSpare2EqualTo(String value) {
            addCriterion("spare2 =", value, "spare2");
            return (Criteria) this;
        }

        public Criteria andSpare2NotEqualTo(String value) {
            addCriterion("spare2 <>", value, "spare2");
            return (Criteria) this;
        }

        public Criteria andSpare2GreaterThan(String value) {
            addCriterion("spare2 >", value, "spare2");
            return (Criteria) this;
        }

        public Criteria andSpare2GreaterThanOrEqualTo(String value) {
            addCriterion("spare2 >=", value, "spare2");
            return (Criteria) this;
        }

        public Criteria andSpare2LessThan(String value) {
            addCriterion("spare2 <", value, "spare2");
            return (Criteria) this;
        }

        public Criteria andSpare2LessThanOrEqualTo(String value) {
            addCriterion("spare2 <=", value, "spare2");
            return (Criteria) this;
        }

        public Criteria andSpare2Like(String value) {
            addCriterion("spare2 like", value, "spare2");
            return (Criteria) this;
        }

        public Criteria andSpare2NotLike(String value) {
            addCriterion("spare2 not like", value, "spare2");
            return (Criteria) this;
        }

        public Criteria andSpare2In(List<String> values) {
            addCriterion("spare2 in", values, "spare2");
            return (Criteria) this;
        }

        public Criteria andSpare2NotIn(List<String> values) {
            addCriterion("spare2 not in", values, "spare2");
            return (Criteria) this;
        }

        public Criteria andSpare2Between(String value1, String value2) {
            addCriterion("spare2 between", value1, value2, "spare2");
            return (Criteria) this;
        }

        public Criteria andSpare2NotBetween(String value1, String value2) {
            addCriterion("spare2 not between", value1, value2, "spare2");
            return (Criteria) this;
        }

        public Criteria andSpare3IsNull() {
            addCriterion("spare3 is null");
            return (Criteria) this;
        }

        public Criteria andSpare3IsNotNull() {
            addCriterion("spare3 is not null");
            return (Criteria) this;
        }

        public Criteria andSpare3EqualTo(String value) {
            addCriterion("spare3 =", value, "spare3");
            return (Criteria) this;
        }

        public Criteria andSpare3NotEqualTo(String value) {
            addCriterion("spare3 <>", value, "spare3");
            return (Criteria) this;
        }

        public Criteria andSpare3GreaterThan(String value) {
            addCriterion("spare3 >", value, "spare3");
            return (Criteria) this;
        }

        public Criteria andSpare3GreaterThanOrEqualTo(String value) {
            addCriterion("spare3 >=", value, "spare3");
            return (Criteria) this;
        }

        public Criteria andSpare3LessThan(String value) {
            addCriterion("spare3 <", value, "spare3");
            return (Criteria) this;
        }

        public Criteria andSpare3LessThanOrEqualTo(String value) {
            addCriterion("spare3 <=", value, "spare3");
            return (Criteria) this;
        }

        public Criteria andSpare3Like(String value) {
            addCriterion("spare3 like", value, "spare3");
            return (Criteria) this;
        }

        public Criteria andSpare3NotLike(String value) {
            addCriterion("spare3 not like", value, "spare3");
            return (Criteria) this;
        }

        public Criteria andSpare3In(List<String> values) {
            addCriterion("spare3 in", values, "spare3");
            return (Criteria) this;
        }

        public Criteria andSpare3NotIn(List<String> values) {
            addCriterion("spare3 not in", values, "spare3");
            return (Criteria) this;
        }

        public Criteria andSpare3Between(String value1, String value2) {
            addCriterion("spare3 between", value1, value2, "spare3");
            return (Criteria) this;
        }

        public Criteria andSpare3NotBetween(String value1, String value2) {
            addCriterion("spare3 not between", value1, value2, "spare3");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}
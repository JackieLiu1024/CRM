package com.xxxx.crm.query;

import com.xxxx.crm.base.BaseQuery;

/*** 营销机会管理多条件查询条件 */
public class SaleChanceQuery extends BaseQuery {
    private String customerName;//客户名称
    private String createMan;//创建人
    private String state;//分配状态

    private Integer devResult; // 开发状态
    private Integer assignMan;// 分配人

    public SaleChanceQuery() {
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getDevResult() {
        return devResult;
    }

    public void setDevResult(Integer devResult) {
        this.devResult = devResult;
    }

    public Integer getAssignMan() {
        return assignMan;
    }

    public void setAssignMan(Integer assignMan) {
        this.assignMan = assignMan;
    }


}

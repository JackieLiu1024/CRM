package com.xxxx.crm.query;

import com.xxxx.crm.base.BaseQuery;

public class RoleQuery extends BaseQuery {
    private String roleName;

    public RoleQuery() {
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}

<!DOCTYPE html>
<html> <head>
    <#include "../common.ftl">
</head> <body class="childrenBody"> <form class="layui-form" style="width:80%;">
    <#--隐藏域,角色id-->
    <input name="id" type="hidden" value="${(role.id)!}"/>
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">⻆⾊名</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input userName"
                   lay-verify="required" name="roleName" id="roleName"
                    <#-- value--⻆⾊名 -->
                   value="${(role.roleName)!}" placeholder="⻆⾊名">
        </div>
    </div>
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">⻆⾊备注</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input userName"
                   lay-verify="required" name="roleRemark" id="roleRemark"
                   <#-- value--⻆⾊备注 -->
                   value="${(role.roleRemark)!}" placeholder="请输⼊⻆⾊备注">
        </div>
    </div>
    <br/>
    <div class="layui-form-item layui-row layui-col-xs12">
        <div class="layui-input-block">
            <button class="layui-btn layui-btn-lg" lay-submit="" lay-filter="addOrUpdateRole">确认</button>
            <button class="layui-btn layui-btn-lg layui-btn-normal">取消</button>
        </div>
    </div>
</form> <script type="text/javascript" src="${ctx}/js/role/add_update.js">
</script>
</body>
</html>
layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    //⻆⾊列表展示
    var tableIns = table.render({
        elem: '#roleList',
        url : ctx+'/role/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "roleListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: "id", title:'编号',fixed:"true", width:80},
            {field: 'roleName', title: '⻆⾊名', minWidth:50, align:"center"},
            {field: 'roleRemark', title: '⻆⾊备注', minWidth:100,
                align:'center'},
            {field: 'createDate', title: '创建时间',
                align:'center',minWidth:150},
            {field: 'updateDate', title: '更新时间',
                align:'center',minWidth:150},
            {title: '操作', minWidth:150,
                templet:'#roleListBar',fixed:"right",align:"center"}
        ]]
    });

    // 多条件搜索
    $(".search_btn").on("click",function(){
        table.reload("roleListTable",{
            page: {
                curr: 1 //重新从第 1 ⻚开始
            },
            where: {
                roleName: $("input[name='roleName']").val()
            }
        })
    });

    //授权页面
    function openAddGrantDailog(datas) {
        if(datas.length==0){
            layer.msg("请选择待授权⻆⾊记录!", {icon: 5});
            return;
        }
        if(datas.length>1){
            layer.msg("暂不⽀持批量⻆⾊授权!", {icon: 5});
            return;
        }
        var url = ctx+"/role/toAddGrantPage?roleId="+datas[0].id;
        var title="⻆⾊管理-⻆⾊授权";
        layui.layer.open({
            title : title,
            type : 2,
            area:["600px","280px"],
            maxmin:true,
            content : url
        });
    }

    //头⼯具栏事件---列表
    table.on('toolbar(roles)', function(obj){
        var checkStatus = table.checkStatus(obj.config.id);
        switch(obj.event){
            case "add":
                openAddOrUpdateRoleDialog();
                break;
            case "grant":
                openAddGrantDailog(checkStatus.data);
                break;
        };
    });

    //打开 添加|更新 iframe
    function openAddOrUpdateRoleDialog(uid) {
        var url = ctx+"/role/addOrUpdateRolePage";
        var title="⻆⾊管理-⻆⾊添加";
        if(uid){
            url = url+"?id="+uid;
            title="⻆⾊管理-⻆⾊更新";
        }
        layui.layer.open({
            title : title,
            type : 2,
            area:["600px","280px"],
            maxmin:true,
            content : url
        });
    }


    /**
     * ⾏监听
     */
    table.on("tool(roles)", function(obj){
        var layEvent = obj.event;
        if(layEvent === "edit") {
            openAddOrUpdateRoleDialog(obj.data.id);
        }else if(layEvent === "del") {
            layer.confirm('确定删除当前⻆⾊？', {icon: 3, title: "⻆⾊管理"}, function(index) {
                $.post(ctx+"/role/delete",{id:obj.data.id},function (data) {
                    if(data.code==200){
                        layer.msg("操作成功！");
                        tableIns.reload();
                    }else{
                        layer.msg(data.msg, {icon: 5});
                    }
                });
            })
        }
    });
});
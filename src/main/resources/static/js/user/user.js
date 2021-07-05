layui.use(['form','jquery','jquery_cookie','table'], function () {
    var form = layui.form,
        table = layui.table,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    //渲染列表
    var tableIns = table.render({
        elem:'#userList',
        url:ctx+'/user/list',
        cellMinWidth:95,
        page:true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "userListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: "id", title:'编号',fixed:"true", width:80},
            {field: 'userName', title: '用户名', minWidth:50, align:"center"},
            {field: 'email', title: '用户邮箱', minWidth:100, align:'center'},
            {field: 'phone', title: '用户电话', minWidth:100, align:'center'},
            {field: 'trueName', title: '真实姓名', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center',minWidth:150},
            {field: 'updateDate', title: '更新时间', align:'center',minWidth:150},
            {title: '操作', minWidth:150, templet:'#userListBar',fixed:"right",align:"center"}
        ]]
    });

    //搜索按钮绑定事件
    $(".search_btn").click(function(){
        tableIns.reload({
            //条件查询
            where:{
                userName:$("input[name='userName']").val(),
                email:$("input[name='email']").val(),
                phone:$("input[name='phone']").val()
            },
            page: {
                //重新从第 1 ⻚开始
                curr:1
            }
        });
    });

    //头部工具栏--添加用户和--(批量)删除
    table.on("toolbar(users)",function (obj){
        //从表格选中的记录获选中后被执行的数据
       var checkStatus = table.checkStatus(obj.config.id);
       console.log(checkStatus);//控制台打印输出checkStatus
       if(obj.event === 'add'){
           openAddOrUpdateUserDialog();
       }
       if(obj.event === 'del'){
           deleteUser(checkStatus.data);
       }
    });

    //行内工具栏--编辑(用户修改)--删除
    table.on("tool(users)",function (obj){


        var layEvent = obj.event;
        // 监听编辑事件
        if(layEvent === "edit"){
            openAddOrUpdateUserDialog(obj.data.id);
            // var checkStatus=table.checkStatus(obj.config.id);//..........报错,证明checkStatus()不是行内方法|行内没有复选框
            // console.log(checkStatus);//..........
            console.log(obj.data);
        }else if(layEvent === "del"){
            //监听删除事件
            layer.confirm('确定删除当前⽤户？', {icon: 3, title: "⽤户管理"}, function(index) {
                $.post(ctx + "/user/delete", {ids:obj.data.id}, function (data) {
                    if(data.code==200){
                        layer.msg("操作成功！");
                        tableIns.reload();
                    }else{
                        layer.msg(data.msg, {icon: 5});
                    }
                });
            });
        }
    });





    //打开添加|编辑用户对话框
    function openAddOrUpdateUserDialog(userId){
        var url = ctx+"/user/addOrUpdateUserPage";
        var title = "<h3>⽤户管理-⽤户添加</h3>";
        if(userId){
            url = url+"?id="+userId;
            title = "<h3>⽤户管理-⽤户更新</h3>";
        }
        layer.open({
            title : title,
            type: 2,
            area:["650px","400px"],
            maxmin:true,
            content : url
        });
    }

    //批量删除用户
    function deleteUser(datas){
        if(datas.length==0){
           layer.msg("请选择删除记录!",{icon:5});
            return;
        }
        //声明数组存储数据
        var ids=[];
        //x为datas的下标
        for(var x in datas){
            //获取对应下标中的id属性值
            ids.push(datas[x].id);
        }
        layer.confirm("确定删除选中的⽤户记录？",{
            btn:['确认','取消']
        },function(index){
            layer.close(index);
            //发送ajax
            $.ajax({
                type:"post",
                url:ctx+"/user/delete",
                data:{"ids":ids.toString()},
                dataType:"json",
                success:function(obj){
                    if(obj.code==200){
                        layer.msg("删除成功!");
                        tableIns.reload();
                    }else{
                        //删除失败
                        layer.msg(obj.msg,{icon: 5});
                    }
                }
            });
        });
    }

});
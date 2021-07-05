layui.use(['form','jquery','jquery_cookie','table'], function () {
    var form = layui.form,
        layer = layui.layer,
        table=layui.table,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    /**
     * 触发表单
     */
    //监听submit事件 实现营销机会的添加与更新
    form.on("submit(addOrUpdateSaleChance)",function(obj){
        var dataField = obj.field;
        console.log(obj.field+"<<<<<<<");
        /**
         * 发送ajax
         */
        // 提交数据时的加载层 （https://layer.layui.com/）
        var index = layer.msg("数据提交中,请稍后...",{
                icon:16, // 图标
                time:false, // 不关闭
                shade:0.8 // 设置遮罩的透明度
            });
        var url = ctx+"/sale_chance/save";
        //获取隐藏域信息，决定添加或者是修改
        if($("input[name=id]").val()){
            url=ctx+"/sale_chance/update";
        }

        $.ajax({
            type:"post",
            url:url,
            data:dataField,
            dataType:"json",
            success:function(data){
                if(data.code==200){
                    //添加成功
                    layer.msg("添加成功了",{icon:6});
                    //关闭加载层
                    layer.close(index);
                    //关闭ifream
                    layer.closeAll("iframe");
                    //刷新父目录页面
                    parent.location.reload();
                }else{
                    //添加失败
                    layer.msg(data.msg,{icon:5});
                }
            }
        });
        //阻止表单提交
        return false;
    });


    /**
     * 取消按钮触发
     */
    $("#closeBtn").click(function(){
        //假设这是iframe页
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    });

    //分配人 下拉框
    $.post(ctx+"/user/queryAllSales",function(data){
        // 如果是修改操作，判断当前修改记录的指派人的值       input[name=man]     隐藏域中
        var assignMan = $("input[name='man']").val();
        //遍历
        for(var x in data){
            // 当前修改记录的指派人的值 与 循环到的值 相等，下拉框则选中????????????????????????????????????
            if(assignMan==data[x].id){
                $("#assignMan").append(" <option selected value='"+data[x].id+"'>"+data[x].uname+"</option>");
            }else{
                $("#assignMan").append(" <option value='"+data[x].id+"'>"+data[x].uname+"</option>");
            }
        }
        // 重新渲染下拉框内容
        layui.form.render("select");
    });
});
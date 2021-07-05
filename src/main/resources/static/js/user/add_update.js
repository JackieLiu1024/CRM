layui.use(['form', 'layer','formSelects'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        // 引入 formSelects 模块
        formSelects = layui.formSelects;
    //添加|更新用户
    form.on("submit(addOrUpdateUser)",function(data){
        // 弹出loading层
        var index = top.layer.msg("数据提交中，请稍候",{icon:16,time:500,shade:0.8});
        var url = ctx+"/user/save";
        if($("input[name='id']").val()){
            url = ctx+"/user/update";
        }
        $.post(url,data.field,function(result){
            if(result.code == 200){
                setTimeout(function (){
                    //关闭弹出层
                    top.layer.close(index);
                    top.layer.msg("操作成功！");
                    //关闭所有iframe层
                    layer.closeAll("iframe");
                    // 刷新⽗⻚⾯
                    parent.location.reload();
                },500);//timeout:延时加载setTimeout函数
            }else{
                layer.msg(result.msg,{icon:5});
            }
        });
        return false;
    });

    //取消按钮,关闭弹出层
    $("#closeBtn").click(function(){
        //先得到当前iframe层的索引
        var index = parent.layer.getFrameIndex(window.name);
        //再执行关闭
        parent.layer.close(index);
    });


    //加载下拉框数据
    var userId=$("input[name='id']").val();
    formSelects.config('selectId',{
        type:"post",
        //负责后台查询下拉框数据的url
        searchUrl:ctx+"/role/queryAllRoles?userId="+userId,
        //⾃定义返回数据中name的key, 默认 name
        keyName: 'roleName',
        //⾃定义返回数据中value的key, 默认 value
        keyVal: 'id'
    },true);


});
layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    form.on("submit(saveBtn)",function(obj){
        var dataField = obj.field;
        //发送ajax修改
        $.ajax({
            type:"post",
            url:ctx+"/user/update",
            data:{
                userName:dataField.userName,
                phone:dataField.phone,
                email:dataField.email,
                trueName:dataField.trueName,
                id:dataField.id
            },
            dataType:"json",
            success:function(data){
                if(data.code==200){
                    //成功
                    layer.msg("保存成功!",function(){
                        window.parent.location.href=ctx+"/index";
                    });
                }else{
                    //失败
                    layer.msg(data.msg);
                }
            }
        });
        //阻止表单提交
        return false;

    });
});



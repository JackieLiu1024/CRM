layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);


    /**
     * 触发表单
     */
    form.on("submit(saveBtn)",function (obj){
        var dataField=obj.field;

        /**
         * 发送ajax
         */

        $.ajax({
            type:"post",
            url:ctx+"/user/updatePassword",
            data:{
                oldPassWord:dataField.old_password,
                newPassWord:dataField.new_password,
                confirmPassword:dataField.again_password
            },
            dataType:"json",
            success:function(data){
                if(data.code == 200){
                    layer.msg("修改成功了，三秒后重新登录...",function(){
                        //清空cookie
                        // 退出系统后，删除对应的cookie
                        $.removeCookie("userIdStr", {domain:"localhost",path:"/crm"});
                        $.removeCookie("userName", {domain:"localhost",path:"/crm"});
                        $.removeCookie("trueName", {domain:"localhost",path:"/crm"});
                        //跳转登录页面
                        window.parent.location.href=ctx+"/index"
                    });
                }else{
                    //修改失败了
                    layer.msg(data.msg);
                }
            }
        });
    });
});
layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    //lay-filter="addOrUpdateRole">确认按钮
    form.on("submit(addOrUpdateRole)", function (data) {
        var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: 500,shade: 0.8});
        //弹出loading
        var url=ctx + "/role/save";
        if($("input[name='id']").val()){
            url=ctx + "/role/update";
        }
        $.post(url, data.field, function (res) {
            if (res.code == 200) {
                setTimeout(function () {
                    top.layer.close(index);
                    top.layer.msg("操作成功！");
                    layer.closeAll("iframe");
                    //刷新⽗⻚⾯
                    parent.location.reload();
                }, 500);
            } else {
                layer.msg(res.msg, {icon: 5});
            }
        });
        return false;
    });
});

var zTreeObj; $(function () {
    loadModuleInfo();
});
function loadModuleInfo() {
    $.ajax({
        type:"post",
        url:ctx+"/module/queryAllModules",
        data:{
            roleId:$("#roleId").val()
        },
        dataType:"json",
        success:function (data) {
            // zTree 的参数配置，深⼊使⽤请参考 API ⽂档（setting 配置详解）
            var setting = {
                data: {
                    //接收数据格式simpleData:
                    simpleData: {
                        enable: true
                    }
                },
                view:{
                    showLine: false
                    // showIcon: false
                },
                check: {
                    enable: true,
                    chkboxType: { "Y": "ps", "N": "ps" }
                },
                callback: {
                    onCheck: zTreeOnCheck
                }
            };
            //将查询到的数据作为zTree的节点(zNodes)
            var zNodes =data;
            //根据 div的id值,zTree 的参数配置,节点数据 -----初始化zTreeObj
            zTreeObj=$.fn.zTree.init($("#test1"), setting, zNodes);
        }
    })
}

//当点击ztree的复选框，触发的函数
function zTreeOnCheck(event, treeId, treeNode) {
    //获取到选中的节点
    var nodes= zTreeObj.getCheckedNodes(true);
    //根据隐藏域获取值
    var roleId=$("#roleId").val();
    //拼接数据
    var mids="mids=";
    for(var i=0;i<nodes.length;i++){
        if(i<nodes.length-1){
            mids=mids+nodes[i].id+"&mids=";
        }else{
            mids=mids+nodes[i].id;
        }
    }
    //发送ajax
    $.ajax({
        type:"post",
        url:ctx+"/role/addGrant",
        data:mids+"&roleId="+roleId,
        dataType:"json",
        success:function (data) {
            console.log(data);
            alert(data.msg);
        }
    })
}
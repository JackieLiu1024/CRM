layui.use(['table','layer'],function() {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 客户开发数据列表
     */
    var  tableIns = table.render({
        elem: '#saleChanceList',
        url : ctx+'/sale_chance/list?state=1&flag=1',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "saleChanceListTable",
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'chanceSource', title: '机会来源',align:"center"},
            {field: 'customerName', title: '客户名称',  align:'center'},
            {field: 'cgjl', title: '成功几率', align:'center'},
            {field: 'overview', title: '概要', align:'center'},
            {field: 'linkMan', title: '联系人',  align:'center'},
            {field: 'linkPhone', title: '联系电话', align:'center'},
            {field: 'description', title: '描述', align:'center'},
            {field: 'createMan', title: '创建人', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center'},
            {field: 'devResult', title: '开发状态', align:'center',templet:function (d) {
                    return formatterDevResult(d.devResult);
                }},
            {title: '操作',fixed:"right",align:"center", minWidth:150,templet:"#op"}
        ]]
    });

    //格式化开发状态
    function formatterDevResult(value){
        /**
         * 0-未开发
         * 1-开发中
         * 2-开发成功
         * 3-开发失败
         */
        if(value==0){
            return "<div style='color: yellow'>未开发</div>";
        }else if(value==1){
            return "<div style='color: #00FF00;'>开发中</div>";
        }else if(value==2){
            return "<div style='color: #00B83F'>开发成功</div>";
        }else if(value==3){
            return "<div style='color: red'>开发失败</div>";
        }else {
            return "<div style='color: #af0000'>未知</div>"
        }
    }

    /**
     * 搜索功能开发
     */
    $(".search_btn").click(function(){
        //从新加载条件查询
        tableIns.reload({
            where: { //设定异步数据接口的额外参数，任意设
                customerName: $("input[name='customerName']").val(),  // 客户名
                createMan: $("input[name='createMan']").val(),  // 创建人
                devResult: $("#devResult").val()  // 开发状态
            }
            ,page: {
                curr: 1 // 重新从第 1 页开始
            }
        }); // 只重载数据
    });

    /**
     * 触发行内工具栏
     */
    table.on("tool(saleChances)",function(obj){
        console.log(obj);
        if(obj.event==='info'){
            openCusDevPlanDialog("行销机会维护",obj.data.id);
            return ;
        }else if(obj.event == 'dev'){
            openCusDevPlanDialog("行销机会开发",obj.data.id);
            return ;
        }
    });

    function openCusDevPlanDialog(title,sid){

        layer.open({
            title:title,
            type: 2,
            area:["750px","550px"],
            maxmin: true,
            content:ctx + "/cus_dev_plan/toCusDevPlanDataPage?sid=" + sid
        });

    }
});
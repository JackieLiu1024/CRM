package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.mapper.SaleChanceMapper;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {

    @Autowired(required = false)
    private SaleChanceMapper saleChanceMapper;

    /**
     * 条件查询功能
     * @param query
     * @return
     */
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery query){

        System.out.println(saleChanceMapper.selectByParams(query));
        //实例化map
        Map<String,Object> map = new HashMap<>();
        //初始化分页
        PageHelper.startPage(query.getPage(),query.getLimit());
        //查询
        List<SaleChance> list = saleChanceMapper.selectByParams(query);

        //pageInfo
        PageInfo<SaleChance> pageInfo = new PageInfo<SaleChance>(list);
        map.put("code",0);
        map.put("msg","success");
        //查找到的总记录数
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**头部工具栏
     * 营销机会数据添加
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveSaleChance(SaleChance saleChance){
        // 1.参数校验
        checkParams(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());
        // 2.设置相关参数默认值
        //未分配
        if(StringUtils.isBlank(saleChance.getAssignMan())){
            //分配状态  未分配
            saleChance.setState(0);//0--未分配，1--已经分配
            //开发状态 未开发
            saleChance.setDevResult(0);//0--未开发，1--开发中，2--成功，3--失败
            //分配时间
            saleChance.setAssignTime(null);
        }
        //已经分配
        if(StringUtils.isNotBlank(saleChance.getAssignMan())){
            //分配状态  已经分配
            saleChance.setState(1);
            //开发状态  开发中
            saleChance.setDevResult(1);
            //分配时间
            saleChance.setAssignTime(new Date());
        }
        //创建事件--系统当前时间
        saleChance.setCreateDate(new Date());
        //更新事件--系统当前时间
        saleChance.setUpdateDate(new Date());
        // 3.执行添加 判断结果
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance)<1,"营销机会数据添加失败！");
    }


    /**行内工具栏
     * 营销机会数据更新
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void changeSaleChance(SaleChance saleChance){
        // 1.参数校验 // 通过id查询记录
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        // 判断是否为空
        AssertUtil.isTrue(null==temp,"待更新记录不存在！");
        // 校验基础参数
        checkParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //原数据未分配,现在分配
        if(StringUtils.isBlank(temp.getAssignMan())&&StringUtils.isNotBlank(saleChance.getAssignMan())){
            //分配状态
            saleChance.setState(1);
            //开发状态
            saleChance.setDevResult(1);
            //分配时间
            saleChance.setAssignTime(new Date());
            //原数据已分配,现在不分配
        }else if(StringUtils.isNotBlank(temp.getAssignMan())&& StringUtils.isBlank(saleChance.getAssignMan())){
            //分配状态
            saleChance.setState(0);
            //开发状态
            saleChance.setDevResult(0);
            //修改 分配时间
            saleChance.setAssignTime(null);
            //分配人
            saleChance.setAssignMan("");
        }
        //修改数据更新事时间
        saleChance.setUpdateDate(new Date());
        //判断是否修改成功
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)<1,"更新失败");
    }


    /**
     * 基本参数校验
     * @param customerName
     * @param linkMan
     * @param linkPhone
     */
    private void checkParams(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"请输入客户名！");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"请输入联系人！");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"请输入手机号！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"手机号格式不正确！");

    }

    /**
     * 批量删除
     * @param array
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeSaleChance(Integer[] array) {
        AssertUtil.isTrue(array == null || array.length==0,"请选择要删除的数据");
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(array)<1,"批量删除失败了");
    }
}

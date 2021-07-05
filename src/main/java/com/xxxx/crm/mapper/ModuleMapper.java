package com.xxxx.crm.mapper;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.dto.TreeDto;
import com.xxxx.crm.vo.Module;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module,Integer> {

    //查询所有的模块
    List<TreeDto> queryAllModules();
}
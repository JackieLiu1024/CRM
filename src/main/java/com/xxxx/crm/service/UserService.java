package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.mapper.UserMapper;
import com.xxxx.crm.mapper.UserRoleMapper;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.query.UserQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.Md5Util;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.utils.UserIDBase64;
import com.xxxx.crm.vo.User;
import com.xxxx.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService extends BaseService<User,Integer> {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private UserRoleMapper userRoleMapper;

    //用户登录
    public UserModel doLogin(String userName, String userPwd ){
        //1)验证用户名，密码是否为空
        checkUser(userName,userPwd);
        //2）用户是否存在
        User temp = userMapper.selectUserByName(userName);
        AssertUtil.isTrue(null==temp,"用户不存在或者用户已经注销");
        //3）用户密码是否正确
        checkUserPwd(temp.getUserPwd(),userPwd);
        //构建返回对象
        return buildInfo(temp);
    }

    //修改密码
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserPassWord(Integer userId, String oldPassWord, String newPassWord, String confirmPassword){
        // 通过userId获取⽤户对象
        User user = userMapper.selectByPrimaryKey(userId);
        // 1. 参数校验
        checkPasswordParams(user, oldPassWord, newPassWord, confirmPassword);
        // 2. 设置⽤户新密码
        user.setUserPwd(Md5Util.encode(newPassWord));
        // 3. 执⾏更新操作
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"⽤户密码更新失败！");
    }

    //修改密码 参数校验
    private void checkPasswordParams(User user, String oldPassWord, String newPassWord, String confirmPassword) {
        // user对象 ⾮空验证
        AssertUtil.isTrue(null==user,"⽤户未登录或不存在！");
        // 原始密码 ⾮空验证
        AssertUtil.isTrue(StringUtils.isBlank(oldPassWord),"请输⼊原始密码！");
        // 原始密码要与数据库中的密⽂密码保持⼀致
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(oldPassWord))),"原始密码不正确！");
        // 新密码 ⾮空校验
        AssertUtil.isTrue(StringUtils.isBlank(newPassWord),"请输⼊新密码！");
        // 新密码与原始密码不能相同
        AssertUtil.isTrue(oldPassWord.equals(newPassWord),"新密码不能与原始密码相同！");
        // 确认密码 ⾮空校验
        AssertUtil.isTrue(StringUtils.isBlank(confirmPassword),"请输⼊确认密码！");
        // 新密码要与确认密码保持⼀致
        AssertUtil.isTrue(!newPassWord.equals(confirmPassword),"新密码与确认密码不⼀致！");
    }

//    //基本资料 动态条件更新一条数据
//    public Integer updateByPrimaryKeySelective(User user){
//        //手机号不为空
//        AssertUtil.isTrue(StringUtils.isBlank(user.getPhone()),"手机号不能为空!");
//        //手机号合法
//        AssertUtil.isTrue(!PhoneUtil.isMobile(user.getPhone()),"手机号不合法!");
//        //插入是否成功
//        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"插入失败!");
//        return userMapper.updateByPrimaryKeySelective(user);
//    }

    //多条件分页查询用户信息
    public Map<String,Object> queryUserByParams(UserQuery userQuery){
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(userQuery.getPage(),userQuery.getLimit());
        PageInfo<User> pageInfo = new PageInfo<>(userMapper.selectByParams(userQuery));
        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    //添加用户
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUser(User user){
        //参数校验
        checkParams(user.getUserName(),user.getEmail(),user.getPhone());
        //设置默认值
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("123456"));
        //执行操作
        AssertUtil.isTrue(userMapper.insertSelective(user)<1,"用户添加失败!");

        // ⽤户⻆⾊分配用户id和角色id--roleIds
        relaionUserRole(user.getId(),user.getRoleIds());
    }

    /**
     * 批量添加用户和角色的关系表数据
     * @param id
     * @param roleIds
     */
    private void relaionUserRole(Integer id, String roleIds) {

        /**
         * ⽤户⻆⾊分配
         * 原始⻆⾊不存在 添加新的⻆⾊记录
         * 原始⻆⾊存在 添加新的⻆⾊记录
         * 原始⻆⾊存在 清空所有⻆⾊
         * 原始⻆⾊存在 移除部分⻆⾊
         * 如何进⾏⻆⾊分配???
         * 如果⽤户原始⻆⾊存在 ⾸先清空原始所有⻆⾊ 添加新的⻆⾊记录到⽤户⻆⾊表
         */

        //统计角色
        int count = userRoleMapper.countUserRoleByUserId(id);
        if(count>0){
            AssertUtil.isTrue(userRoleMapper.deleteUserRolesByUid(id)!=count,"⽤户⻆⾊分配失败!");
        }
        if(StringUtils.isNotBlank(roleIds)){
            //重新添加新的⻆⾊
            List<UserRole> urlist=new ArrayList<UserRole>();
            //遍历
            for (String rid: roleIds.split(",")) {
                UserRole ur=new UserRole();
                ur.setUserId(id);
                ur.setRoleId(Integer.parseInt(rid));
                ur.setCreateDate(new Date());
                ur.setUpdateDate(new Date());
                //添加容器
                urlist.add(ur);
            }
            //insert
            AssertUtil.isTrue(userRoleMapper.insertBatch(urlist)!=urlist.size(),"⽤户⻆⾊分配失败!");
        }
    }




    //用户模块--行内工具栏编辑--用户更新
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user){
        // 1. 参数校验
        // 通过id查询⽤户对象
        User temp = userMapper.selectByPrimaryKey(user.getId());
        // 判断对象是否存在
        AssertUtil.isTrue(temp == null, "待更新记录不存在！");
        // 验证参数
        checkUserParams(user.getUserName(),user.getEmail(),user.getPhone());
        // 2. 设置默认参数
        temp.setUpdateDate(new Date());
        // 3. 执⾏更新，判断结果
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "⽤户更新失败！");

        //⽤户⻆⾊分配
        Integer userId = userMapper.selectUserByName(user.getUserName()).getId();
        relaionUserRole(userId,user.getRoleIds());
    }

        //用户添加--参数校验
        private void checkParams(String userName,String email,String phone){
        //用户名非空
        AssertUtil.isTrue(StringUtils.isBlank(userName),"⽤户名不能为空！");
        //用户名不能重复
        User temp = userMapper.selectUserByName(userName);
        AssertUtil.isTrue(temp!=null,"该⽤户已存在！");
        //邮箱非空
        AssertUtil.isTrue(StringUtils.isBlank(email),"邮箱不能为空！");
        //手机号非空
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空！");
        //手机号合法
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"⼿机号码格式不正确！");
    }

    //删除用户
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(Integer userId) {
        User user = selectByPrimaryKey(userId);
        AssertUtil.isTrue(null == userId || null == user, "待删除记录不存在!");
        int count = userRoleMapper.countUserRoleByUserId(userId);
        if (count > 0) {
            AssertUtil.isTrue(userRoleMapper.deleteUserRolesByUid(userId) !=count, "⽤户⻆⾊删除失败!");
        }
        user.setIsValid(0);
        AssertUtil.isTrue(updateByPrimaryKeySelective(user) < 1, "⽤户记录删除失败!");
    }


    //批量删除用户
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUserByIds(Integer[] ids){
        AssertUtil.isTrue(ids == null || ids.length==0,"请选择待删除的⽤户记录!");
        AssertUtil.isTrue(userMapper.deleteBatch(ids)!=ids.length,"⽤户记录删除失败!");
    }

    //用户模块--行内工具栏编辑--参数校验
    private void checkUserParams(String userName,String email,String phone){
        //用户名非空
        AssertUtil.isTrue(StringUtils.isBlank(userName),"⽤户名不能为空！");
        //邮箱非空
        AssertUtil.isTrue(StringUtils.isBlank(email),"邮箱不能为空！");
        //手机号非空
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空！");
        //手机号合法
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"⼿机号码格式不正确！");

    }

    //查询所有的销售人员
    public List<Map<String, Object>> queryAllSales(){
        return userMapper.queryAllSales();
    }


    private UserModel buildInfo(User user) {
        UserModel userModel = new UserModel();
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());

        return userModel;
    }

    private void checkUserPwd(String tempPwd, String userPwd) {
        //加密
        userPwd = Md5Util.encode(userPwd);
        //对比
        AssertUtil.isTrue(!tempPwd.equals(userPwd),"密码不正确");
    }

    private void checkUser(String userName, String userPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"密码不能为空");
    }
}

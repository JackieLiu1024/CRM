后端用户登录功能
/**
* ⽤户登录
* 1. 验证参数
* 姓名 ⾮空判断
* 密码 ⾮空判断
* 2. 根据⽤户名，查询⽤户对象
* 3. 判断⽤户是否存在
* ⽤户对象为空，记录不存在，⽅法结束
* 4. ⽤户对象不为空
* ⽤户存在，校验密码
* 密码不正确，⽅法结束
* 5. 密码正确
* ⽤户登录成功，返回⽤户的相关信息 （定义UserModel类，返回⽤户某些信息）
*/


前端登录功能实现




后端修改密码功能
/**
* ⽤户密码修改
* 1. 参数校验
* userId ⾮空 ⽤户对象必须存在
* oldPassword ⾮空 与数据库中密⽂密码保持⼀致
* newPassword ⾮空 与原始密码不能相同
* confirmPassword ⾮空 与新密码保持⼀致
* 2. 设置⽤户新密码
* 新密码进⾏加密处理
* 3. 执⾏更新操作
* 受影响的⾏数⼩于1，则表示修改失败
*/


前端实现修改密码页面跳转

拦截器
/**
* 判断⽤户是否是登录状态
* 获取Cookie对象，解析⽤户ID的值
* 如果⽤户ID不为空，且在数据库中存在对应的⽤户记录，表示请求合法
* 否则，请求不合法，进⾏拦截，重定向到登录⻚⾯
*/


/*** 营销机会数据添加
* 1.参数校验
*   customerName:非空
*   linkMan:非空
*   linkPhone:非空 11位手机号
* 2.设置相关参数默认值
*   state:默认未分配 如果选择分配人 state 为已分配
*   assignTime: 如果选择分配人 时间为当前系统时间
*   devResult:默认未开发 如果选择分配人 devResult为开发中 0-未开发 1-开发中 2-开发成功 3-开发失败
*   isValid:默认有效数据(1-有效 0-无效)
*   createDate updateDate:默认当前系统时间
* 3.执行添加 判断结果
*/


/**
* 营销机会数据更新
* 1.参数校验
*   id:记录必须存在
*   customerName:非空
*   linkMan:非空
*   linkPhone:非空，11位手机号
* 2. 设置相关参数值
*   updateDate:系统当前时间
*   原始记录 未分配 修改后改为已分配(由分配人决定)
*   state 0->1 * assginTime 系统当前时间
*   devResult 0-->1
*   原始记录 已分配 修改后 为未分配
*   state 1-->0
*   assignTime 待定 null
*   devResult 1-->0
* 3.执行更新 判断结果 */



/**
* 添加⽤户
* 1. 参数校验
*   ⽤户名 ⾮空 唯⼀性
*   邮箱 ⾮空
*   ⼿机号 ⾮空 格式合法
* 2. 设置默认参数
*   isValid 1
*   creteDate 当前时间
*   updateDate 当前时间
*   userPwd 123456 -> md5加密
* 3. 执⾏添加，判断结果
*/


/**
* 更新⽤户
* 1. 参数校验
*   id ⾮空 记录必须存在
*   ⽤户名 ⾮空 唯⼀性
*   email ⾮空
*   ⼿机号 ⾮空 格式合法
* 2. 设置默认参数
*   updateDate
* 3. 执⾏更新，判断结果
* @param user
*/











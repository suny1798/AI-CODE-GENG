package com.suny.aicodegeng.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.suny.aicodegeng.model.dto.user.UserLoginRequest;
import com.suny.aicodegeng.model.dto.user.UserQueryRequest;
import com.suny.aicodegeng.model.dto.user.UserRegisterRequest;
import com.suny.aicodegeng.model.entity.User;
import com.suny.aicodegeng.model.vo.LoginUserVO;
import com.suny.aicodegeng.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 *  服务层。
 *
 * @author suny
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userRegisterRequest   用户注册请求类
     * @return 新用户 id
     */
    long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     *
     * @param userLoginRequest   用户登录请求类
     * @return 新用户 id
     */
    LoginUserVO userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);


    /**
     * 获取登录用户脱敏信息
     * @param user
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    UserVO getUserVO(User user);

    List<UserVO> getUserVOList(List<User> userList);

    QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 获取加密后的密码
     * @param userPassword
     * @return
     */
    String getEncryptPassword(String userPassword);



    /**
     * 退出登录
     * @param request
     */
    void userLogout(HttpServletRequest request);
}

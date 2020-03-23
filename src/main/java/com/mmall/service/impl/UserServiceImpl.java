package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author：wuhaoteng
 * @date：2020/3/20 1:46 下午
 * @desc：
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {
    private static final String TOKEN_PREFIX = "token_";
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        //检验姓名
        ServerResponse<String> validResponse = checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        //md5加密密码
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess("登陆成功", user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        //检验姓名
        ServerResponse<String> validResponse = checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        //校验邮箱
        validResponse = checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        user.setRole(Const.ROLE.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        //插入数据
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }


    @Override
    public ServerResponse<String> checkValid(String value, String type) {
        if (StringUtils.isNotBlank(type)) {
            if (type.equals(Const.USERNAME)) {
                int resultCount = userMapper.checkUserName(value);
                if (resultCount != 0) {
                    return ServerResponse.createByErrorMessage("用户已存在");
                }
            }
            if (type.equals(Const.EMAIL)) {
                int resultCount = userMapper.checkEmail(value);
                if (resultCount != 0) {
                    return ServerResponse.createByErrorMessage("邮箱已存在");
                }
            }
            return ServerResponse.createBySuccessMessage("检验成功");
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
    }

    @Override
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse<String> validResponse = checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestion(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess("查询成功", question);
        } else {
            return ServerResponse.createByErrorMessage("用户密码提示问题为空");
        }
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        ServerResponse<String> validResponse = checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("答案不正确");
        }

        //说明答案是用户的，并且正确的
        String token = UUID.randomUUID().toString();
        TokenCache.put(TOKEN_PREFIX + username, token);
        return ServerResponse.createBySuccess(token);
    }

    @Override
    public ServerResponse<String> resetPassword(String username, String newPassword, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误，token不能为空");
        }

        ServerResponse<String> validResponse = checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        String token = TokenCache.get(TOKEN_PREFIX + username);
        if (!StringUtils.equals(token, forgetToken)) {
            return ServerResponse.createByErrorMessage("token无效或已过期");
        }

        if (StringUtils.isBlank(newPassword)) {
            return ServerResponse.createByErrorMessage("新密码不能为空");
        }

        int resultCode = userMapper.updatePassword(username, MD5Util.MD5EncodeUtf8(newPassword));
        if (resultCode == 0) {
            return ServerResponse.createByErrorMessage("密码修改失败");
        }

        return ServerResponse.createBySuccess("密码修改成功");
    }

    @Override
    public ServerResponse<User> getUserInfo(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMessage("查询不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse<User> updateUserInfo(User user) {
        String email = user.getEmail();
        Integer userId = user.getId();
        //检查重复email，这里要剔除自身
        int resultCount = userMapper.checkEmailByUserId(email, userId);
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("邮箱已存在");
        }
        resultCount = userMapper.updateByPrimaryKeySelective(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("更新个人信息失败");
        }
        return ServerResponse.createBySuccess("更新个人信息成功", user);
    }

    @Override
    public ServerResponse checkManager(User user) {
        if (user != null && user.getRole() == Const.ROLE.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}

package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * @author：wuhaoteng
 * @date：2020/3/20 1:46 下午
 * @desc：
 */
public interface IUserService {
    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String value, String type);

    ServerResponse<String> selectQuestion(String username);

    ServerResponse<String> checkAnswer(String username, String question, String answer);

    ServerResponse<String> resetPassword(String username, String newPassword, String forgetToken);

    ServerResponse<User> getUserInfo(Integer userId);

    ServerResponse<User> updateUserInfo(User user);

    ServerResponse checkManager(User user);
}

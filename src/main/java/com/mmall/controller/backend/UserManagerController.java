package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author：wuhaoteng
 * @date：2020/3/23 3:42 下午
 * @desc：后台用户模块接口
 */
@Controller
@RequestMapping("/manage/user/")
public class UserManagerController {
    @Autowired
    private IUserService iUserService;

    /**
     * 管理员登陆
     *
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "managerLogin.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            User user = response.getData();
            if (iUserService.checkManager(user).isSuccess()) {
                session.setAttribute(Const.CURRENT_USER, response.getData());
            } else {
                return ServerResponse.createByErrorCodeMessage(ResponseCode.NOT_MANAGER.getCode(), ResponseCode.NOT_MANAGER.getDesc());
            }
        }
        return response;
    }

}

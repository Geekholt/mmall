package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author：wuhaoteng
 * @date：2020/3/23 5:15 下午
 * @desc：后台类目模块接口
 */

@Controller
@RequestMapping("/manage/category/")
public class CategoryManagerController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 添加商品分类
     *
     * @param session
     * @param categoryName
     * @param parentId
     * @return
     */
    @RequestMapping(value = "addCategory.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        //判断管理员权限
        if (!iUserService.checkManager(user).isSuccess()) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NOT_MANAGER.getCode(), ResponseCode.NOT_MANAGER.getDesc());
        }

        return iCategoryService.addCategory(categoryName, parentId);
    }

    /**
     * 修改商品分类名称
     *
     * @param session
     * @param categoryId
     * @param categoryName
     * @return
     */
    @RequestMapping(value = "updateCategory.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> updateCategory(HttpSession session, Integer categoryId, String categoryName) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        //判断管理员权限
        if (!iUserService.checkManager(user).isSuccess()) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NOT_MANAGER.getCode(), ResponseCode.NOT_MANAGER.getDesc());
        }

        return iCategoryService.updateCategory(categoryId, categoryName);
    }

    /**
     * 获取分类集合（只获取当前parentId下的子节点，不递归）
     *
     * @param session
     * @param parentId
     * @return
     */
    @RequestMapping(value = "getChildCategoryByParentId.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<List<Category>> getChildCategoryByParentId(HttpSession session, @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        //判断管理员权限
        if (!iUserService.checkManager(user).isSuccess()) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NOT_MANAGER.getCode(), ResponseCode.NOT_MANAGER.getDesc());
        }

        return iCategoryService.getChildCategoryByParentId(parentId);
    }


    @RequestMapping(value = "getDeepCategoryById.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<List<Integer>> getDeepCategoryById(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        //判断管理员权限
        if (!iUserService.checkManager(user).isSuccess()) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NOT_MANAGER.getCode(), ResponseCode.NOT_MANAGER.getDesc());
        }

        return iCategoryService.getDeepCategoryById(categoryId);
    }

}

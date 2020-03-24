package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author：wuhaoteng
 * @date：2020/3/23 5:22 下午
 * @desc：
 */
@Service("iCategoryService")
public class CategoryServiceImp implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse<String> addCategory(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createBySuccessMessage("参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        //设置分类是可用的
        category.setStatus(true);
        int resultCode = categoryMapper.insert(category);
        if (resultCode == 0) {
            return ServerResponse.createByErrorMessage("添加品类失败");
        }
        return ServerResponse.createByErrorMessage("添加品类成功");
    }

    @Override
    public ServerResponse<String> updateCategory(Integer categoryId, String categoryName) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createBySuccessMessage("参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int resultCode = categoryMapper.updateByPrimaryKeySelective(category);
        if (resultCode == 0) {
            return ServerResponse.createByErrorMessage("品类更新失败");
        }
        return ServerResponse.createByErrorMessage("品类更新成功");
    }

    @Override
    public ServerResponse<List<Category>> getChildCategoryByParentId(Integer parentId) {
        if (parentId == null) {
            return ServerResponse.createBySuccessMessage("参数错误");
        }
        List<Category> categories = categoryMapper.selectChildCategoryByParentId(parentId);
        return ServerResponse.createBySuccess(categories);
    }

    @Override
    public ServerResponse<List<Category>> getDeepCategoryByParentId(Integer parentId) {
        List<Category> categories = categoryMapper.selectChildCategoryByParentId(parentId);
        findChildCategory(categories);
        return ServerResponse.createBySuccess(categories);
    }


    //递归算法,算出子节点
    private List<Category> findChildCategory(List<Category> categories) {
        for (Category category : categories) {
            Integer categoryId = category.getId();
            List<Category> list = categoryMapper.selectChildCategoryByParentId(categoryId);
            category.setCategories(findChildCategory(list));
        }
        return categories;
    }
}

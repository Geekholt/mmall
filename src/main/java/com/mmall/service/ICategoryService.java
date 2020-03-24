package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

/**
 * @author：wuhaoteng
 * @date：2020/3/23 5:22 下午
 * @desc：
 */
public interface ICategoryService {

    ServerResponse<String> addCategory(String categoryName, Integer parentId);

    ServerResponse<String> updateCategory(Integer categoryId, String categoryName);

    ServerResponse<List<Category>> getChildCategoryByParentId(Integer parentId);

    ServerResponse<List<Integer>> getDeepCategoryById(Integer categoryId);
}

package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;

/**
 * @author：wuhaoteng
 * @date：2020/3/24 4:38 下午
 * @desc：
 */
public interface IProductService {
    ServerResponse saveOrUpdateProduct(Product product);
}

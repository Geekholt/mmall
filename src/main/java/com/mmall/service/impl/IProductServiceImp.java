package com.mmall.service.impl;

import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author：wuhaoteng
 * @date：2020/3/24 4:39 下午
 * @desc：
 */
@Service("iProductServiceImp")
public class IProductServiceImp implements IProductService {
    @Autowired
    private ProductMapper productMapper;

    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        if (product != null) {
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImagesArray = product.getSubImages().split(",");
                product.setMainImage(subImagesArray[0]);
            }
            if (product.getId() == null) {
                //新增商品
                int resultCount = productMapper.insert(product);
                if (resultCount == 0) {
                    return ServerResponse.createBySuccessMessage("新增商品失败");
                }
                return ServerResponse.createBySuccessMessage("新增商品成功");
            } else {
                //更新商品
                int resultCount = productMapper.updateByPrimaryKeySelective(product);
                if (resultCount == 0) {
                    return ServerResponse.createBySuccessMessage("更新商品失败");
                }
                return ServerResponse.createBySuccessMessage("更新商品成功");
            }
        }
        return null;
    }
}

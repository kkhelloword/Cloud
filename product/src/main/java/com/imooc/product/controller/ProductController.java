package com.imooc.product.controller;


import com.imooc.product.VO.ProductInfoVO;
import com.imooc.product.VO.ProductVO;
import com.imooc.product.VO.ResultVO;
import com.imooc.product.dataobject.ProductCategory;
import com.imooc.product.dataobject.ProductInfo;
import com.imooc.product.dto.CartDto;
import com.imooc.product.service.CategoryService;
import com.imooc.product.service.ProductService;
import com.imooc.utils.ResultVOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResultVO list(@RequestParam(value = "sellerId",required = false) String sellerId) {
        /*查询所有的上架商品*/
        List<ProductInfo> productInfoList = productService.findUpAll();
        /*查询类目*/
        List<Integer> list = productInfoList.stream().map(e -> e.getCategoryType()).collect(Collectors.toList());
        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeIn(list);

        ArrayList<ProductVO> ProductVOList = new ArrayList<>();
        /*数据拼装*/
        for (ProductCategory productCategory : productCategoryList) {
            ProductVO productVO = new ProductVO();
            productVO.setCategoryName(productCategory.getCategoryName());
            productVO.setCategoryType(productCategory.getCategoryType());
            ArrayList<ProductInfoVO> arrayList = new ArrayList<>();
            for (ProductInfo productInfo : productInfoList) {
                if (productInfo.getCategoryType() .equals(productCategory.getCategoryType())){
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo,productInfoVO);
                    arrayList.add(productInfoVO);
                }
            }
            productVO.setListFoods(arrayList);
            ProductVOList.add(productVO);
        }
        return ResultVOUtil.success(ProductVOList);
    }

    /**
     * 查询商品列表(提供给订单服务使用)
     *@param  [productIds]
     *@return java.util.List<com.imooc.product.dataobject.ProductInfo>
     */
    @PostMapping("/productList")
    public List<ProductInfo> getProductList(@RequestBody List<String> productIds){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return productService.getProductList(productIds);
    }

    @PostMapping("/decreaseStock")
    public void decreaseStock(@RequestBody List<CartDto> cartDtos){
        productService.decreaseStock(cartDtos);
    }
}

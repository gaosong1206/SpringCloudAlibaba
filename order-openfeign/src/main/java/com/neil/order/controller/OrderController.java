package com.neil.order.controller;

import com.neil.order.feign.ProductFeignService;
import com.neil.order.feign.StockFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    StockFeignService stockFeignService;

    @Autowired
    ProductFeignService productFeignService;

    @RequestMapping("/add")
    public String add(){
        System.out.println("下单成功");
        String s = stockFeignService.reduce();
        String p = productFeignService.get(1);
        return "hello feign "+s+" "+p;
    }

}

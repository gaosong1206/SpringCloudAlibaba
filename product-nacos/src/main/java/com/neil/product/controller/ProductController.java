package com.neil.product.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Value("${server.port}")
    private Integer port;

    @RequestMapping("/{id}")
    public String get(@PathVariable("id") Integer id) throws InterruptedException {

        TimeUnit.SECONDS.sleep(4);
        System.out.println("查询商品"+id);
        return "查询商品"+id+":"+port;
    }


}

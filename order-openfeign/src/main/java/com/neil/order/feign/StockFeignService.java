package com.neil.order.feign;

import com.neil.order.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "stock-nacos",path = "/stock",configuration = FeignConfig.class)
public interface StockFeignService {

    /**
     * 声明需要调用的rest接口对应的方法
     * @return
     */
    @RequestMapping("/reduce")
    String reduce();

}


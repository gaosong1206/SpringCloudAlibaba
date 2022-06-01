package com.neil.order.feign;

import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "product-nacos",path = "/product")
public interface ProductFeignService {

    //@RequestMapping("/{id}")
    //// Feign中必须使用@PathVariable指定路径中的参数
    //String get(@PathVariable("id") Integer id);

    @RequestLine("GET /{id}")
    // Feign原生注解中必须使用@Param指定路径中的参数
    String get(@Param("id") Integer id);

}

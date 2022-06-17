package com.neil.order.config;

import feign.Contract;
import feign.Logger;
import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 全局配置：当使用@Configuration注解时，会将配置作用于所有的服务提供方
 * 局部配置：如果只想针对某一个服务进行配置，就不要加@Configuration
 */
//@Configuration
public class FeignConfig {

    /**
     * 全局日志配置
     * @return
     */
    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

    /**
     * 全局契约配置
     * @return
     */
/*    @Bean
    public Contract feignContract(){
        return new Contract.Default();
    }*/

    /**
     * 全局超时配置
     * @return
     */
/*    @Bean
    public Request.Options options(){

        return new Request.Options(5000,10000);

    }*/

}

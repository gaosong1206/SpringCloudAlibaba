package com.neil.order.feign;

import org.springframework.stereotype.Component;

@Component
public class StockFeignServiceFallback implements StockFeignService{

    @Override
    public String reduce2() {
        return "降级啦";
    }
}

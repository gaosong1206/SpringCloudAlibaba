package com.neil.order.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Override
    @SentinelResource(value = "getUser",blockHandler="blockHandlerForGetUser")
    public String getUser() {
        return "song.gao";
    }

    public String blockHandlerForGetUser(BlockException exception) {
        return "链路限流规则";
    }

}

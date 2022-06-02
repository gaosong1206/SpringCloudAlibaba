package com.neil.order.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/order")
public class OrderController {

    @RequestMapping("/add")
    public String add(){
        System.out.println("下单成功");
        return "hello world";
    }

    @RequestMapping("/flow")
    @SentinelResource(value = "flow",blockHandler = "blockHandlerForFlow")
    public String flow(){
        System.out.println("正常访问");
        return "正常访问";
    }

    public String blockHandlerForFlow(BlockException exception){
        return "限流规则";
    }

    @RequestMapping("/flowThread")
    @SentinelResource(value = "flowThread",blockHandler = "blockHandlerForFlowThread")
    public String flowFhread() throws InterruptedException {

        TimeUnit.SECONDS.sleep(5);
        System.out.println("正常访问");
        return "正常访问";

    }

    public String blockHandlerForFlowThread(BlockException exception){
        return "限流(线程规则)";
    }


}

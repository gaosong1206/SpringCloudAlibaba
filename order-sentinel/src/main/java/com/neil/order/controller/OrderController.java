package com.neil.order.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.neil.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @RequestMapping("/add")
    public String add() {
        System.out.println("下单成功");
        return "hello world";
    }

    @RequestMapping("/test1")
    @SentinelResource(value = "test1", blockHandler = "blockHandlerForTest")
    public String test1(){
        return orderService.getUser();
    }

    @RequestMapping("/test2")
    @SentinelResource(value = "test2", blockHandler = "blockHandlerForTest")
    public String test2(){
        return orderService.getUser();
    }

    public String blockHandlerForTest(BlockException exception) {
        return "链路限流规则";
    }

    @RequestMapping("/get")
    @SentinelResource(value = "get", blockHandler = "blockHandlerForGet")
    public String get() {
        return "查询订单";
    }

    public String blockHandlerForGet(BlockException exception) {
        return "关联限流规则";
    }

    @RequestMapping("/flow")
    @SentinelResource(value = "flow", blockHandler = "blockHandlerForFlow")
    public String flow() {
        System.out.println("正常访问");
        return "正常访问";
    }

    public String blockHandlerForFlow(BlockException exception) {
        return "限流规则";
    }

    @RequestMapping("/flowThread")
    @SentinelResource(value = "flowThread", blockHandler = "blockHandlerForFlowThread")
    public String flowFhread() throws InterruptedException {

        TimeUnit.SECONDS.sleep(2);
        System.out.println("正常访问");
        return "正常访问";

    }

    public String blockHandlerForFlowThread(BlockException exception) {
        return "限流(线程规则)";
    }

    @RequestMapping("/err")
    public String err() {
        int i = 1/0;
        return "hello";
    }

    @RequestMapping("/get/{id}")
    @SentinelResource(value = "getById",blockHandler = "HotBlockHandler")
    public String getById(@PathVariable("id")String id){
        System.out.println("正常访问");
        return "正常访问";
    }

    public String HotBlockHandler(String id,BlockException e){
        return "热点异常处理";
    }


}
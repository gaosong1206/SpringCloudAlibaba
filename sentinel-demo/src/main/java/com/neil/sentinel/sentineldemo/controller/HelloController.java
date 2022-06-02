package com.neil.sentinel.sentineldemo.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.neil.sentinel.sentineldemo.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class HelloController {

    private static final String RESOURCE_NAME = "hello";
    private static final String USER_RESOURCE_NAME = "user";
    private static final String DEGRADE_RESOURCE_NAME = "degrade";

    // 进行流量控制
    @RequestMapping("/hello")
    public String hello(){

        Entry entry = null;

        try {

            // sentinel针对资源进行限制的
            entry= SphU.entry(RESOURCE_NAME);

            // 被保护的业务逻辑
            String str = "hello world";
            log.info("===="+str+"====");
            return str;

        } catch (BlockException e) {

            // 资源访问阻止，被限流或者降级。
            // 进行相应的处理操作
            log.info("block!");
            return "被流控了";

        }catch (Exception e){

            // 若需要配置降级规则，需要通过这种方式记录业务异常。
            Tracer.traceEntry(e,entry);

        }finally {

            if (entry!=null){
                entry.exit();
            }

        }

        return null;

    }


    /**
     * @SentinelResource 改善接口中资源定义和被流控降级后的处理方法
     * 使用方法： 1：添加依赖：sentinel-annotation-aspectj
     *          2：配置bean-SentinelResourceAspect
     *          3：通过value声明资源，blockHandler设置流控降级后的处理方法，默认该方法必须声明在同一个类中
     *          4：如果不在一个类中，通过blockHandlerClass声明方法的类，且方法必须声明为 public static
     *          5: 当接口中出现了异常，就可以交给fallback指定的方法处理
     *          6: blockHandler和fallback同时出发，则blockHandler优先级更高。
     *          7: exceptionsToIgnore排除哪些异常不需要处理
     * @param id
     * @return
     */
    @RequestMapping("/user")
    @SentinelResource(
            value = USER_RESOURCE_NAME,
            fallback = "fallbackHandlerForGetUser",
            blockHandlerClass = HelloController.class,
            blockHandler = "blockHandlerForGetUser"
    )
    public User getUser(String id){

        int i = 1/0;
        return new User("song.gao");

    }

    public User fallbackHandlerForGetUser(String id,Throwable throwable){
        throwable.printStackTrace();
        return new User("异常处理!");
    }

    /**
     * 注意: 声明为public,返回值必须和原方法一直,包含原方法的参数，顺序一致
     *      可以在参数最后添加BlockException，
     * @param id
     * @param ex
     * @return
     */
    public User blockHandlerForGetUser(String id,BlockException ex){
        ex.printStackTrace();
        return new User("流控!");
    }

    @PostConstruct // init-method
    private static void initFlowRules(){

        // 流控规则
        List<FlowRule> rules = new ArrayList<>();

        // 流控
        FlowRule rule = new FlowRule();
        // 设置受保护的资源
        rule.setRefResource(RESOURCE_NAME);
        // 设置流控规则QPS
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // 设置受保护的资源阈值
        // set limit QPS to 20
        rule.setCount(1);
        rules.add(rule);

        // 通过@SentinelResource注解来定义资源并配置降级和流控的处理方法
        FlowRule rule2 = new FlowRule();
        // 设置受保护的资源
        rule2.setRefResource(USER_RESOURCE_NAME);
        // 设置流控规则QPS
        rule2.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // 设置受保护的资源阈值
        // set limit QPS to 20
        rule.setCount(1);
        rules.add(rule);


        // 加载配置的规则
        FlowRuleManager.loadRules(rules);

    }

    @PostConstruct // 初始化
    public void initDegradeRule(){

        /* 降级规则，异常 */
        List<DegradeRule> degradeRules = new ArrayList<>();
        DegradeRule degradeRule = new DegradeRule();
        degradeRule.setResource(DEGRADE_RESOURCE_NAME);

        /* 一分钟内，执行了2次，出现了2次异常，就会触发熔断 */
        // 设置规则的策略，异常数
        degradeRule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT);
        // 触发熔断的异常数：2
        degradeRule.setCount(2);
        // 触发熔断的最小请求书：2
        degradeRule.setMinRequestAmount(2);
        // 统计时长 单位：毫秒 默认1s
        // degradeRule.setStatIntervalMs(60*1000); // 时间太短不好测
        degradeRules.add(degradeRule);

        // 熔断持续的时长:10s
        // 一旦触发熔断，再次请求对应的接口就会直接调用降级方法
        // 10s后-半开状态：恢复接口请求的调用，如果第一次请求异常,再次熔断，不会根据设置的条件进行判定。
        degradeRule.setTimeWindow(10);

        DegradeRuleManager.loadRules(degradeRules);

    }

    @RequestMapping("/degrade")
    @SentinelResource(
            value = DEGRADE_RESOURCE_NAME,
            entryType = EntryType.IN,
            blockHandler = "blockHandleForFb"
    )
    public User degrade(String id){
        throw new RuntimeException("异常");
    }

    public User blockHandleForFb(String id,BlockException ex){

        return new User("降级");

    }




}

package com.evoa.cloud.contentcenter.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangxinxin
 */
@Slf4j
@RestController
public class HotTestController {

    @GetMapping("hot-test")
    @SentinelResource("hot")
    public String hotTest(@RequestParam(required = false) String a, @RequestParam(required = true) String b){
      log.info("a is " + a + " b is "+ b);
      return a+b;
    }

}

package com.bdyx.util;

import com.bdyx.http.HttpResult;
import com.bdyx.service.HttpAPIService;
import lombok.extern.log4j.Log4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Log4j
@Component
public class TimeStart {
    @Resource
    private HttpAPIService httpAPIService;

    //配置看地址https://www.jianshu.com/p/1defb0f22ed1
    @Scheduled(fixedRate = 6000)
    private void process() throws Exception {
        log.info("11111");
        HttpResult httpResult = httpAPIService.doPost("http://localhost:8083/mz/testpropars");
        log.info(httpResult);
    }
//    @Scheduled(fixedRate = 6000)
//    public void reportCurrentTime() {
//        log.info("现在时间：" + dateFormat.format(new Date()));
//    }
}

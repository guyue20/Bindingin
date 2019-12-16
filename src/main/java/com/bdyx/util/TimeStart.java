package com.bdyx.util;

import com.bdyx.http.HttpResult;
import com.bdyx.mapper.Tkmapper;
import com.bdyx.service.HttpAPIService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Log4j
@Component
public class TimeStart {
    @Resource
    private HttpAPIService httpAPIService;
    @Autowired
    Tkmapper tkmapper;

    //配置看地址https://www.jianshu.com/p/1defb0f22ed1
//    @Scheduled(fixedRate = 6000)
//    private void process() throws Exception {
//        log.info("11111");
//        HttpResult httpResult = httpAPIService.doPost("http://localhost:8083/mz/testpropars");
//        log.info(httpResult);
//    }
    //@Scheduled(fixedRate = 6000)
    public void reportCurrentTime() {
        try {
            String hsql = "exec p_wa_get_NoInform ";
            log.info(hsql);
            String charge_no = tkmapper.getprpString(hsql);
            log.info(charge_no);
            //在这调用地三方接口
            // Map<Sting, Object> stingObjectMap = new Map<Sting, Object>();
            HttpResult httpResult = httpAPIService.doPost("http://localhost:8083/mz/testpropars");
            log.info(httpResult);
            String sql = "exec [p_wa_charge_SetInform ";
            log.info(sql);
            Map<String, Object> map = tkmapper.callPronamesql(hsql);
            log.info(map);

        } catch (Exception e) {

        }
    }
}

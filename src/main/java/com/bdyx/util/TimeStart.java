package com.bdyx.util;

import com.alibaba.fastjson.JSONObject;
import com.bdyx.http.HttpResult;
import com.bdyx.mapper.Tkmapper;
import com.bdyx.service.HttpAPIService;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Log4j
@Component
@Data
@PropertySource(value = "config.properties")
public class TimeStart {
    @Value("${bsUrl}")
    String bsUrl;
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
    // @Scheduled(fixedRate = 6000*100)
    public void reportCurrentTime() {
        try {
            String hsql = "exec p_wa_get_NoInform ";
            log.info(hsql);
            List<Map<String, Object>> chargeNos = tkmapper.getprpString(hsql);
            log.info(chargeNos);
            for (int i = 0; i < chargeNos.size(); i++) {
                JSONObject resjson = new JSONObject();
                Map<String, Object> map1 = chargeNos.get(i);
                resjson.put("charge_no", map1.get("charge_no").toString());  //申请单号
                resjson.put("charge_date", map1.get("charge_date").toString());//收费时间
                resjson.put("charge_opera", map1.get("charge_opera").toString());//收费人
                resjson.put("charge_price", map1.get("charge_price").toString());//收费价格
                resjson.put("Invoice_number", map1.get("Invoice_number").toString());//发票号
                resjson.put("status", "1");   //默认1；
                log.info("请求地址    " + bsUrl);
                log.info("请求参数    " + resjson);
                HttpResult httpResult = httpAPIService.doPost(bsUrl, resjson);
                if (httpResult.getCode() == 0) {
                    //成功
                    String sql = "exec p_wa_charge_SetInform '" + chargeNos.get(i) + "'";
                    log.info(sql);
                    Map<String, Object> map = tkmapper.callPronamesql(hsql);
                } else {
                    log.info(httpResult);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

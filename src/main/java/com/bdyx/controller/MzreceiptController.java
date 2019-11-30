package com.bdyx.controller;

import com.alibaba.fastjson.JSONObject;
import com.bdyx.config.Configbase;
import com.bdyx.entity.Mzreceipt;
import com.bdyx.http.HttpResult;
import com.bdyx.mapper.Tkmapper;
import com.bdyx.service.HttpAPIService;;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Log4j
@Controller
@RequestMapping("/mz")
public class MzreceiptController {
    //    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private HttpAPIService httpAPIService;
    @Autowired
    Configbase configbase;
    @Autowired
    Tkmapper tkmapper;

    @RequestMapping("/getList")
    @ResponseBody
    public List<Mzreceipt> getList() {
        //callProname
        List<Mzreceipt> list = tkmapper.getList("14", "1", "02429");
        return list;
    }

    @RequestMapping("/testpro")
    @ResponseBody
    public Map<String, Object> testPro() {
        Map<String, Object> qry_medicalByID = tkmapper.callProname("QRY_MedicalByID", "'aaa','60000200'");
        log.info(qry_medicalByID);
        return qry_medicalByID;
    }

    //@RequestBody接body参数 @RequestHead 接收head参数 request
    //@RequestParam("head") String headstr, @RequestParam("body") String bodystr
    @RequestMapping(value = "/testpropars", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, Object> testPropars() {
//        JSONObject head = JSONObject.parseObject(headstr);
//        JSONObject body = JSONObject.parseObject(bodystr);
//        log.info(head);
//        log.info(body);
        Map<String, Object> stringObjectMap = tkmapper.callPronamesql("exec QRY_MedicalByID 'aaa','60000200'");
        return stringObjectMap;
    }

    @RequestMapping("httpclient")
    @ResponseBody
    public String test() throws Exception {
        HttpResult httpResult = httpAPIService.doPost("http://localhost:8083/mz/testpropars");
        JSONObject object = JSONObject.parseObject(httpResult.toString());
        return object.toString();
    }
}

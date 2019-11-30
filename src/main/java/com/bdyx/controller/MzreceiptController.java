package com.bdyx.controller;

import com.alibaba.fastjson.JSONObject;
import com.bdyx.config.Configbase;
import com.bdyx.entity.Mzreceipt;
import com.bdyx.http.HttpResult;
import com.bdyx.mapper.Tkmapper;


import com.bdyx.service.HttpAPIService;
import com.fasterxml.jackson.core.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/mz")
public class MzreceiptController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
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
        return qry_medicalByID;
    }

    @RequestMapping(value = "/testpropars", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> testPropars() {
        Map<String, Object> stringObjectMap = tkmapper.callPronamesql("exec QRY_MedicalByID 'aaa','60000200'");
        logger.info(stringObjectMap.toString());
        logger.info(configbase.getTesttr());
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

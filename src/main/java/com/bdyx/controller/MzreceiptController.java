package com.bdyx.controller;

import com.bdyx.entity.Mzreceipt;
import com.bdyx.mapper.Tkmapper;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/mz")
public class MzreceiptController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
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

    @RequestMapping("/testpropars")
    @ResponseBody
    public Map<String, Object> testPropars() {
        Map<String, Object> stringObjectMap = tkmapper.callPronamesql("exec QRY_MedicalByID 'aaa','60000200'");
        logger.info(stringObjectMap.toString());
        return stringObjectMap;
    }
}

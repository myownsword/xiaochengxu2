package com.demo.action;

import com.base.action.BaseAction;
import com.demo.business.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author wenhm
 *
 * @param <T>注入的Service
 * @param <R>返回的数据类型
 */
@Controller
@RequestMapping("/sys/uacm/uum")
public class DemoAction extends BaseAction<DemoService<Map<String,Object>>, Map<String,Object>> {

    @RequestMapping
    public String index(HttpServletRequest request , ModelMap model){

        return "demo/demo_index" ;
    }



}
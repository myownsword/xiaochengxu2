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
 */
@Controller
@RequestMapping("/demo")
public class DemoAction extends BaseAction<DemoService<Map<String,Object>>, Map<String,Object>> {

    @RequestMapping
    public String index(HttpServletRequest request , ModelMap model){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("mapping","getIdentityTypeList");
        List<Map<String,Object>> list = service.list(map);
        model.addAttribute("list",list);
        return "demo/demo_index" ;  
    }



}
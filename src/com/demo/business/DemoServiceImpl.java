package com.demo.business;

import com.base.bussiness.BaseServiceImpl;
import com.demo.mapper.DemoMapper;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DemoServiceImpl extends BaseServiceImpl<DemoMapper,Map<String,Object>> implements DemoService<Map<String,Object>>{


}
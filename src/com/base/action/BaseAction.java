package com.base.action;

import com.base.bussiness.BaseService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 *
 * @param <T>注入的Service
 * @param <R>返回的数据类型
 */
public abstract class BaseAction<T extends BaseService<R>, R extends Object> {

//	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected T service ;

	public BaseAction() {
		super();
	}

	@Autowired
	public void setService(T t){
		this.service = t ;
	}


//	/**
//	 * 分页获取数据
//	 * @param map
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="page" , method= RequestMethod.POST)
//	public PageInfo<R> page(@RequestBody Map<String,Object> map, HttpServletRequest request){
//		map.put("current_user", request.getRemoteUser());
//		return service.page(map);
//	}

	/**
	 * 获取符合条件的所有数据
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="list" , method=RequestMethod.POST)
	public List<R> list(@RequestBody Map<String,Object> map, HttpServletRequest request){

		return service.list(map);
	}

	/**
	 * 根据Id获取数据
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="get" , method=RequestMethod.POST)
	public R get(@RequestBody Map<String,Object> map, HttpServletRequest request){
		map.put("current_user", request.getRemoteUser());
		R r = service.get(map) ;
		if(r==null){
			return (R)new HashMap();
		}
		return r;
	}

	/**
	 * 保存或者修改
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="saveOrUpdate" , method=RequestMethod.POST)
	public boolean saveOrUpdate(@RequestBody Map<String,Object> map, HttpServletRequest request){
		String key = "resource_id" ;//数据库中的数据表的主键默认为resource_id
		if(map.get("key")!=null && !"".equals(map.get("key"))){
			//如果进行了自定义请告知
			key = map.get("key").toString();
		}
		//如果主键不为空，则进行修改操作
		if(map.get(key)!=null && !"".equals(map.get(key))){
//			map.put("modify_time", DateFormatUtils.format(UtilTools.getDate(),"yyyy-MM-dd HH:mm:ss"));
			map.put("modify_user", request.getRemoteUser());
			return service.update(map);
		}
		//否则进行添加操作
		else{
			//判断是否为重复提交
			if(isRepeatSubmit(map.get("token") , request)){
				return false ;
			}
//			map.put(key, UtilTools.getUUID());
//			map.put("create_time", DateFormatUtils.format(UtilTools.getDate(),"yyyy-MM-dd HH:mm:ss"));
			map.put("create_user", request.getRemoteUser());
			return service.add(map);
		}
	}

	/**
	 * 添加新记录
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="add" , method=RequestMethod.POST)
	public boolean add(@RequestBody Map<String,Object> map, HttpServletRequest request){
		//判断是否为重复提交
		if(isRepeatSubmit(map.get("token") , request)){
			return false ;
		}
		String key = "resource_id" ;//数据库中的数据表的主键默认为resource_id
		if(map.get("key")!=null && !"".equals(map.get("key"))){
			//如果进行了自定义请告知
			key = map.get("key").toString();
		}
//		map.put(key, UtilTools.getUUID());
//		map.put("create_time", DateFormatUtils.format(UtilTools.getDate(),"yyyy-MM-dd HH:mm:ss"));
		map.put("create_user", request.getRemoteUser());
		return service.add(map);
	}

	/**
	 * 批量添加记录
	 * @param list
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="addBatch" , method=RequestMethod.POST)
	public boolean addBatch(@RequestBody List<Map<String,Object>> list,
							@RequestParam(value="token", defaultValue="")String token , HttpServletRequest request){
		//判断是否为重复提交
		if(isRepeatSubmit(token , request)){
			return false ;
		}
		for(Map<String,Object> map : list){
			String key = "resource_id" ;//数据库中的数据表的主键默认为resource_id
			if(map.get("key")!=null && !"".equals(map.get("key"))){
				//如果进行了自定义请告知
				key = map.get("key").toString();
			}
//			map.put(key, UtilTools.getUUID());
//			map.put("create_time", DateFormatUtils.format(UtilTools.getDate(),"yyyy-MM-dd HH:mm:ss"));
			map.put("create_user", request.getRemoteUser());
		}
		return service.addBatch(list);
	}

	/**
	 * 修改记录
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="update" , method=RequestMethod.POST)
	public boolean update(@RequestBody Map<String,Object> map, HttpServletRequest request){
//		map.put("modify_time", DateFormatUtils.format(UtilTools.getDate(),"yyyy-MM-dd HH:mm:ss"));
		map.put("modify_user", request.getRemoteUser());
		return service.update(map);
	}

	/**
	 * 批量修改记录
	 * @param list
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="updateBatch" , method=RequestMethod.POST)
	public boolean updateBatch(@RequestBody List<Map<String,Object>> list, HttpServletRequest request){
		for(Map<String,Object> map : list){
//			map.put("modify_time", DateFormatUtils.format(UtilTools.getDate(),"yyyy-MM-dd HH:mm:ss"));
			map.put("modify_user", request.getRemoteUser());
		}
		return service.updateBatch(list);
	}

	/**
	 * 删除（批量删除）记录
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="delete" , method=RequestMethod.POST)
	public boolean delete(@RequestParam(value="ids", required=true)String ids, @RequestParam(value="mapping", defaultValue="delete")String mapping){
		return service.delete(ids , mapping);
	}

	/**
	 * 删除（批量删除）记录
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="deleteExt" , method=RequestMethod.POST)
	public boolean deleteExt(@RequestBody Map<String,Object> map, HttpServletRequest request){
		map.put("current_user", request.getRemoteUser());
		return service.deleteExt(map);
	}

	/**
	 * 根据条件判断记录是否存在
	 * (校验插件返回false则为校验失败，当server返回true的时候表示记录存在；
	 * 所以要做 server 的返回结果前进行取反操作)
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="exist" , method=RequestMethod.POST)
	public boolean exist(@RequestBody Map<String,Object> map, HttpServletRequest request){
		return !service.exist(map) ;
	}

	/**
	 * 切换状态
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="toggle" , method=RequestMethod.POST)
	public boolean toggle(@RequestBody Map<String,Object> map, HttpServletRequest request){
		return service.toggle(map);
	}

	/**
	 * 是否为重复提交
	 * @param request
	 * @return
	 */
	protected boolean isRepeatSubmit(Object token , HttpServletRequest request) {
		HttpSession session = request.getSession(false) ;
		Object serverToken =session.getAttribute("token");
		session.removeAttribute("token");
		if (token==null || serverToken == null) {
			return true;
		}else if (!token.equals(serverToken.toString())) {
			return true;
		}
		return false;
	}




}
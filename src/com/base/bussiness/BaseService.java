package com.base.bussiness;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @param <R>返回结果的类型
 */
public interface BaseService<R extends Object> {

//	/**
//	 * 按条件分页获取数据
//	 * @param map
//	 * @return
//	 */
//	PageInfo<R> page(Map<String, ? extends Object> map);

	/**
	 * 按条件检索数据
	 * @param map
	 * @return
	 */
	List<R> list(Map<String, ? extends Object> map);

	/**
	 * 根据Id获取指定的唯一记录
	 * @param map
	 * @return
	 */
	R get(Map<String, ? extends Object> map);

	/**
	 * 添加记录
	 * @param map
	 * @return
	 */
	boolean add(Map<String, ? extends Object> map);

	/**
	 * 批量添加记录
	 * @param list
	 * @return
	 */
	boolean addBatch(List<Map<String, Object>> list) ;

	/**
	 * 批量添加记录
	 * @param mapping
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	boolean addBatch(String mapping, Map<String, List> map) ;

	/**
	 * 修改记录
	 * @param map
	 * @return
	 */
	boolean update(Map<String, ? extends Object> map);

	/**
	 * 批量修改记录
	 * @param map
	 * @return
	 */
	boolean updateBatch(List<Map<String, Object>> list);

	/**
	 * [批量]删除记录
	 * @param ids
	 * @param mapping	SQL ID
	 * @param dialect 方言
	 * @return
	 */
	boolean delete(String ids, String mapping, String dialect);

	/**
	 * [批量]删除记录
	 * @param ids
	 * @param mapping	SQL ID
	 * @param dialect 方言
	 * @return
	 */
	boolean delete(String ids, String mapping);

	/**
	 * [批量]删除记录
	 * @param ids
	 * @param mapping	SQL ID
	 * @param dialect 方言
	 * @return
	 */
	boolean deleteExt(Map<String, ? extends Object> map);

	/**
	 * 根据条件判断记录是否存在
	 * @param map
	 * @return
	 */
	boolean exist(Map<String, ? extends Object> map);

	/**
	 * 切换记录的状态
	 * @param map
	 * @return
	 */
	boolean toggle(Map<String, ? extends Object> map);
	
}
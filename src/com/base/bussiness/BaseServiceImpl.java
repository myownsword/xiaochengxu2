package com.base.bussiness;

import com.base.mapper.BaseMapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by WENHM on 2018/3/22.
 */
public abstract class BaseServiceImpl<T extends BaseMapper, R> implements BaseService<R> {
//    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    protected SqlSessionTemplate sqlSessionTemplate;
    protected T mapper;
    protected String sqlMapping;

    public BaseServiceImpl() {
    }

    @Autowired
    public void setMapper(T t) {
        this.sqlMapping = t.getClass().getInterfaces()[0].getName() + ".";
        this.mapper = t;
    }

//    public PageInfo<R> page(Map<String, ? extends Object> map) {
//        Page<Object> page = PageHelper.startPage(Integer.parseInt(map.get("pageNum").toString()), Integer.parseInt(map.get("pageSize").toString()));
//        if (map.get("order") != null && ((List)map.get("order")).size() > 0) {
//            Map m = (Map)((List)map.get("order")).get(0);
//            if (m.get("name") != null && m.get("dir") != null) {
//                page.setOrderBy(m.get("name") + " " + m.get("dir"));
//            }
//        }
//
//        String sql = String.format("%s%s", this.sqlMapping, map.get("mapping") == null ? "getPageObjList" : map.get("mapping"));
//        this.logger.debug("Execute {} param : {}", sql, map);
//        List<R> l = this.sqlSessionTemplate.selectList(sql, map);
//        return new PageInfo(l);
//    }

    public List<R> list(Map<String, ? extends Object> map) {
        String sql = String.format("%s%s", this.sqlMapping, map.get("mapping") == null ? "getObjList" : map.get("mapping"));
//        this.logger.debug("Execute {} param : {}", sql, map);
        List<R> list = this.sqlSessionTemplate.selectList(sql, map);
        return list;
    }

    public R get(Map<String, ? extends Object> map) {
        String sql = String.format("%s%s", this.sqlMapping, map.get("mapping") == null ? "getObjById" : map.get("mapping"));
//        this.logger.debug("Execute {} param : {}", sql, map);
        R r = this.sqlSessionTemplate.selectOne(sql, map);
        return r;
    }

    public boolean add(Map<String, ? extends Object> map) {
        String sql = String.format("%s%s", this.sqlMapping, map.get("mapping") == null ? "add" : map.get("mapping"));
//        this.logger.debug("Execute {} param : {}", sql, map);
        boolean r = this.sqlSessionTemplate.insert(sql, map) > 0;
        return r;
    }

    @Transactional
    public boolean addBatch(List<Map<String, Object>> list) {
        boolean result = false;

        Map map;
        String sql;
        for(Iterator var4 = list.iterator(); var4.hasNext(); result = this.sqlSessionTemplate.insert(sql, map) > 0 || result) {
            map = (Map)var4.next();
            sql = String.format("%s%s", this.sqlMapping, map.get("mapping") == null ? "add" : map.get("mapping"));
//            this.logger.debug("Execute {} param : {}", sql, map);
        }

        return result;
    }

    @Transactional
    public boolean addBatch(String mapping, Map<String, List> map) {
        String sql = String.format("%s%s", this.sqlMapping, mapping);
//        this.logger.debug("Execute {} param : {}", sql, map);
        boolean r = this.sqlSessionTemplate.insert(sql, map) > 0;
        return r;
    }

    public boolean update(Map<String, ? extends Object> map) {
        String sql = String.format("%s%s", this.sqlMapping, map.get("mapping") == null ? "update" : map.get("mapping"));
//        this.logger.debug("Execute {} param : {}", sql, map);
        boolean r = this.sqlSessionTemplate.update(sql, map) > 0;
        return r;
    }

    @Transactional
    public boolean updateBatch(List<Map<String, Object>> list) {
        boolean result = false;

        Map map;
        String sql;
        for(Iterator var4 = list.iterator(); var4.hasNext(); result = this.sqlSessionTemplate.update(sql, map) > 0 || result) {
            map = (Map)var4.next();
            sql = String.format("%s%s", this.sqlMapping, map.get("mapping") == null ? "update" : map.get("mapping"));
//            this.logger.debug("Execute {} param : {}", sql, map);
        }

        return result;
    }

    @Transactional
    public boolean delete(String ids, String mapping, String dialect) {
        boolean result = false;
        String[] var8;
        int var7 = (var8 = ids.split(",")).length;

        for(int var6 = 0; var6 < var7; ++var6) {
            String id = var8[var6];
            String sql = String.format("%s%s", this.sqlMapping, mapping);
//            this.logger.debug("Execute {} param : {}", sql, id);
            result = this.sqlSessionTemplate.delete(sql, id) > 0 || result;
        }

        return result;
    }

    @Transactional
    public boolean delete(String ids, String mapping) {
        boolean result = false;
        String[] var7;
        int var6 = (var7 = ids.split(",")).length;

        for(int var5 = 0; var5 < var6; ++var5) {
            String id = var7[var5];
            String sql = String.format("%s%s", this.sqlMapping, mapping);
//            this.logger.debug("Execute {} param : {}", sql, id);
            result = this.sqlSessionTemplate.delete(sql, id) > 0 || result;
        }

        return result;
    }

    public boolean deleteExt(Map<String, ? extends Object> map) {
        String sql = String.format("%s%s", this.sqlMapping, map.get("mapping"));
//        this.logger.debug("Execute {} param : {}", sql, map);
        return this.sqlSessionTemplate.delete(sql, map) > 0;
    }

    public boolean exist(Map<String, ? extends Object> map) {
        String sql = String.format("%s%s", this.sqlMapping, map.get("mapping") == null ? "exist" : map.get("mapping"));
//        this.logger.debug("Execute {} param : {}", sql, map);
        boolean r = this.sqlSessionTemplate.selectOne(sql, map) != null;
        return r;
    }

    public boolean toggle(Map<String, ? extends Object> map) {
        String sql = String.format("%s%s", this.sqlMapping, map.get("mapping") == null ? "toggle" : map.get("mapping"));
//        this.logger.debug("Execute {} param : {}", sql, map);
        boolean r = this.sqlSessionTemplate.update(sql, map) > 0;
        return r;
    }

}

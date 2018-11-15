package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public int findTotalCount(int cid,String rname) {
//        String sql = "select count(*) from tab_route where cid = ?";
        //定义初始sql
        String sql = "select count(*) from tab_route where 1 = 1";
        //定义一个StringBuilder动态拼接sql
        StringBuilder sb = new StringBuilder();
        sb.append(sql);
        List params = new ArrayList();//存储条件们
        //判断cid是否有值
        if ( cid != 0){
            //不为空则需要拼接查询条件
            sb.append(" and cid = ?");
            params.add(cid);//添加对应的值
        }
        //判断rname是否为空
        if (rname != null && !"".equals(rname)){
            //不为空则需要拼接查询条件
            sb.append(" and rname like ?");
            params.add("%"+rname+"%");
        }
        sql = sb.toString();
        return template.queryForObject(sql, Integer.class, params.toArray());
    }

    @Override
    public List<Route> findByPage(int cid, int start, int pageSize,String rname) {
       // String sql = "select * from tab_route where cid = ? limit ? , ?";
        //定义初始sql
        String sql = "select * from tab_route where 1 = 1";
        //定义一个StringBuilder动态拼接sql
        StringBuilder sb = new StringBuilder();
        sb.append(sql);
        List params = new ArrayList();//存储条件们
        //判断cid是否有值
        if ( cid != 0){
            //不为空则需要拼接查询条件
            sb.append(" and cid = ?");
            params.add(cid);//添加对应的值
        }
        //判断rname是否为空
        if (rname != null && !"".equals(rname)){
            //不为空则需要拼接查询条件
            sb.append(" and rname like ?");
            params.add("%"+rname+"%");
        }
        sb.append(" limit ? , ?");
        params.add(start);
        params.add(pageSize);
        sql = sb.toString();
        return template.query(sql,new BeanPropertyRowMapper<Route>(Route.class),params.toArray());
    }

    @Override
    public Route findOne(int rid) {
        String sql = "select * from tab_route where rid = ?";

        return template.queryForObject(sql,new BeanPropertyRowMapper<Route>(Route.class),rid);
    }
}

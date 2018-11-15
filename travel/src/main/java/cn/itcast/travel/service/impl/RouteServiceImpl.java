package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao dao = new RouteDaoImpl();
    private RouteImgDao routeImgDao = new RouteImgDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    /**
     * 分页方法
     * @param cid
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize ,String rname) {
        //封装PageBean
        PageBean<Route> pb = new PageBean<Route>();
        //设置当前页码
        pb.setCurrentPage(currentPage);
        //设置每页显示条数
        pb.setPageSize(pageSize);
        //获取总记录数
        int totalCount = dao.findTotalCount(cid,rname);
        //设置总记录数
        pb.setTotalCount(totalCount);
        int totalpage = (int) Math.ceil(totalCount * 1.0 / pageSize);
        //设置总页数
        pb.setTotalpage(totalpage);
        //设置当前页显示的数据集合
        int start = (currentPage - 1) * pageSize;//开始的记录数
        List<Route> list = dao.findByPage(cid,start,pageSize,rname);
        pb.setList(list);

        return pb;
    }

    @Override
    public Route findOneById(String rid) {
        //从RouteDao中查询route对象
        Route route = dao.findOne(Integer.parseInt(rid));
        //根据route对象的id 查询图片集合
        List<RouteImg> routeImgList = routeImgDao.findByRid(route.getRid());
        route.setRouteImgList(routeImgList);
        //根据route对象的sid 查询商家信息
        Seller seller = sellerDao.findBySid(route.getSid());
        route.setSeller(seller);
        //根据rid查询收藏次数
        int count = favoriteDao.findCountByRid(Integer.parseInt(rid));
        //设置收藏次数
        route.setCount(count);
        return route;
    }
}

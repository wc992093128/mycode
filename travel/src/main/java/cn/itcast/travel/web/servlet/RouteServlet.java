package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService service = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();
    /**
     * 分页查询
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");
        String rname = request.getParameter("rname");
        //加rname判断
        if ("null".equals(rname)){
            rname = "";
        }
        rname = new String(rname.getBytes("iso-8859-1"),"utf-8");
        int cid = 0;//类别id
        //处理参数
        if ( cidStr != null && cidStr.length() > 0 && !"null".equals(cidStr) ){

            cid = Integer.parseInt(cidStr);
        }
        int currentPage = 0;//当前页码，如果不传，则默认为第一页
        //处理参数
        if ( currentPageStr != null && currentPageStr.length() > 0 ){

            currentPage = Integer.parseInt(currentPageStr);
        }else {
            currentPage = 1;
        }
        int pageSize = 0;//每页显示条数，如果不传递，则默认显示5条
        //处理参数
        if ( pageSizeStr != null && pageSizeStr.length() > 0 ){

            pageSize = Integer.parseInt(currentPageStr);
        }else {
            pageSize = 5;
        }

        //调用Service查询PageBean对象
        PageBean<Route> pb = service.pageQuery(cid, currentPage, pageSize,rname);
        /*System.out.println(pb.getCurrentPage());*/
        //将PageBean对象序列化为Json，返回
        writeValue(pb,response);
    }

    /**
     * 查询商品详情的方法
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收参数
        String rid = request.getParameter("rid");
        //调用service查询Route对象
        Route route = service.findOneById(rid);
        //序列化route返回界面
        writeValue(route,response);
    }

    /**
     * 查询是否收藏
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收参数
        String rid = request.getParameter("rid");
        //从session中取出登录用户信息
        User user = (User) request.getSession().getAttribute("user");
        //判断用户是否登录
        int uid;
        if ( user == null ){
            //用户未登录,设置uid=0;
            uid = 0;
        }else {
            //用户已登录，设置uid
            uid = user.getUid();
        }
        //调用service查询是否已经收藏
        boolean flag = favoriteService.isFavorite(rid, uid);
        //回写到客户端
        writeValue(flag,response);
    }

    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取参数
        String rid = request.getParameter("rid");
        //从session中取出user对象
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        //判断user对象是否为空
        if ( user == null ){
            //用户未登录，不能添加收藏
            return;
        }else {
            //用户已登录
         uid = user.getUid();
        }
        //调用service添加收藏
        favoriteService.add(rid,uid);
    }
}

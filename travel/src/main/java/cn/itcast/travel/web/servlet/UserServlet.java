package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet{
    //声明UserService业务对象
    private UserService service = new UserServiceImpl();

    /**
     * 注册功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //校验验证码
        String check = request.getParameter("check");
        //取出正确的验证码
        String checkcode_server = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
        request.getSession().removeAttribute("CHECKCODE_SERVER");
        System.out.println(checkcode_server);
        //判断验证码是否正确
        if ( checkcode_server == null || !checkcode_server.equalsIgnoreCase(check) ){
            //验证码错误
            ResultInfo info = new ResultInfo();
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            //将info对象序列化为json
            writeValue(info,response);
            return;
        }

        //拿到请求中的数据
        Map<String, String[]> map = request.getParameterMap();
        //创建user实体类对象
        User user = new User();
        //调用BeanUtils封装对象
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //调用service完成注册
        boolean flag = service.register(user);
        //创建响应实例对象
        ResultInfo info = new ResultInfo();
        //判断是否注册成功
        if (flag) {
            //注册成功
            info.setFlag(true);
        } else {
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("注册失败");
        }

        //将数据回写到客户端
        //设置ContentType
        writeValue(info,response);
    }

    /**
     * 登录方法
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取验证码
        String check = request.getParameter("check");
        //拿到正确的验证码
        String checkcode_server = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
        ResultInfo info = new ResultInfo();
        ObjectMapper mapper = new ObjectMapper();
        //判断验证码是否为空
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)){
            //验证码错误
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            //将错误信息回写到客户端
            writeValue(info,response);
            return;
        }
        //获取请求数据
        Map<String, String[]> map = request.getParameterMap();
        //封装User对象
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //调用service查询
        UserService service = new UserServiceImpl();
        User u = service.login(user);

        //判断是否查询到数据
        if (u == null) {
            //没有查询到，登录失败，用户名或密码错误
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
        }
        if (u != null && !"Y".equals(u.getStatus())) {
            //尚未激活
            info.setFlag(false);
            info.setErrorMsg("您尚未激活，请先激活");
        }
        if (u != null && "Y".equals(u.getStatus())) {
            //登录成功
            request.getSession().setAttribute("user", u);
            info.setFlag(true);
        }
        //响应数据
       writeValue(info,response);
    }

    /**
     * 查询单个对象
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从session域中取出登录中存入的user
        Object user =  request.getSession().getAttribute("user");
        //将user写回客户端

       writeValue(user,response);
    }

    /**
     * 激活方法
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取请求信息
        String code = request.getParameter("code");
        if ( code != null ){
            //调用service完成激活
            UserService service = new UserServiceImpl();
            boolean flag = service.active(code);
            String msg = null;
            if(flag){
                //激活成功
                msg = "激活成功，请<a href='http://localhost:80/travel/login.html'>登录</a>";
            }else{
                //激活失败
                msg = "激活失败，请联系管理员!";
            }
            //响应数据
           writeValue(msg,response);
        }
    }

    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.销毁session
        request.getSession().invalidate();

        //2.跳转登录页面
        response.sendRedirect(request.getContextPath()+"/login.html");
    }
}

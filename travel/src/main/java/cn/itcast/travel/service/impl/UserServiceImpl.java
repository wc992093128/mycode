package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {
    private UserDao dao = new UserDaoImpl();
    /**
     * 用户注册的方法
     *
     * @param user
     * @return
     */
    @Override
    public boolean register(User user) {
        //调用dao查询用户名是否存在
        User u = dao.findByUsername(user.getUsername());
        //判断返回的user对象是否为空
        if ( u != null ){
            //用户名存在
           return false;
        }
        //用户名不存在，保存用户信息
        //设置激活码，唯一字符串
        user.setCode(UuidUtil.getUuid());
        //设置激活状态
        user.setStatus("N");
        dao.save(user);
        //发送激活邮件，邮件正文
        String content = "<a href='http://localhost/travel/user/active?code=" + user.getCode() + "'>点击激活【黑马旅游网】</a>";
        MailUtils.sendMail(user.getEmail(),content,"激活邮件");
        return true;
    }

    @Override
    public boolean active(String code) {
        //根据激活码查询用户
        User user = dao.findBycode(code);
        if (user != null) {
            //激活成功调用修改方法设置status状态
            dao.updateStatus(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User login(User user) {
        return dao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }
}

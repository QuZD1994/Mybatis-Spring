package cn.mybatisspring.test;

import cn.mybatisspring.dao.RoleDao;
import cn.mybatisspring.dao.UsersDao;
import cn.mybatisspring.entity.Role;
import cn.mybatisspring.entity.Users;
import cn.mybatisspring.service.UsersService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Client {

    public static void main(String[] args) {
        ApplicationContext cx = new ClassPathXmlApplicationContext("applicationContext.xml");
        /*UsersDao usersDao = (UsersDao) cx.getBean("usersDao");
        System.out.println(usersDao.getUserList().size());
        RoleDao roleDao = (RoleDao) cx.getBean("roleDao");
        Role role = roleDao.getRoleID((long) 1);
        System.out.println(role.getRoleName());*/

        UsersService usersService = (UsersService) cx.getBean("usersService");
        Users users = usersService.findUserByNamePwd("杨光", "123456");
        System.out.println("user = " + users);
        if (users == null){
            users.setId(new Long(9));
            users.setName("杨光");
            users.setPwd("123456");
            users.setRoleId(new Long(3));
            usersService.addUsers(users);
        }
        //usersService.addUsers(users);
        /*usersService.deleteUsers(users.getId());*/

    }
}

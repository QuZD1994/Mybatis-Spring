package cn.mybatisspring.service;

import cn.mybatisspring.dao.UsersDao;
import cn.mybatisspring.entity.Users;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class UsersServiceImpl implements UsersService {

    //依赖于dao 采用设值注入
    private UsersDao usersDao;

    public void setUsersDao(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    @Transactional(readOnly = true)
    @Override
    public Users findUserByNamePwd(String name, String pwd) {
        return usersDao.getUserByNamePwd(name, pwd);
    }
    @Transactional(readOnly = true)
    @Override
    public List<Users> findUsersByNameRID(String name, Integer rid) {
        return usersDao.getUsersByNameRID(name, rid);
    }
    @Transactional
    @Override
    public Integer addUsers(Users users) {
        if (users == null){
            return null;
        }
        //插入用户时先查用户在不在
        Users user = usersDao.getUserByName(users.getName());
        if (user == null){
            return usersDao.insert(users);
        }
        return null;
    }
    @Transactional
    @Override
    public Integer deleteUsers(Long uid) {
        return usersDao.delete(uid);
    }
}

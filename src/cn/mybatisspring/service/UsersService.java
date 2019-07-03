package cn.mybatisspring.service;

import cn.mybatisspring.entity.Users;

import java.util.List;

public interface UsersService {
    /**
     * 查询用户信息
     * @param name
     * @param pwd
     * @return
     */
    Users findUserByNamePwd(String name, String pwd);

    /**
     *  动态，分页查询用户列表
     * @param name
     * @param rid
     * @return
     */
    List<Users> findUsersByNameRID(String name, Integer rid);

    /**
     * 插入用户信息
     * @param users
     * @return
     */
    Integer addUsers(Users users);

    /**
     * 删除用户信息
     * @param uid
     * @return
     */
    Integer deleteUsers(Long uid);

}

package cn.mybatisspring.dao;

import cn.mybatisspring.entity.Users;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UsersDao {

    /**
     * 查询所有用户信息
     * @return
     */
    public List<Users> getUserList();

    /**
     * 查询用户信息
     * @param id
     * @return
     */
    Users getUsersByID(Long id);

    /**
     * sql语句需要传递两个参数是是要那个@Param
     * 查询用户信息
     * @param name
     * @param pwd
     * @return
     */
    Users getUserByNamePwd(@Param("name") String name, @Param("pwd") String pwd);

    /**
     * 根据用户名查询用户信息
     * @param name
     * @return
     */
    Users getUserByName(@Param("name") String name);

    /**
     * 多表查询
     * @param name
     * @param roleId
     * @return
     */
    List<Users> getUsersByNameRID(@Param("name") String name, @Param("roleId") Integer roleId);

    /**
     * 插入用户信息
     * @param users
     * @return
     */
    Integer insert(Users users);

    /**
     * 删除用户信息
     * @param id
     * @return
     */
    Integer delete(Long id);

    /**
     * 插入用户信息
     * @param users
     * @return
     */
    Integer update(Users users);
}

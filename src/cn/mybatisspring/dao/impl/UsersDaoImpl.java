/*
package cn.mybatisspring.dao.impl;

import cn.mybatisspring.Util.GetSession;
import cn.mybatisspring.dao.UsersDao;
import cn.mybatisspring.entity.Users;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.List;

public class UsersDaoImpl implements UsersDao {

    private SqlSession sqlSession = null;
    private SqlSessionTemplate sqlSessionTemplate;

    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    @Override
    public List<Users> getUserList() {
        return sqlSessionTemplate.selectList("cn.mybatisspring.dao.UsersDao.getUserList");
        //第二种使用mapper
        //return sqlSessionTemplate.getMapper(UsersDao.class).getUserList();
    }

    @Override
    public Users getUsersByID(Long id) {
        return sqlSessionTemplate.getMapper(UsersDao.class).getUsersByID(id);
    }

    @Override
    public Users getUserByNamePwd(String name, String pwd) {
        return null;
    }

    @Override
    public List<Users> getUsersByNameRID(String name, Integer roleId) {
        return null;
    }

    @Override
    public Integer insert(Users users) {
        return null;
    }

    @Override
    public Integer delete(Long id) {
        return null;
    }

    @Override
    public Integer update(Users users) {

        return null;
    }


}
*/

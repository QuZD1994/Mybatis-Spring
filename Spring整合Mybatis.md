# Spring整合Mybatis

## 整合思路：

1.Spring提供DI和AOP整合mybais框架，并且提供了核心接口，以便与Spring整合（Spring通过IOC，DI管理其他框架的对象）

spring IOC管理 **SqlSessionTemplate**  ,**SqlSessionFactoryBean**

**SqlSessionFactoryBean**：为整合应用提供Sqlsession对象资源

**SqlSessionTemplate:** 负责管理Mybatis的Sqlsession，调用SQL映射语句，实现对数据库的访问

**MapperFactoryBean:** 根据指定的Mapper接口生成Bean实例

**MapperScannerConfigurer:**根据指定包批量扫描Mapper接口并生成实例

## 整合步骤

### 1.导入两个框架所需jar包

### 2.编写实体类，dao接口和sql映射文件

Role
Users

RoleDao

```java
package cn.mybatisspring.dao;


import cn.mybatisspring.entity.Role;


public interface RoleDao {

    Role getRoleID(Long rid);
}

```

RoleDao-mapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<!--namespace：接口的全类名-->
<mapper namespace="cn.mybatisspring.dao.RoleDao">
    <select id="getRoleID" resultMap="roleResultMap">
        select r.*,u.*, u.id as uid from users u inner join role r on u.roleId = r.id where r.id = #{rid}
    </select>
    <!--角色的id和属性id，其他属性一样不用管自动映射-->
    <resultMap id="roleResultMap" type="cn.mybatisspring.entity.Role">
        <id column="id" property="id"/>
        <!--右边用户列表嵌套到role中：一对多
        property:Role中Users集合的属性名-->
        <collection property="usersList" ofType="cn.mybatisspring.entity.Users">
            <!--id和Role 的id 一样 需起别名-->
            <id column="uid" property="id"/>
        </collection>
    </resultMap>
</mapper>
```



UsersDao

```java
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

```



UsersDao-mapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<!--namespace：接口的全类名-->
<mapper namespace="cn.mybatisspring.dao.UsersDao">
    <select id="getUserList" resultType="cn.mybatisspring.entity.Users">
        select * from users
    </select>
    <select id="getUsersByID" resultType="cn.mybatisspring.entity.Users">
        select * from users where id = #{id}
    </select>
    <select id="getUserByNamePwd" resultType="cn.mybatisspring.entity.Users">
        select * from users where name = #{name} and pwd = #{pwd}
    </select>

    <select id="getUserByName" resultType="cn.mybatisspring.entity.Users">
        select * from users where name = #{name}
    </select>
    
    <select id="getUsersByNameRID" resultMap="userResult">
        select u.*, r.*, r.id as r_id from users u inner join role r on u.roleId = r.id where u.name = #{name} and r.id = #{roleId}
    </select>



    <resultMap id="userResult" type="cn.mybatisspring.entity.Users">
        <id column="id" property="id"/>
        <result column="roleName" property="roleName"/>
        <result column="name" property="name"/>
        <association property="role" javaType="cn.mybatisspring.entity.Role">
            <id column="r_id" property="id"/>
        </association>
        <!--<collection property=""/>-->
    </resultMap>

    <insert id="insert" parameterType="cn.mybatisspring.entity.Users">
        insert into users(id, name, pwd, roleId) values (#{id} , #{name}, #{pwd}, #{roleId});
    </insert>

    <delete id="delete">
        delete from users where id = #{id}
    </delete>

    <update id="update">
        update users set name = #{name}, roleId = #{roleId} where id = #{id}
    </update>
</mapper>
```

### 3.编写Dao接口实现类（其中一种方式）

```java
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
```

### 4.使用数据映射器，数据映射器扫描类完成dao接口实现bean实例

```xml
 <!--定义SqlSessionFactoryBean-->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">

        <property name="dataSource" ref="dataSource"/>
        <!--加载mybatis配置文件-->
        <property name="configLocation">
            <value>classpath:mybatis-config.xml</value>
        </property>
        <!--加载SQL映射文件-->
        <property name="mapperLocations">
            <list>
                <value>classpath:cn/mybatisspring/dao/RoleDao-mapper.xml</value>
                <value>classpath:cn/mybatisspring/dao/UsersDao-mapper.xml</value>
            </list>
        </property>
    </bean>
    <!--定义SqlSessionTemplate 实例-->
    <bean id="sqlSessionTemplate" class="org.mybatis.spring.SqlSessionTemplate">
        <constructor-arg ref="sqlSessionFactory"/>
    </bean>
```



#### dao接口实现类不用编写采用xml方式

```xml
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">

        <property name="dataSource" ref="dataSource"/>
        <!--加载mybatis配置文件-->
        <property name="configLocation">
            <value>classpath:mybatis-config.xml</value>
        </property>
        <!--加载SQL映射文件-->
        <property name="mapperLocations">
            <list>
                <value>classpath:cn/mybatisspring/dao/RoleDao-mapper.xml</value>
                <value>classpath:cn/mybatisspring/dao/UsersDao-mapper.xml</value>
            </list>
        </property>
    </bean>
<bean id="sqlSessionTemplate" class="org.mybatis.spring.SqlSessionTemplate">
        <constructor-arg ref="sqlSessionFactory"/>
    </bean>
<!--定义数据映射器 将某包下的dao接口生成bean实例,RoleDao无实现类-->
    <bean id="roleDao" class="org.mybatis.spring.mapper.MapperFactoryBean">
        <property name="mapperInterface" value="cn.mybatisspring.dao.RoleDao"/>
        <!--<property name="sqlSessionTemplate" ref="sqlSessionTemplate"/>-->
        <property name="sqlSessionFactory" ref="sqlSessionFactory"/>
    </bean>
```

#### 整个包扫描

```xml
<!--定义数据映射器扫描类，将某包下的所有dao接口生成bean实例
    bean id叫什么？
    dao接口类的首字母小写roleDao, usersDao
    value：sqlSessionFactory bean的id
    -->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="cn.mybatisspring.dao"/>
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
    </bean>

    <!--
    ref="usersDao" ：为映射器扫描类自动生成的
    -->
    <bean id="usersService" class="cn.mybatisspring.service.UsersServiceImpl">
        <property name="usersDao" ref="usersDao"/>
    </bean>
 <!--定义SqlSessionFactoryBean-->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">

        <property name="dataSource" ref="dataSource"/>
        <!--加载mybatis配置文件-->
        <property name="configLocation">
            <value>classpath:mybatis-config.xml</value>
        </property>
        <!--加载SQL映射文件-->
        <property name="mapperLocations">
            <list>
                <value>classpath:cn/mybatisspring/dao/RoleDao-mapper.xml</value>
                <value>classpath:cn/mybatisspring/dao/UsersDao-mapper.xml</value>
            </list>
        </property>
    </bean>
    <!--定义SqlSessionTemplate 实例-->
    <bean id="sqlSessionTemplate" class="org.mybatis.spring.SqlSessionTemplate">
        <constructor-arg ref="sqlSessionFactory"/>
    </bean>
```

### 5.测试

```java
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
```



# 声明式事务

为什么使用

1.没有硬编码繁琐、易于维护

2.事务处理需要业务逻辑层解决

关注业务逻辑层方法采用什么事务策略，增删改采用能读能写策略，查询采用只读策略

如何使用

## 1.xml方式

1.引入aop tx命名空间

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:p="http://www.springframework.org/schema/p"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/aop
       http://www.springframework.org/schema/aop/spring-aop-4.0.xsd
       http://www.springframework.org/schema/tx
       http://www.springframework.org/schema/tx/spring-tx-4.0.xsd">

   <!--2.定义事务管理器 对数据源进行管理-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>
    <!--3.定义事务增强 配置事务规则-->
    <tx:advice id="txAdvice" transaction-manager="transactionManager">
        <tx:attributes>
            <!--对find开头的方法只读 。。。。-->
            <tx:method name="find*" read-only="true"/>
            <tx:method name="add*" read-only="false"/>
            <tx:method name="delete*" read-only="false"/>
            <tx:method name="update*" read-only="false"/>
        </tx:attributes>
    </tx:advice>
    <!--4.Spring aop配置-->
    <aop:config>
        <aop:pointcut id="poingcut" expression="execution(* cn.mybatisspring.service..*.*(..))"/>
        <!--织入：将事务增强和切入点关联起来-->
        <aop:advisor advice-ref="txAdvice" pointcut-ref="poingcut"/>
    </aop:config>
</beans>
```

## 2.注解方式

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:p="http://www.springframework.org/schema/p"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/aop
       http://www.springframework.org/schema/aop/spring-aop-4.0.xsd
       http://www.springframework.org/schema/tx
       http://www.springframework.org/schema/tx/spring-tx-4.0.xsd">

   <!--2.定义事务管理器 对数据源进行管理-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>
    <!--3.添加注解事务支持-->
    <tx:annotation-driven transaction-manager="transactionManager"/>
</beans>
```

在类中必须使用@Transactional注解元素进行标识

```java
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
```


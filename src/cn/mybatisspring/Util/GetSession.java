package cn.mybatisspring.Util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class GetSession {

    private static final String CONFIG = "mybatis-config.xml";
    private static SqlSessionFactory sqlSessionFactory = null;

    /**
     * 利用静态代码块：sqlSessionFactory之初始化一次
     * 静态代码块之初始化一次
     */
    static {
        InputStream inputStream = null;
        //1.加载mybatis配置文件
        try {
            inputStream = Resources.getResourceAsStream(CONFIG);
            //2.创建sqlSessionFactory对象
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static SqlSession getSqlSession(){
        return sqlSessionFactory.openSession();
    }
    public static void close(SqlSession sqlSession){
        if (sqlSession != null){
            sqlSession.close();
        }
    }
}

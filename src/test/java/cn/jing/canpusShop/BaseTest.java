package cn.jing.canpusShop;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * function:单元测试基类，用来配置Spring和junit整合，junit启动时加载Spring IOC容器。
 * ／／@RunWith注解用来告诉junit，spring是用哪个类来跑单元测试的。（spring与junit整合）
 * 
 * @author liangjing
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit，spring配置文件的位置，则将会被自动载入
@ContextConfiguration({ "classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml",
		"classpath:spring/spring-redis.xml" })
public class BaseTest {

}

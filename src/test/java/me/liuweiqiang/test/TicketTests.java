package me.liuweiqiang.test;

//import me.liuweiqiang.rmi.exception.TicketException;
import me.liuweiqiang.test.exception.TicketException;
import me.liuweiqiang.test.bean.HessianBean;
import me.liuweiqiang.test.rmi.TicketServiceEx;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

//SpringBootTest提供了比ContextConfiguration更多功能
//SpringBootTest默认提供模拟的环境，指定定义或者随机端口会提供真正的Web环境，这里指定随机端口
//导入测试配置
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(HessianBean.class)
public class TicketTests {

    @Autowired
    @Qualifier("hessianProxyFactoryBean")
    private TicketServiceEx ticketServiceEx1; //hessian和xfire提供了同样的服务，可以通过Qualifier匹配相应FactoryBean提供的服务

    @Autowired
    @Qualifier("xFireClientFactoryBean")
    private TicketServiceEx ticketServiceEx2;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void test001() {
        Assert.assertEquals(
                "Qiang", ticketServiceEx1.requestTicket("", "", "", "")
        );
    }

    @Test
    public void test002() {
        try {
            ticketServiceEx1.verifyTicket("", "", "", "", "");
//        } catch (ClassNotFoundException e) {
        } catch (TicketException e) {
            Assert.fail("Hessian wouldn't throw exception in other package");
            //这里按接口定义的捕获TicketException，但接口声明的异常和服务端的异常不在同一个包
            // 所以Hessian抛的是RemoteAccessException
        } catch (RemoteAccessException e) {
//            System.out.println("RemoteAccessException cought");
            logger.info("RemoteAccessException cought");

        }
    }

    @Test
    public void xfiretest003() {
        Assert.assertEquals(
                "Qiang", ticketServiceEx2.requestTicket("", "", "", "")
        );
    }

    @Test
    public void xfiretest004() {
        try {
            ticketServiceEx2.verifyTicket("", "", "", "", "");
//        } catch (ClassNotFoundException e) {
        } catch (TicketException e) {
//            System.out.println("TicketException cought");
            logger.info("TicketException cought");
            //与Hessian不同，XFire会抛出TicketException，即使与服务端的异常不在同一个包
        }
    }

    @Test
    public void xfiretest005() {
        try {
            ticketServiceEx2.verifyTicket("1", "", "", "", "");
//        } catch (ClassNotFoundException e) {
        } catch (TicketException e) {
            Assert.fail("Should catch RuntimeException when \"1\".equals(ticket)");
        } catch (RuntimeException e) {
//            System.out.println("RuntimeException");
            logger.info("RuntimeException cought");
        }
    }
}

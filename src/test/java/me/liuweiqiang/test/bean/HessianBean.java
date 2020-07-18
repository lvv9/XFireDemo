package me.liuweiqiang.test.bean;

import me.liuweiqiang.test.rmi.TicketServiceEx;
import org.codehaus.xfire.spring.remoting.XFireClientFactoryBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;

//测试配置
//这里加上Lazy可能是因为启动的问题，不然端口注入不了
@Configuration
@Lazy
public class HessianBean {

    @LocalServerPort
    private int port; //注入SpringBootTest随机端口

    @Bean
    public HessianProxyFactoryBean hessianProxyFactoryBean() {
        HessianProxyFactoryBean hessianProxyFactoryBean = new HessianProxyFactoryBean();
        hessianProxyFactoryBean.setServiceUrl("http://localhost:" + port + "/ticketservice");
        hessianProxyFactoryBean.setServiceInterface(TicketServiceEx.class);
        return hessianProxyFactoryBean;
    }

    @Bean
    public XFireClientFactoryBean xFireClientFactoryBean() {
        XFireClientFactoryBean xFireClientFactoryBean = new XFireClientFactoryBean();
        xFireClientFactoryBean.setServiceClass(TicketServiceEx.class);
        xFireClientFactoryBean.setWsdlDocumentUrl("http://localhost:" + port + "/TicketServiceEx?wsdl");
        xFireClientFactoryBean.setLookupServiceOnStartup(false); //启动式不查找服务，也是可能因为启动的问题需要在
                                                                //SpringMVC加载完真正访问服务的时候才查找
        return xFireClientFactoryBean;
    }
}

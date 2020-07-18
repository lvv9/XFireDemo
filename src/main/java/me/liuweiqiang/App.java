package me.liuweiqiang;

import me.liuweiqiang.rmi.TicketServiceEx;
import org.codehaus.xfire.XFireFactory;
import org.codehaus.xfire.spring.remoting.XFireExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.caucho.HessianServiceExporter;

@SpringBootApplication
public class App {

    private TicketServiceEx ticketServiceEx;

    //注入服务实现
    @Autowired
    public void setTicketServiceEx(TicketServiceEx ticketServiceEx) {
        this.ticketServiceEx = ticketServiceEx;
    }

    //通过BeanName暴露服务
    @Bean("/ticketservice")
    public HessianServiceExporter hessianServiceExporter() {
        HessianServiceExporter hessianServiceExporter = new HessianServiceExporter();
        hessianServiceExporter.setService(ticketServiceEx);
        hessianServiceExporter.setServiceInterface(TicketServiceEx.class);
        return hessianServiceExporter;
    }

    @Bean("/TicketServiceEx")
    public XFireExporter xFireExporter() throws Exception {
        //
        XFireExporter xFireExporter = new XFireExporter();
        xFireExporter.setServiceInterface(TicketServiceEx.class);
        xFireExporter.setServiceBean(ticketServiceEx);
        xFireExporter.setXfire(XFireFactory.newInstance().getXFire());
        xFireExporter.afterPropertiesSet();
        //这一段是摘抄的，通过xml方式注入的话因为XFire太旧了会报异常，官网上不了也找不到什么资料
        return xFireExporter;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args); //加载上下文
    }
}

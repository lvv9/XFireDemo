package me.liuweiqiang;

import me.liuweiqiang.rmi.TicketServiceEx;
import org.codehaus.xfire.DefaultXFire;
import org.codehaus.xfire.XFire;
import org.codehaus.xfire.aegis.AegisBindingProvider;
import org.codehaus.xfire.aegis.type.DefaultTypeMappingRegistry;
import org.codehaus.xfire.aegis.type.TypeMappingRegistry;
import org.codehaus.xfire.service.DefaultServiceRegistry;
import org.codehaus.xfire.service.ServiceRegistry;
import org.codehaus.xfire.service.binding.MessageBindingProvider;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;
import org.codehaus.xfire.spring.remoting.XFireExporter;
import org.codehaus.xfire.transport.DefaultTransportManager;
import org.codehaus.xfire.transport.TransportManager;
import org.codehaus.xfire.transport.http.XFireServletController;
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
    public XFireExporter xFireExporter(XFire xFire) {
        XFireExporter xFireExporter = new XFireExporter();
        xFireExporter.setServiceInterface(TicketServiceEx.class);
        xFireExporter.setServiceBean(ticketServiceEx);
//        xFireExporter.setXfire(XFireFactory.newInstance().getXFire());
//        xFireExporter.afterPropertiesSet();
        //这一段是摘抄的，通过xml方式注入的话因为XFire太旧了会报异常，官网上不了也找不到什么资料
        xFireExporter.setXfire(xFire);
        return xFireExporter;
    }

    @Bean("xfire")
    public DefaultXFire xFire(ServiceRegistry serviceRegistry, TransportManager transportManager) {
        return new DefaultXFire(serviceRegistry, transportManager);
    }

    @Bean(value = "xfire.transportManager",
    initMethod = "initialize",
    destroyMethod = "dispose")
    public DefaultTransportManager transportManager() {
        return new DefaultTransportManager();
    }

    @Bean("xfire.serviceRegistry")
    public DefaultServiceRegistry serviceRegistry() {
        return new DefaultServiceRegistry();
    }

    @Bean(value = "xfire.typeMappingRegistry", initMethod = "createDefaultMappings")
    public DefaultTypeMappingRegistry typeMappingRegistry() {
        return new DefaultTypeMappingRegistry();
    }

    @Bean("xfire.aegisBindingProvider")
    public AegisBindingProvider aegisBindingProvider(TypeMappingRegistry typeMappingRegistry) {
        return new AegisBindingProvider(typeMappingRegistry);
    }

    @Bean("xfire.serviceFactory")
    public ObjectServiceFactory serviceFactory(TransportManager transportManager, AegisBindingProvider aegisBindingProvider) {
        return new ObjectServiceFactory(transportManager, aegisBindingProvider);
    }

    @Bean("xfire.servletController")
    public XFireServletController xFireServletController(XFire xFire) {
        return new XFireServletController(xFire);
    }

    @Bean("xfire.messageServiceFactory")
    public ObjectServiceFactory objectServiceFactory(TransportManager transportManager,
                                                     MessageBindingProvider messageBindingProvider) {
        ObjectServiceFactory objectServiceFactory = new ObjectServiceFactory(transportManager, messageBindingProvider);
        objectServiceFactory.setStyle("message");
        return objectServiceFactory;
    }

    @Bean("xfire.messageBindingProvider")
    public MessageBindingProvider messageBindingProvider() {
        return new MessageBindingProvider();
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args); //加载上下文
    }
}

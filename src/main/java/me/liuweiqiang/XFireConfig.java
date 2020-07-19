package me.liuweiqiang;

import org.codehaus.xfire.DefaultXFire;
import org.codehaus.xfire.XFire;
import org.codehaus.xfire.aegis.AegisBindingProvider;
import org.codehaus.xfire.aegis.type.DefaultTypeMappingRegistry;
import org.codehaus.xfire.aegis.type.TypeMappingRegistry;
import org.codehaus.xfire.service.DefaultServiceRegistry;
import org.codehaus.xfire.service.ServiceRegistry;
import org.codehaus.xfire.service.binding.MessageBindingProvider;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;
import org.codehaus.xfire.transport.DefaultTransportManager;
import org.codehaus.xfire.transport.TransportManager;
import org.codehaus.xfire.transport.http.XFireServletController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XFireConfig {

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
}

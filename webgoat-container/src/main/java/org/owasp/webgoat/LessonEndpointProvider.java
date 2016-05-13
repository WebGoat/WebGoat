package org.owasp.webgoat;

import org.owasp.webgoat.lessons.LessonEndpointMapping;
import org.owasp.webgoat.plugins.PluginClassLoader;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoint;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Map;

/**
 * Each lesson can define an endpoint which can support the lesson. So for example if you create a lesson which uses JavaScript and
 * needs to call out to the server to fetch data you can define an endpoint in that lesson. WebGoat will pick up this endpoint and
 * Spring will publish it.
 * <p/>
 * Find all the defined endpoints in the lessons and register those endpoints in the Spring context so later on the
 * Actuator will pick them up and expose them as real endpoints.
 * <p/>
 * We use the Actuator here so we don't have to do all the hard work ourselves (endpoint strategy pattern etc) so in a
 * lesson you can just define a subclass of LessonEndpoint which this class will publish as an endpoint. So we can
 * dynamically load endpoints from our plugins.
 */
public class LessonEndpointProvider {

    private final String pluginBasePackage;
    private final ApplicationContext parentContext;
    private final PluginClassLoader classLoader;
    private ListableBeanFactory context;
    private DefaultListableBeanFactory providedBeans;
    private BeanFactory beanFactory;


    public LessonEndpointProvider(String pluginBasePackage, ApplicationContext parentContext, BeanFactory beanFactory, PluginClassLoader cl) {
        this.pluginBasePackage = pluginBasePackage;
        this.parentContext = parentContext;
        this.providedBeans = new DefaultListableBeanFactory(this.parentContext.getParentBeanFactory());
        this.beanFactory = beanFactory;
        this.classLoader = cl;
    }

    public void registerEndpoints() {
        if (context == null) {
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
            context.setParent(parentContext);
            context.setClassLoader(classLoader);

            ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(context, false);
            scanner.addIncludeFilter(new AnnotationTypeFilter(LessonEndpointMapping.class));
            scanner.scan(pluginBasePackage);
            context.refresh();

            Map<String, MvcEndpoint> beansOfType = context.getBeansOfType(MvcEndpoint.class);
            ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
            beansOfType.forEach((k, v) -> {
                configurableBeanFactory.registerSingleton(k, v);
            });
            this.context = context;
        }
    }
}

package com.innowise.candidatemodule.configuration;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class EmbeddedTomcatConfig implements WebMvcConfigurer {

    @Value("${http.port}")
    private int httpPort;

    @Value("${server.port}")
    private int serverPort;

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> customizeTomcatConnector() {
        return factory -> {
            Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
            connector.setPort(httpPort);
            connector.setSecure(false);
            connector.setScheme("http");
            connector.setRedirectPort(serverPort);
            factory.addAdditionalTomcatConnectors(connector);
        };
    }

    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addViewController("/{spring:[\\w\\-]+}").setViewName("forward:/");
        registry.addViewController("/**/{spring:[\\w\\-]+}").setViewName("forward:/");
    }
}

package ar.edu.itba.paw.webapp.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Locale;
import java.util.Properties;

@EnableWebMvc
@ComponentScan({ "ar.edu.itba.paw.webapp.controller", "ar.edu.itba.paw.service", "ar.edu.itba.paw.persistence", "ar.edu.itba.paw.webapp.component", "ar.edu.itba.paw.webapp.config"})
@Configuration
@EnableCaching
@EnableAsync
@EnableTransactionManagement
@PropertySource(value = "classpath:application.properties")
public class WebConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebConfig.class);

    public static final int MAX_UPLOAD_SIZE = 2097152; // 2 MB
    public static final int MAX_SLIDESHOW_COUNT = 5;


    @Autowired
    private Environment env;


    /**
     * Entity manager for Hibernate & JPA factory.
     * @return The created entity manager.
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPackagesToScan("ar.edu.itba.paw.model");
        factoryBean.setDataSource(dataSource());

        final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(vendorAdapter);

        final Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL92Dialect");
        if (isDevelopmentMode()) {
            properties.setProperty("hibernate.show_sql", "true");
            properties.setProperty("format_sql", "true");
        }

        factoryBean.setJpaProperties(properties);
        return factoryBean;
    }


    /**
     * Creates the transaction manager for the repository.
     * @param entityManagerFactory The corresponding entity manager.
     * @return The created transaction manager.
     */
    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }


    /**
     * Sets up postgres driver data source.
     * @return The data source.
     */
    @Bean
    public DataSource dataSource() {
        final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.postgresql.Driver.class);
        dataSource.setUrl(env.getProperty("postgres.url"));
        dataSource.setUsername(env.getProperty("postgres.username"));
        dataSource.setPassword(env.getProperty("postgres.password"));

        LOGGER.debug("Connecting to database at {} with user {}", dataSource.getUrl(), dataSource.getUsername());

        return dataSource;
    }


    /**
     * Set message source for i18n.
     * @return The message resource initialized.
     */
    @Bean
    public MessageSource messageSource() {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:i18n/messages", "classpath:i18n/emailMessages");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
        messageSource.setCacheSeconds(5);
        return messageSource;
    }


    /**
     * Resolver for the image max size.
     * @return Multipart Resolver.
     */
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setMaxUploadSize(MAX_UPLOAD_SIZE);
        return commonsMultipartResolver;
    }


    /**
     * Bean for mail server configuration
     * @return
     */
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getProperty("mail.host"));
        mailSender.setPort(Integer.parseInt(env.getProperty("mail.port")));
        mailSender.setUsername(env.getProperty("mail.username"));
        mailSender.setPassword(env.getProperty("mail.password"));
        mailSender.setDefaultEncoding("UTF-8");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.from.email", env.getProperty("mail.username"));
        if (isDevelopmentMode()) props.put("mail.debug", "true");

        return mailSender;
    }


    /**
     * Bean for velocity set up.
     * @return The created velocity engine.
     * @throws VelocityException
     * @throws IOException
     */
    @Bean
    public VelocityEngine velocityEngine() {
        Properties properties = new Properties();
        properties.setProperty("input.encoding", "UTF-8");
        properties.setProperty("output.encoding", "UTF-8");
        properties.setProperty("resource.loader", "class");
        properties.setProperty("exposeSpringMacroHelpers", "true");
        properties.setProperty("velocimacro.library", "macro-library.vm");
        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        return new VelocityEngine(properties);
    }


    /**
     * Creates a cache manager. Used only to store all categories (they do not change)
     * @return The created cache manager.
     */
    @Bean
    CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Collections.singletonList(new ConcurrentMapCache("allCategories")));
        return cacheManager;
    }


    /**
     * The object mapper for data binding.
     * @return The created object mapper.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);

        return mapper;
    }


    /** Auxiliary Method */


    /**
     * Use LOGGER debug mode for switching development/production mode respectively.
     * @return True for development mode, false if production.
     */
    private boolean isDevelopmentMode() {
        return LOGGER.isDebugEnabled();
    }
}

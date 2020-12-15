package ar.edu.itba.paw.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hsqldb.jdbc.JDBCDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@ComponentScan({ "ar.edu.itba.paw.persistence" })
@EnableTransactionManagement
@Configuration
public class TestConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestConfig.class);

    @Value("classpath:schema.sql")
    private Resource schemaSql;

    /**
     * Data source creator. Stored in memory, does not bring
     * up a real database connection.
     * @return The created data source.
     */
    @Bean
    public DataSource dataSource() {
        final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(JDBCDriver.class);
        dataSource.setUrl("jdbc:hsqldb:mem:paw");
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        return dataSource;
    }

    /**
     * Entity Manager creator.
     * @return The created entity manager.
     */
    @Bean
    @DependsOn("dbInitializer")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPackagesToScan("ar.edu.itba.paw.model");
        factoryBean.setDataSource(dataSource());

        final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(vendorAdapter);

        final Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");

        if (!isDevelopmentMode()) {
            properties.setProperty("hibernate.show_sql", "true");
            properties.setProperty("format_sql", "true");
        }

        factoryBean.setJpaProperties(properties);
        return factoryBean;
    }


    /**
     * The creator of a transaction manager.
     * @param entityManagerFactory The related entity manager.
     * @return The created platform transaction manager.
     */
    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }


    /**
     * Data source initializer for test.
     * @param ds The data source to initialize.
     * @return The data source initializer.
     * Entity Manager creator.
     * @return The created entity manager.
     */
    @Bean(name = "dbInitializer")
    public DataSourceInitializer dataSourceInitializer(final DataSource ds) {
        final DataSourceInitializer dsi = new DataSourceInitializer();
        dsi.setDataSource(ds);
        dsi.setDatabasePopulator(databasePopulator());
        return dsi;
    }

    /**
     * Data base populator function.
     * @return The data base populator linked to schema.sql;
     */
    private DatabasePopulator databasePopulator() {
        final ResourceDatabasePopulator dbp = new ResourceDatabasePopulator();
        dbp.addScript(schemaSql);
        return dbp;
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
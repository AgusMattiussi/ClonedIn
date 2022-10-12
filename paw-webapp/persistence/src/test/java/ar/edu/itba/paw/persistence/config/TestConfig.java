package ar.edu.itba.paw.persistence.config;

import org.hsqldb.jdbc.JDBCDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
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
@Configuration
@EnableTransactionManagement
public class TestConfig {

    /*@Value("classpath:hsqldb.sql")
    private Resource hsqldbSql;
    @Value("classpath:schema.sql")
    private Resource schemaSql;
    @Value("classpath:init.sql")
    private Resource initSql;
    @Value("classpath:category.sql")
    private Resource categorySql;

    @Bean
    public DataSource dataSource(){
        final SimpleDriverDataSource ds = new SimpleDriverDataSource();

        ds.setDriverClass(JDBCDriver.class);
        ds.setUrl("jdbc:hsqldb:mem:paw");
        ds.setUsername("ha");
        ds.setPassword("");

        return ds;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPackagesToScan("ar.edu.itba.model");
        factoryBean.setDataSource(dataSource());
        final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        final Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL92Dialect");
        // Si ponen esto en prod, hay tabla!!!
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("format_sql", "true");
        factoryBean.setJpaProperties(properties);
        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer(final DataSource ds){
        final DataSourceInitializer dsInit = new DataSourceInitializer();

        dsInit.setDataSource(ds);
        dsInit.setDatabasePopulator(dataSourcePopulator());

        return dsInit;
    }

    private DatabasePopulator dataSourcePopulator() {
        final ResourceDatabasePopulator dbp = new ResourceDatabasePopulator();

        dbp.addScript(hsqldbSql);
        dbp.addScript(schemaSql);
        dbp.addScript(initSql);
         //Puedo agregar script de inserts iniciales para la DB

        return dbp;
    }*/

}

package ar.edu.itba.paw.webapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.sql.DataSource;

@EnableWebMvc
@ComponentScan({ "ar.edu.itba.paw.webapp.controller", "ar.edu.itba.paw.services", "ar.edu.itba.paw.persistence" })
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Value("classpath:schema.sql")
    private Resource schemaSql;
    @Bean
    public ViewResolver viewResolver() {
        final InternalResourceViewResolver vr = new InternalResourceViewResolver();

        vr.setViewClass(JstlView.class);
        vr.setPrefix("/WEB-INF/views/");
        vr.setSuffix(".jsp");

        return vr;
    }

    @Bean
    public DataSource dataSource(){
        final SimpleDriverDataSource ds = new SimpleDriverDataSource();

        ds.setDriverClass(org.postgresql.Driver.class);
        ds.setUrl("jdbc:postgresql://localhost/paw");
        ds.setUsername("postgres");
        ds.setPassword("admin");

        return ds;
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

        dbp.addScript(schemaSql);

        return dbp;
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("/assets/css")
                .addResourceLocations("/assets/images")
                .addResourceLocations("/assets/js");
    }
}

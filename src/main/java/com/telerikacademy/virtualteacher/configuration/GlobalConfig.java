package com.telerikacademy.virtualteacher.configuration;


import com.telerikacademy.virtualteacher.dtos.response.CourseRatingResponseDTO;
import com.telerikacademy.virtualteacher.models.CourseRating;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories("com.telerikacademy.virtualteacher.repositories")
@PropertySource("classpath:application.properties")
@EnableTransactionManagement

public class GlobalConfig {
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;
    private String dbDriver;

    @Autowired
    public GlobalConfig(Environment environment) {
        dbUrl = environment.getProperty("spring.datasource.url");
        dbUsername = environment.getProperty("spring.datasource.username");
        dbPassword = environment.getProperty("spring.datasource.password");
        dbDriver = environment.getProperty("spring.datasource.driver-class-name");
    }

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .username(dbUsername)
                .password(dbPassword)
                .url(dbUrl)
                .driverClassName(dbDriver)
                .build();
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.telerikacademy.virtualteacher.models");
        factory.setDataSource(dataSource());
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        return txManager;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new PropertyMap<CourseRating, CourseRatingResponseDTO>() {
            @Override
            protected void configure() {
                map().setUser(source.getUser().getId());
                map().setCourse(source.getCourse().getId());
                map().setRating(source.getRating());
            }
        });

        return modelMapper;
    }
}

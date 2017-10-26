package com.vs.BaseProject;

import java.util.Properties;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.web.ErrorPageFilter;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableTransactionManagement
@EnableSwagger2
public class App extends SpringBootServletInitializer
{
   
	  private static final Logger LOGGER = LoggerFactory.getLogger(App.class.getName());
	  
	  @Value("${db.driver}")
	  private String DB_DRIVER;
	  
	  @Value("${db.password}")
	  private String DB_PASSWORD;
	  
	  @Value("${db.url}")
	  private String DB_URL;
	  
	  @Value("${db.username}")
	  private String DB_USERNAME;

	  @Value("${hibernate.dialect}")
	  private String HIBERNATE_DIALECT;
	  
	  @Value("${hibernate.show_sql}")
	  private String HIBERNATE_SHOW_SQL;
	  
	  @Value("${hibernate.hbm2ddl.auto}")
	  private String HIBERNATE_HBM2DDL_AUTO;

	  @Value("${entitymanager.packagesToScan}")
	  private String ENTITYMANAGER_PACKAGES_TO_SCAN;

	  @Bean
	  public Docket api() {
    	return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())  
			        .select()                                  
			        .apis(RequestHandlerSelectors.any())  
			        .paths(PathSelectors.any())                          
			        .build();  
	  }
     
	  private ApiInfo apiInfo() {
		  return new ApiInfoBuilder().title("Spring REST Sample with Swagger")
					.description("Spring REST Sample with Swagger")
					.termsOfServiceUrl("http://www.vectoscalar.com")
					.contact("Dimple")
					.version("0.0").build();
	  }	
    
	  
		@Bean
		public DataSource dataSource() {
			DriverManagerDataSource dataSource = new DriverManagerDataSource();
			dataSource.setDriverClassName(DB_DRIVER);
			dataSource.setUrl(DB_URL);
			dataSource.setUsername(DB_USERNAME);
			dataSource.setPassword(DB_PASSWORD);
			return (DataSource) dataSource;
		}

		@Bean
		public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws NamingException {
			LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
			factoryBean.setDataSource(dataSource());
			factoryBean.setPackagesToScan(ENTITYMANAGER_PACKAGES_TO_SCAN);
			factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
			factoryBean.setJpaProperties(jpaProperties());
			return factoryBean;
		}
		
		@Bean
		public AmazonS3 getS3Client(){
			ClasspathPropertiesFileCredentialsProvider provider =  getProvider();
			AmazonS3ClientBuilder s3ClientBuilder = AmazonS3ClientBuilder.standard();
			s3ClientBuilder.setCredentials(provider);
			s3ClientBuilder.setRegion(Region.getRegion(Regions.US_EAST_1).toString());
			return s3ClientBuilder.build();
		}
		
		public ClasspathPropertiesFileCredentialsProvider getProvider(){
			return new ClasspathPropertiesFileCredentialsProvider();
		}

		@Bean
		public JpaVendorAdapter jpaVendorAdapter() {
			HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
			return hibernateJpaVendorAdapter;
		}

		public Properties jpaProperties() {
			Properties properties = new Properties();
			properties.put("hibernate.dialect", HIBERNATE_DIALECT);
			properties.put("hibernate.hbm2ddl.auto", HIBERNATE_HBM2DDL_AUTO);
			properties.put("hibernate.show_sql", HIBERNATE_SHOW_SQL);
			return properties;
		}

		@Bean
		@Autowired
		public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
			JpaTransactionManager txManager = new JpaTransactionManager();
			txManager.setEntityManagerFactory(emf);
			return txManager;
		}
    
		@Bean
		public ErrorPageFilter errorPageFilter() {
			return new ErrorPageFilter();
		}

		@Bean
		public FilterRegistrationBean disableSpringBootErrorFilter(ErrorPageFilter filter) {
			FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		    filterRegistrationBean.setFilter(filter);
		    filterRegistrationBean.setEnabled(false);
		    return filterRegistrationBean;
		}	 
	  
		@Override
		protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
	        return application.sources(App.class);
		}
    
		public static void main( String[] args ){
	    	SpringApplication.run(App.class, args);
	        System.out.println( "Application started!" );
		}
}

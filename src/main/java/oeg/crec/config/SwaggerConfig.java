package oeg.crec.config;

import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("oeg.crec.rest"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "crec internal REST API",
                "<img src='/images/crec.png' width=128px style='float:right;margin-top:-40px;'> This is the API to operate the <a href='http://crec.linkeddata.es'>crec</a>. "
                        + "Source code is it <a href='http://github.com/oeg-upm/crec'>github</a>"
                        + "<br>The methods in this API permit creating, "
                        + "deleting and populating Concept Schemes with SKOS Concepts. In addition, some handy transformation methods are provided, "
                        + "to transform data between TBX/LEMON/SKOS formats. These methods are based on the <a href='http://github.com/oeg-upm/creclib'>creclib</a> project.",
                "1.0",
                "All rights reserved",
                "Ontology Engineering Group - Universidad Politécnica de Madrid",
                "No license yet",
                "All rights reserved");
    }
  
    //https://www.baeldung.com/spring-boot-actuator-http
    //You can visit this to see the HTTP trace. http://crec.linkeddata.es/actuator/httptrace. 
    //Debe incluirse en el POM. spring-boot-starter-actuator
    @Bean
    public HttpTraceRepository httpTraceRepository() {
        return new InMemoryHttpTraceRepository();
    }
}

package com.proximity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;

import io.swagger.models.Contact;
import lombok.Getter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.proximity")).paths(PathSelectors.any()).build()
				.directModelSubstitute(Pageable.class, SwaggerPageable.class).apiInfo(apiInfo());
	}
	
	@Bean
	public RestTemplate rest() {
		return new RestTemplate();
	}

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Machines Administration System")
            .version("1.0.0")
            .build();
    }
	
}

@Getter
class SwaggerPageable {

	@Nullable
	private Integer size;

	@Nullable
	private Integer page;

	@Nullable
	private String sort;

}

package com.produtopedidoitens.emsapi.application.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${server.url}")
    private String serverUrl;

    @Bean
    public OpenAPI openApiInformation() {
        Server localServer = new Server().url(serverUrl).description(" Local server");

        Contact contact = new Contact()
                .email("vanderleik@yahoo.com.br")
                .name("Vanderlei Kleinschmidt")
                .url("https://www.linkedin.com/in/vanderlei-kleinschmidt-a1557731/");

        Info info = new Info()
                .contact(contact)
                .title("Sistema de gerenciamento de eventos")
                .description("API para cadastro e gerenciamento de eventos")
                .version("v0.0.1")
                .license(new License().name("Apache 2.0").url("http://springdoc.org"));

        return new OpenAPI()
                .info(info)
                .addServersItem(localServer)
                .externalDocs(new ExternalDocumentation()
                        .description("Springdoc OpenAPI 3.0")
                        .url("https://springdoc.org/"));
    }

}

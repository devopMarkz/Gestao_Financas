package io.github.devopMarkz.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Sistema de Biblioteca Online",
                description = "Sistema web para gerenciamento de uma biblioteca, onde usuários podem pesquisar livros, fazer reservas e acompanhar seus empréstimos, enquanto administradores podem gerenciar o acervo, usuários e todo o ciclo de empréstimos, com segurança.",
                version = "v1",
                contact = @Contact(
                        name = "Marcos André Costa da Silva",
                        email = "marcosdev2002@gmail.com"
                )
        )
)
public class OpenApiConfig {
}

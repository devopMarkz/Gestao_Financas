package io.github.devopMarkz.backend.usuario.application.mappers;

import io.github.devopMarkz.backend.usuario.application.dto.usuarios.UsuarioCreateDTO;
import io.github.devopMarkz.backend.usuario.application.dto.usuarios.UsuarioDTO;
import io.github.devopMarkz.backend.usuario.application.dto.usuarios.UsuarioUpdateDTO;
import io.github.devopMarkz.backend.usuario.domain.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UsuarioMapper {

    public abstract Usuario toUsuario(UsuarioDTO usuarioDTO);

    public abstract Usuario toUsuario(UsuarioCreateDTO usuarioCreateDTO);

    public abstract Usuario toUsuario(UsuarioUpdateDTO usuarioUpdateDTO);

    public abstract UsuarioDTO toUsuarioDTO(Usuario usuario);

}

package io.github.devopMarkz.backend.usuario.application.service;

import io.github.devopMarkz.backend.usuario.application.dto.usuarios.UsuarioCreateDTO;
import io.github.devopMarkz.backend.usuario.application.dto.usuarios.UsuarioDTO;
import io.github.devopMarkz.backend.usuario.domain.model.Usuario;
import io.github.devopMarkz.backend.usuario.domain.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioDTO salvarUsuario(UsuarioCreateDTO usuarioDTO) {
        Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario = usuarioRepository.save(usuario);
        return modelMapper.map(usuario, UsuarioDTO.class);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioDTO> usuariosDTO = usuarios.stream().map(u -> modelMapper.map(u, UsuarioDTO.class)).toList();
        return usuariosDTO;
    }

}

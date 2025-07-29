package io.github.devopMarkz.backend.usuario.application.service;

import io.github.devopMarkz.backend.usuario.application.dto.usuarios.UsuarioCreateDTO;
import io.github.devopMarkz.backend.usuario.application.dto.usuarios.UsuarioDTO;
import io.github.devopMarkz.backend.usuario.application.mappers.UsuarioMapper;
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
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          ModelMapper modelMapper,
                          PasswordEncoder passwordEncoder,
                          UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
    }

    @Transactional
    public UsuarioDTO salvarUsuario(UsuarioCreateDTO usuarioDTO) {
        Usuario usuario = usuarioMapper.toUsuario(usuarioDTO);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toUsuarioDTO(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream().map(usuarioMapper::toUsuarioDTO).toList();
    }

}

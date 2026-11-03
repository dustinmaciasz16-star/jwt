package com.example.jwt.service;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.example.jwt.entidades.Usuario;
import com.example.jwt.repositories.UsuarioRepository;

@Service
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;

	public UsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	// Registrar un usuario
	public Usuario registrar(Usuario usuario) {

		String contrasena = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt());

		usuario.setPassword(contrasena);

		return usuarioRepository.save(usuario);

	}

	// Autenticar un usuario
	public Usuario autenticar(String username, String password) {

		Optional<Usuario> usuarioEncontrado = usuarioRepository.findByUsername(username);

		if (usuarioEncontrado.isEmpty()) {
			return null;
		}

		Usuario usuario = usuarioEncontrado.get();

		if (BCrypt.checkpw(password, usuario.getPassword())) {
			return usuario;
		}

		return null;
	}
}

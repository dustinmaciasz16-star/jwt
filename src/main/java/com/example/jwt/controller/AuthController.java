package com.example.jwt.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.jwt.entidades.Usuario;
import com.example.jwt.service.UsuarioService;
import com.example.jwt.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UsuarioService usuarios;
	
	

    public AuthController(UsuarioService usuarioService) {
		this.usuarios = usuarioService;
	}

    @PostMapping("/registrar")
	public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
		try {
			Usuario userNuevo = usuarios.registrar(usuario);
			return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al registrar usuario" + e.getMessage());
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
		String username = credenciales.get("username");
		String password = credenciales.get("password");

		try {
			Usuario autenticado = usuarios.autenticar(username, password);
			String token = JwtUtil.generarToken(autenticado.getUsername(), autenticado.getRol());
			return ResponseEntity.ok(Map.of("token", token));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrecta");
		}
	}
    
    @GetMapping("/perfil")
	public ResponseEntity<?> verPerfil(@RequestHeader(value = "Authorization", required = false) String authHeader) {

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("Acceso denegado: Debes proveer un token bearer valido en la cabecera Authorization");
		}
		String toke = authHeader.substring(7);
		DecodedJWT datosToke = JwtUtil.validarToke(toke);
		if (datosToke == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado: token Invalido o expirado");
		}

		String usuario = datosToke.getSubject();
		String rol = datosToke.getClaim("rol").asString();

		return ResponseEntity.ok(Map.of("Mensaje", "Bienvenido al sistema", "Usuario", usuario, "Rol", rol, "Estatus",
				"Autenticado Exitosamente"));

	}
}

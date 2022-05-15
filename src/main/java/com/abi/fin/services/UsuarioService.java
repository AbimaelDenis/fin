package com.abi.fin.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.abi.fin.model.entities.Usuario;

public interface UsuarioService {
	
	List<Usuario> findAll();
	
	Usuario autenticar(String email, String senha);
	
	Usuario salvarUsuario(Usuario usuario);
	
	void validarEmail(String email);
	
	Optional<Usuario> procurarPorId(Long id);
}

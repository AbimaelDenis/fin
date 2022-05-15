package com.abi.fin.services;

import java.util.List;

import com.abi.fin.model.entities.Usuario;

public interface UsuarioService {
	
	List<Usuario> findAll();
	
	Usuario findById(Long id);
	
	Usuario autenticar(String email, String senha);
	
	Usuario salvarUsuario(Usuario usuario);
	
	void validarEmail(String email);
	
}

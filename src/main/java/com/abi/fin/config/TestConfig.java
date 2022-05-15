package com.abi.fin.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.abi.fin.model.repositories.UsuarioRepository;
import com.abi.fin.services.impl.UsuarioServiceImpl;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner{
	
	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private UsuarioServiceImpl service;
	
	@Override
	public void run(String... args) throws Exception {
//		//Cenário
//		Usuario usuario = new Usuario(null, "usuario", "usuario@email.com", "123");
//		repository.save(usuario);
//		
//		//ação
//		try {
//			//service.validarEmail(usuario.getEmail());
//			//service.autenticar("usuario@email.com", "123");
//			service.salvarUsuario(usuario);
//			System.out.println("Registrado !");
//		}
//		catch(RegraNegocioException e) {
//			System.out.println(e.getMessage());
//		}catch(ErroAutenticacao e) {
//			System.out.println(e.getMessage());
//		}
		
	}
	
}

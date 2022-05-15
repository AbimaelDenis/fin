package com.abi.fin.resources;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abi.fin.api.dto.UsuarioDTO;
import com.abi.fin.exceptions.ErroAutenticacao;
import com.abi.fin.exceptions.RegraNegocioException;
import com.abi.fin.model.entities.Lancamento;
import com.abi.fin.model.entities.Usuario;
import com.abi.fin.services.LancamentoService;
import com.abi.fin.services.UsuarioService;

@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioResource {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@GetMapping
	public ResponseEntity<List<Usuario>> findAll(){
		return ResponseEntity.ok().body(usuarioService.findAll());
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Usuario> findById(@PathVariable Long id){
		return ResponseEntity.ok().body(usuarioService.procurarPorId(id).get());
	}
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody UsuarioDTO dto) {
		Usuario usuario = new Usuario(null, dto.getNome(), dto.getEmail(), dto.getSenha()); 
		
		try {
			Usuario usuarioSalvo = usuarioService.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		}catch(RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping(value = "/{id}/saldo")
	public ResponseEntity obterSaldo(@PathVariable Long id) {
		Optional<Usuario> usuario = usuarioService.procurarPorId(id);
		if(!usuario.isPresent())
			return new ResponseEntity("Usuário não localizado." ,HttpStatus.NOT_FOUND);		
		return new ResponseEntity(String.format("Saldo: R$ %.2f", lancamentoService.obterSaldoPorUsuario(id)), HttpStatus.CREATED);
	}
	
	@PostMapping(value = "/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDTO dto){
		
		try {
			Usuario usuarioAutenticado = usuarioService.autenticar(dto.getEmail(), dto.getSenha());
			return ResponseEntity.ok(usuarioAutenticado);
		}catch(ErroAutenticacao e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}

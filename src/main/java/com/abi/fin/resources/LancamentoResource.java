package com.abi.fin.resources;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abi.fin.api.dto.AtualizaStatusDTO;
import com.abi.fin.api.dto.LancamentoDTO;
import com.abi.fin.exceptions.RegraNegocioException;
import com.abi.fin.model.entities.Lancamento;
import com.abi.fin.model.entities.Usuario;
import com.abi.fin.model.enums.StatusLancamento;
import com.abi.fin.model.enums.TipoLancamento;
import com.abi.fin.services.LancamentoService;
import com.abi.fin.services.UsuarioService;

@RestController
@RequestMapping(value = "/lancamentos")
public class LancamentoResource {

	@Autowired
	private LancamentoService lancamentoService;

	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping
	public ResponseEntity buscar(@RequestParam(value = "descricao", required = false) String descricao,
								@RequestParam(value = "mes", required = false) Integer mes,
								@RequestParam(value = "ano", required = false) Integer ano,
								@RequestParam(value = "usuario") Long idUsuario) {
		
		Optional<Usuario> u = usuarioService.procurarPorId(idUsuario);
		
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
			
		if(!u.isPresent())
			return ResponseEntity.badRequest().body("Não foi possível realizar a consulta: usuário não encontrado para o id informado");
		
		lancamentoFiltro.setUsuario(u.get());
		return ResponseEntity.ok().body(lancamentoService.buscar(lancamentoFiltro));
		
			
	}

	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {
		try {
			Lancamento lancamento = lancamentoService.salvar(converter(dto));
			return ResponseEntity.ok().body(lancamento);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity atualizar(@PathVariable Long id, @RequestBody LancamentoDTO dto) {
		Optional<Lancamento> l = lancamentoService.procurarPorId(id);
		if (!l.isPresent())
			return ResponseEntity.badRequest().body("Lançamento não encontrado na base de Dados.");
		try {
			Lancamento lancamento = converter(dto);
			lancamento.setId(l.get().getId());
			return new ResponseEntity(lancamentoService.atualizar(lancamento), HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping(value = "/atualiza-status/{id}")
	public ResponseEntity atualizarStatus(@PathVariable Long id, @RequestBody AtualizaStatusDTO dto) {
		Optional<Lancamento> lanc = lancamentoService.procurarPorId(id);
		if(!lanc.isPresent())
			return ResponseEntity.badRequest().body("Lançamento não encontrado para o id informado.");
		try {
			lanc.get().setStatus(StatusLancamento.valueOf(dto.getStatus().toUpperCase().trim()));
			return new ResponseEntity(lancamentoService.atualizar(lanc.get()), HttpStatus.CREATED);
		}catch(IllegalArgumentException e) {
			return ResponseEntity.badRequest().body("Status inválido.");
		}
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity deletar(@PathVariable Long id) {
		Optional<Lancamento> l = lancamentoService.procurarPorId(id);
		if(!l.isPresent())
			return ResponseEntity.badRequest().body("Lançamento não encontrado na base de Dados.");
		lancamentoService.deletar(l.get());		
			return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	private Lancamento converter(LancamentoDTO dto) {
		Lancamento lancamento = new Lancamento();
		lancamento.setId(dto.getId());
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setAno(dto.getAno());
		lancamento.setMes(dto.getMes());
		lancamento.setValor(dto.getValor());

		if (!usuarioService.procurarPorId(dto.getUsuario()).isPresent())
			throw new RegraNegocioException("Usuário não encontrado para o id informado");
		lancamento.setUsuario(usuarioService.procurarPorId(dto.getUsuario()).get());

		lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));

		return lancamento;

	}
}

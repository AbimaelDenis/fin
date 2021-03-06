
package com.abi.fin.services.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import com.abi.fin.exceptions.RegraNegocioException;
import com.abi.fin.model.entities.Lancamento;
import com.abi.fin.model.enums.StatusLancamento;
import com.abi.fin.model.enums.TipoLancamento;
import com.abi.fin.model.repositories.LancamentoRepository;
import com.abi.fin.services.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService {

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		validar(lancamento);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		return lancamentoRepository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		validar(lancamento);
		return lancamentoRepository.save(lancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		lancamentoRepository.delete(lancamento);
	}

	@Override
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		Example example = Example.of(lancamentoFiltro, ExampleMatcher.matching().withIgnoreCase()
				.withStringMatcher(org.springframework.data.domain.ExampleMatcher.StringMatcher.CONTAINING));

		return lancamentoRepository.findAll(example);
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		atualizar(lancamento);

	}

	@Override
	public void validar(Lancamento lancamento) {
		if (lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
			throw new RegraNegocioException("Informe uma Descri????o v??lida");
		}

		if (lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
			throw new RegraNegocioException("Infomr um M??s v??lido.");
		}

		if (lancamento.getAno() == null || lancamento.getAno().toString().length() != 4) {
			throw new RegraNegocioException("Infomar um Ano v??lido.");
		}

		if (lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null) {
			throw new RegraNegocioException("Infomar um Usu??rio.");
		}

		if (lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
			throw new RegraNegocioException("Informe um valor v??lido");
		}

		if (lancamento.getTipo() == null) {
			throw new RegraNegocioException("Informe um tipo de Lan??amento.");
		}
	}

	@Override
	public Optional<Lancamento> procurarPorId(Long id) {
		return lancamentoRepository.findById(id);
	}

	@Override
	@Transactional
	public BigDecimal obterSaldoPorUsuario(Long id) {
		BigDecimal receita = lancamentoRepository.obterSaldoPorTipoLancamentoEUsuario(id,TipoLancamento.RECEITA);
		BigDecimal despesa = lancamentoRepository.obterSaldoPorTipoLancamentoEUsuario(id,TipoLancamento.DESPESA);

		if (receita == null)
			receita = BigDecimal.ZERO;
		if (despesa == null)
			despesa = BigDecimal.ZERO;

		return receita.subtract(despesa);
	}

}

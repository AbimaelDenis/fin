package com.abi.fin.api.dto;

import java.math.BigDecimal;

public class LancamentoDTO {

	private Long id;
	private String descricao;
	private Integer mes;
	private Integer ano;
	private BigDecimal valor;
	private Long usuario;
	private String tipo;
	private String status;
	
	public LancamentoDTO() {}

	public Long getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getMes() {
		return mes;
	}

	public Integer getAno() {
		return ano;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public Long getUsuario() {
		return usuario;
	}

	public String getTipo() {
		return tipo;
	}

	public String getStatus() {
		return status;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public void setUsuario(Long usuario) {
		this.usuario = usuario;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	
}

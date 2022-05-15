package com.abi.fin.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abi.fin.model.entities.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}

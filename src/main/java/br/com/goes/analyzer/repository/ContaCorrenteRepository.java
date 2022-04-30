package br.com.goes.analyzer.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.goes.analyzer.model.ContaCorrente;

public interface ContaCorrenteRepository extends CrudRepository<ContaCorrente, Long>{
	
	Optional<ContaCorrente> findByBancoAndAgenciaAndConta(String banco, String agencia, String conta);
}

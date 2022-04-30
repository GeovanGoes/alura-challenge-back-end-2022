package br.com.goes.analyzer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.goes.analyzer.model.ContaCorrente;
import br.com.goes.analyzer.repository.ContaCorrenteRepository;

@Service
public class ContaCorrenteService {
	
	@Autowired
	private ContaCorrenteRepository repository;
	
	private Optional<ContaCorrente> findContaCorrenteExistente(ContaCorrente contaCorrente) {
		return repository.findByBancoAndAgenciaAndConta(
				contaCorrente.getBanco(), 
				contaCorrente.getAgencia(), 
				contaCorrente.getConta());
	}
	
	public ContaCorrente save(ContaCorrente cc) {
		Optional<ContaCorrente> finded = findContaCorrenteExistente(cc);
		if (finded.isEmpty())
			return repository.save(cc);
		else 
			return finded.get();
	}
}

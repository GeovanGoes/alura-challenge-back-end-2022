package br.com.goes.analyzer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Entity
@Data
public class ContaCorrente {

	
	@Id
	@GeneratedValue
	private Long id;
	private String banco;
	private String agencia;
	private String conta;
	
	public ContaCorrente(String banco, String agencia, String conta) {
		super();
		this.banco = banco;
		this.agencia = agencia;
		this.conta = conta;
	}
	
	public ContaCorrente() {
	}

	public boolean isValid() {
		if (StringUtils.isEmpty(banco) || StringUtils.isEmpty(agencia) || StringUtils.isEmpty(conta))
			return false;
		else 
			return true;
	}
	
	
	
}

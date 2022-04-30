package br.com.goes.analyzer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Entity
@Data
public class ContaCorrente {

	
	@Id
	@GeneratedValue
	private Long id;
	@JsonProperty("banco")
	private String banco;
	@JsonProperty("agencia")
	private String agencia;
	@JsonProperty("conta")
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

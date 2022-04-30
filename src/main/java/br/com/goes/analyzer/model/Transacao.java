package br.com.goes.analyzer.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;


@Entity
@Data
public class Transacao {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private ContaCorrente origem;
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private ContaCorrente destino;
	
	private BigDecimal valorTransacao;
	
	private LocalDateTime dataHoraTransacao;
	
	public Transacao() {
	}
	
	public Transacao(ContaCorrente origem, ContaCorrente destino, BigDecimal valorTransacao,
			LocalDateTime dataHoraTransacao) {
		super();
		this.origem = origem;
		this.destino = destino;
		this.valorTransacao = valorTransacao;
		this.dataHoraTransacao = dataHoraTransacao;
	}

	public boolean isValid() {
		if (this.origem.isValid() && this.destino.isValid() && valorTransacao != null && this.dataHoraTransacao != null)
			return true;
		else 
			return false;
	}
	
}

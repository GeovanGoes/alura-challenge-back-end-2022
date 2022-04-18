package br.com.goes.analyzer.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import br.com.goes.analyzer.controller.Columns;
import br.com.goes.analyzer.exceptions.ValidationException;
import lombok.Data;


@Entity
@Data
public class Transacao {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String bancoOrigem;
	
	private String agenciaOrigem;
	
	private String contaOrigem;
	
	private String bancoDestino;
	
	private String agenciaDestino;
	
	private String contaDestino;
	
	private BigDecimal valorTransacao;
	
	private LocalDateTime dataHoraTransacao;

	
	public Transacao(String line, int lineNumber, LocalDate date) throws Exception {
		String[] splittedData = line.split(",");
		
		if (splittedData.length != Columns.values().length)
			throw new ValidationException("Registro inválido: " + line);

		dataHoraTransacao = LocalDateTime.parse(getValue(splittedData, Columns.DATA_HORA_TRANSACAO));
		
		if (date != null && !dataHoraTransacao.toLocalDate().equals(date))
			throw new ValidationException("Registro inválido, data diferente do esperado: " + line);
		
		bancoOrigem = getValue(splittedData, Columns.BANCO_ORIGEM);
		agenciaOrigem = getValue(splittedData, Columns.AGENCIA_ORIGEM);
		contaOrigem = getValue(splittedData, Columns.CONTA_ORIGEM);
		bancoDestino = getValue(splittedData, Columns.CONTA_DESTINO);
		agenciaDestino = getValue(splittedData, Columns.BANCO_DESTINO);
		contaDestino = getValue(splittedData, Columns.AGENCIA_DESTINO);
		valorTransacao = new BigDecimal(getValue(splittedData, Columns.VALOR_TRANSACAO));
	}
	
	public Transacao() {
	}
	
	public String getValue(String[] sp, Columns column) throws Exception {
		String value = sp[Columns.getIndex(column)];
		if (value == null || value.trim().isEmpty())
			throw new ValidationException("Registro incompleto. Problemas ao obter os dados do campo: " + column.name());
		return value;
	}
	
	
}

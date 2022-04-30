package br.com.goes.analyzer.converters;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import br.com.goes.analyzer.controller.Columns;
import br.com.goes.analyzer.exceptions.ValidationException;
import br.com.goes.analyzer.model.ContaCorrente;
import br.com.goes.analyzer.model.Transacao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TransacoesCsvConverter implements TransacoesConverter{

	@Override
	public List<Transacao> converter(MultipartFile file) throws ValidationException, IOException {
		List<Transacao> transacoes = new ArrayList<>();
		String data = new String(file.getBytes());
		
		if (data == null || data.trim().isEmpty())
			throw new ValidationException("Arquivo vazio.");
		
        String[] rows = data.split("\\R");
        
        int lineNumber = 1;
        for (String string : rows) {
        	Transacao registro = null;
        	try {
        		registro = parse(string);
            	transacoes.add(registro);
			} catch (ValidationException ve) {
				log.error("Erro no parse: " + ve.getMessage());
			} catch (Exception e) {
				log.error("Erro inesperado no parse: ", e);
			}
        	lineNumber = lineNumber++;
        }
		return transacoes;
	}
	
	
	private Transacao parse(String line) throws Exception {
		String[] splittedData = line.split(",");
		
		if (splittedData.length != Columns.values().length)
			throw new ValidationException("Registro inválido: " + line);

		LocalDateTime dataHoraTransacao = LocalDateTime.parse(getValue(splittedData, Columns.DATA_HORA_TRANSACAO));		
		String bancoOrigem = getValue(splittedData, Columns.BANCO_ORIGEM);
		String agenciaOrigem = getValue(splittedData, Columns.AGENCIA_ORIGEM);
		String contaOrigem = getValue(splittedData, Columns.CONTA_ORIGEM);
		String bancoDestino = getValue(splittedData, Columns.BANCO_DESTINO);
		String agenciaDestino = getValue(splittedData, Columns.AGENCIA_DESTINO);
		String contaDestino = getValue(splittedData, Columns.CONTA_DESTINO);
		BigDecimal valorTransacao = new BigDecimal(getValue(splittedData, Columns.VALOR_TRANSACAO));
		
		ContaCorrente origem = new ContaCorrente(bancoOrigem, agenciaOrigem, contaOrigem);
		ContaCorrente destino = new ContaCorrente(bancoDestino, agenciaDestino, contaDestino);
		return new Transacao(origem, destino, valorTransacao, dataHoraTransacao);
	}
	
	private String getValue(String[] sp, Columns column) throws Exception {
		String value = sp[Columns.getIndex(column)];
		if (value == null || value.trim().isEmpty())
			throw new ValidationException("Registro incompleto. Problemas ao obter os dados do campo: " + column.name());
		return value;
	}

}

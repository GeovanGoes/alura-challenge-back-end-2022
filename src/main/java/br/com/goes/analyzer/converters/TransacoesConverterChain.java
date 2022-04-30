package br.com.goes.analyzer.converters;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import br.com.goes.analyzer.exceptions.ValidationException;
import br.com.goes.analyzer.model.Transacao;

@Component
public class TransacoesConverterChain {
	public List<Transacao> converter(MultipartFile file) throws ValidationException, IOException {
		TransacoesCsvConverter transacoesCsvConverter = new TransacoesCsvConverter();
		TransacoesXmlConverter transacoesXmlConverter = new TransacoesXmlConverter();
		transacoesCsvConverter.setNext(transacoesXmlConverter);
		return transacoesCsvConverter.converter(file);
	}
}

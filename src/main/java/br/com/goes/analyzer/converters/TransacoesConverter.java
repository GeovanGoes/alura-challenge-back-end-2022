package br.com.goes.analyzer.converters;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.com.goes.analyzer.exceptions.ValidationException;
import br.com.goes.analyzer.model.Transacao;

public interface TransacoesConverter {
	
	List<Transacao> converter(MultipartFile file) throws ValidationException, IOException;
	
}

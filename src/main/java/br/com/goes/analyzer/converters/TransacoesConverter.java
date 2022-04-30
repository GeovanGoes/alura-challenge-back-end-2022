package br.com.goes.analyzer.converters;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.com.goes.analyzer.exceptions.ValidationException;
import br.com.goes.analyzer.model.Transacao;

public abstract class TransacoesConverter {
	
	protected final String MSG_FORMATO_INVALIDO = "Formato de arquivo não suportado.";
	
	abstract List<Transacao> converter(MultipartFile file) throws ValidationException, IOException;
	abstract boolean isYourReponsability(String extensao);
	abstract void setNext(TransacoesConverter next);
	
	protected String getExtensaoDoArquivo(MultipartFile file) throws ValidationException {
		if (file != null) {
			String fileName = file.getOriginalFilename();
			String[] split = fileName.split("\\.");
			String extensao = split[split.length-1];
			return extensao;
		} else
			throw new ValidationException(this.MSG_FORMATO_INVALIDO);
	}
	
	
}

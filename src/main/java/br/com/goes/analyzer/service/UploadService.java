package br.com.goes.analyzer.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.goes.analyzer.exceptions.ValidationException;
import br.com.goes.analyzer.model.Transacao;
import br.com.goes.analyzer.model.Upload;
import br.com.goes.analyzer.model.Usuario;
import br.com.goes.analyzer.repository.UploadRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UploadService {

	@Autowired
	private UploadRepository repository;
	
	public Upload salvar(MultipartFile file, Usuario usuario) throws ValidationException, IOException {
		List<Transacao> transacoes = new ArrayList<>();
		String data = new String(file.getBytes());
		
		if (data == null || data.trim().isEmpty())
			throw new ValidationException("Arquivo vazio.");
		
        String[] rows = data.split("\\R");
        
        int lineNumber = 1;
        
        LocalDate date = null;
        
        boolean transacoesRepetidas = false;
        for (String string : rows) {
        	Transacao registro = null;
        	try {
        		if (!transacoes.isEmpty() && date == null) {
        			date = transacoes.stream().findFirst().get().getDataHoraTransacao().toLocalDate();
        			Optional<Upload> finded = repository.findUploadByDataReferencia(date);
        			if (finded.isPresent()) {
        				transacoesRepetidas = true;
        				break;
        			}
        		}
        		registro = new Transacao(string, lineNumber, date);
            	transacoes.add(registro);
			} catch (ValidationException ve) {
				log.error("Erro no parse: " + ve.getMessage());
			} catch (Exception e) {
				log.error("Erro inesperado no parse: ", e);
			}
        	lineNumber = lineNumber++;
        }
        
        if(transacoesRepetidas)
        	throw new ValidationException("Transações já recebidas para essa data.");
      
		Upload upload = new Upload();
		upload.setTransacoes(transacoes);
		upload.setDataReferencia(date);
		upload.setDataUpload(LocalDateTime.now());
		upload.setUsuarioUpload(usuario);
		Upload saved = repository.save(upload);
		return saved;
	}
	
	
	
	public List<Upload> list(){
		return repository.findAllByDataReferenciaNotNullOrderByDataReferenciaDesc();
	}
	
	
	public Optional<Upload> findById(Long id) {
		return repository.findById(id);
	}
}

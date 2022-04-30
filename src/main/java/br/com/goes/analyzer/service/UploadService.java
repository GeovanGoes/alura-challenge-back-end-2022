package br.com.goes.analyzer.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.goes.analyzer.converters.TransacoesCsvConverter;
import br.com.goes.analyzer.exceptions.ValidationException;
import br.com.goes.analyzer.model.ContaCorrente;
import br.com.goes.analyzer.model.Transacao;
import br.com.goes.analyzer.model.Upload;
import br.com.goes.analyzer.model.Usuario;
import br.com.goes.analyzer.repository.ContaCorrenteRepository;
import br.com.goes.analyzer.repository.UploadRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UploadService {

	@Autowired
	private UploadRepository repository;
	
	@Autowired
	private ContaCorrenteService contaCorrenteService;
	
	@Autowired
	private TransacoesCsvConverter csvConverter; 
	
	public Upload salvar(MultipartFile file, Usuario usuario) throws ValidationException, IOException {
		List<Transacao> transacoes = csvConverter.converter(file);
		transacoes = validarTransacoes(transacoes);
		LocalDate date = transacoes.stream().findFirst().get().getDataHoraTransacao().toLocalDate();
		transacoes.forEach(item -> {
				item.setDestino(contaCorrenteService.save(item.getDestino()));
				item.setOrigem(contaCorrenteService.save(item.getOrigem()));
		});
		Upload upload = new Upload();
		upload.setTransacoes(transacoes);
		upload.setDataReferencia(date);
		upload.setDataUpload(LocalDateTime.now());
		upload.setUsuarioUpload(usuario);
		Upload saved = repository.save(upload);
		return saved;
	}
	
	
	/***
	 * 
	 * @param transacoes
	 * @return
	 * @throws ValidationException
	 */
	private List<Transacao> validarTransacoes(List<Transacao> transacoes) throws ValidationException {        
        transacoes = transacoes.stream().filter(t -> t.isValid()).collect(Collectors.toList());
        LocalDate date = transacoes.stream().findFirst().orElseThrow(() -> new ValidationException("Arquivo vazio.")).getDataHoraTransacao().toLocalDate();
        Optional<Upload> findUploadByDataReferencia = repository.findUploadByDataReferencia(date);
        if (findUploadByDataReferencia.isPresent())
        	throw new ValidationException("Transações já recebidas para essa data.");
        transacoes = transacoes.stream().filter(t -> t.getDataHoraTransacao().toLocalDate().equals(date)).collect(Collectors.toList());
        transacoes.stream().findFirst().orElseThrow(() -> new ValidationException("Arquivo vazio."));
        return transacoes;
	}



	public List<Upload> list(){
		return repository.findAllByDataReferenciaNotNullOrderByDataReferenciaDesc();
	}
	
	
	public Optional<Upload> findById(Long id) {
		return repository.findById(id);
	}
}

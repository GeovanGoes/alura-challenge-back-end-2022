package br.com.goes.analyzer.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.goes.analyzer.model.Upload;

public interface UploadRepository extends CrudRepository<Upload, Long>{
	
	Optional<Upload> findUploadByDataReferencia(LocalDate dataReferencia);
	
	List<Upload> findAllByDataReferenciaNotNullOrderByDataReferenciaDesc();
	
}

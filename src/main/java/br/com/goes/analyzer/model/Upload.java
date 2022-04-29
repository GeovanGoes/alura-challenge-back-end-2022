package br.com.goes.analyzer.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class Upload {
	
	@Id
	@GeneratedValue
	private Long id;
	private LocalDate dataReferencia;
	private LocalDateTime dataUpload;
	@OneToMany(cascade = CascadeType.ALL, fetch =  FetchType.EAGER)
	@JoinColumn(name = "upload_id")
	private List<Transacao> transacoes;
	@OneToOne(fetch = FetchType.EAGER)
	private Usuario usuarioUpload;

}

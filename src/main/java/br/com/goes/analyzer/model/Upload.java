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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Data;


@JacksonXmlRootElement
@Entity
@Data
public class Upload {
	
	@JsonIgnore
	@Id
	@GeneratedValue
	private Long id;
	@JsonIgnore
	private LocalDate dataReferencia;
	@JsonIgnore
	private LocalDateTime dataUpload;
	@JsonProperty("transacao")
	@OneToMany(cascade = CascadeType.ALL, fetch =  FetchType.EAGER)
	@JoinColumn(name = "upload_id")
	private List<Transacao> transacoes;
	@JsonIgnore
	@OneToOne(fetch = FetchType.EAGER)
	private Usuario usuarioUpload;

}

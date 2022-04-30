package br.com.goes.analyzer.converters;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import br.com.goes.analyzer.exceptions.ValidationException;
import lombok.Data;

public class TransacoesXmlConverter extends TransacoesConverter {

	private TransacoesConverter next;
	private final String MY_RESPONSABILITY = "xml";

	@Override
	public List<br.com.goes.analyzer.model.Transacao> converter(MultipartFile file) throws ValidationException, IOException {
		
		if (isYourReponsability(getExtensaoDoArquivo(file))) {
			XmlMapper mapper = new XmlMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
			
			List<Transacao> readValue = mapper.readValue(file.getBytes(), new TypeReference<List<Transacao>>() {});
			
			if (readValue != null) {
				return readValue.stream().map(item -> new br.com.goes.analyzer.model.Transacao(
						new br.com.goes.analyzer.model.ContaCorrente(item.getOrigem().getBanco(), item.getOrigem().getAgencia(), item.getOrigem().getConta()), 
						new br.com.goes.analyzer.model.ContaCorrente(item.getDestino().getBanco(), item.getDestino().getAgencia(), item.getDestino().getConta()), 
						new BigDecimal(item.getValor()), 
						LocalDateTime.parse(item.getData()))).collect(Collectors.toList());
			} else
				throw new ValidationException("Problemas para obter dados do xml.");
			
		} else if (next != null)
			return next.converter(file);
		else
			throw new ValidationException(super.MSG_FORMATO_INVALIDO);
	}

	@Override
	boolean isYourReponsability(String extensao) {
		return this.MY_RESPONSABILITY.equalsIgnoreCase(extensao);
	}

	@Override
	void setNext(TransacoesConverter next) {
		this.next = next;		
	}
	
	
	@Data
	static class Transacoes {
		@JacksonXmlElementWrapper(localName = "transacao")
		private List<Transacao> transacao;

		public Transacoes(List<Transacao> transacao) {
			super();
			this.transacao = transacao;
		}
		public Transacoes() {
		}
		
	}
	
	@Data
	static class Transacao {
		private ContaCorrente origem;
		private ContaCorrente destino;
		private String valor;
		private String data;
		public Transacao(ContaCorrente origem, ContaCorrente destino, String valor, String data) {
			super();
			this.origem = origem;
			this.destino = destino;
			this.valor = valor;
			this.data = data;
		}
		public Transacao() {
			System.out.println("iiiio");
		}
	}
	
	@Data
	static class ContaCorrente {
		private String banco;
		private String agencia;
		private String conta;
		public ContaCorrente(String banco, String agencia, String conta) {
			super();
			this.banco = banco;
			this.agencia = agencia;
			this.conta = conta;
		}
		public ContaCorrente() {
			System.out.println("iiiio");
		}
	}

}

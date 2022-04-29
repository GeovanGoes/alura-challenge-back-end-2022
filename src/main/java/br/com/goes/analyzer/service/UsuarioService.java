package br.com.goes.analyzer.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import br.com.goes.analyzer.exceptions.ValidationException;
import br.com.goes.analyzer.model.Usuario;
import br.com.goes.analyzer.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;
	@Autowired
	private JavaMailSender mailSender;
	
	
	public Usuario criar(String nome, String email) throws ValidationException {
	
		String senhaEmClaro = gerarSenha();
		Usuario usuario = salvarUsuarioNovo(nome, email, senhaEmClaro);
		
		if (usuario != null)
			enviarEmail(senhaEmClaro, usuario.getEmail());
		else
			throw new ValidationException("Problemas ao criar usuário.");
		return usuario;
	}

	@Transactional
	private Usuario salvarUsuarioNovo(String nome, String email, String senhaEmClaro) throws ValidationException {
		Optional<Usuario> findByEmail = buscarPorEmail(email);
		if (!findByEmail.isPresent())
			return repository.save(new Usuario(nome, email, criptografarSenha(senhaEmClaro), true));
		else if (!Boolean.TRUE.equals(findByEmail.get().getAtivo())) {
			return reativarUsuario(nome, senhaEmClaro, findByEmail);
		} else
			throw new ValidationException("Email já em uso.");
	}

	public Optional<Usuario> buscarPorEmailAndAtivo(String email) {
		Optional<Usuario> findByEmail = repository.findByEmailAndAtivoTrue(email);
		return findByEmail;
	}
	
	public Optional<Usuario> buscarPorEmail(String email) {
		Optional<Usuario> findByEmail = repository.findByEmail(email);
		return findByEmail;
	}

	private Usuario reativarUsuario(String nome, String senhaEmClaro, Optional<Usuario> findByEmail) {
		Usuario usuario = findByEmail.get();
		usuario.setNome(nome);
		usuario.setAtivo(true);
		usuario.setSenha(criptografarSenha(senhaEmClaro));
		return repository.save(usuario);
	}
	
	public void enviarEmail(String senhaEmClaro, String mail) throws ValidationException {
		try {
			log.error("enviando e-mail.");
			SimpleMailMessage smm = new SimpleMailMessage();
			smm.setTo(mail);
			smm.setText("Olá, sua senha é: " + senhaEmClaro + " use-a para acessar o sistema!");
			smm.setSubject("Sua senha chegou! =D");
			mailSender.send(smm);
		} catch (Exception e) {
			throw new ValidationException("não foi possível enviar o email com a senha.");
		}
		
	}

	public String criptografarSenha(String senhaEmClaro) {
		String hashpw = BCrypt.hashpw(senhaEmClaro, BCrypt.gensalt());
		log.info("Cripfografada: " + hashpw);
		return hashpw;
	}

	private String gerarSenha() {		
		int index = 0;
		String pass = "";
		Random r = new Random();
		while (index <= 5) {
			int inteiro = r.nextInt(9);
			pass = pass + inteiro;
			index = index+1;
		}
		log.info("senha aleatoria: " + pass);
		return pass;
	}
	
	
	public Usuario atualizar(Long id, String nome, String email) throws ValidationException {
		Usuario usuario = repository.findById(id).orElseThrow(() -> new ValidationException("usuario nao encontrado."));
		
		Optional<Usuario> findByEmail = repository.findByEmail(email);
		if (findByEmail.isEmpty())
			throw new ValidationException("email em uso."); 
		
		usuario.setEmail(email);
		usuario.setNome(nome);
		return repository.save(usuario);		
	}
	
	public Set<Usuario> listarUsuariosAtivos() {
		Set<Usuario> emptySet = new HashSet<>();
		
		Iterable<Usuario> findAll = repository.findAll();
		for (Usuario usuario : findAll) {
			if (Boolean.TRUE.equals(usuario.getAtivo()) && !"admin@email.com.br".equalsIgnoreCase(usuario.getEmail()))
				emptySet.add(usuario);
		}
		return  emptySet;
	}
	
	public void deletarUsuario(Long id) throws ValidationException {
		Usuario usuario = repository.findById(id).orElseThrow(() -> new ValidationException("usuario nao encontrado."));
		usuario.setAtivo(false);
		repository.save(usuario);
	}

	public Usuario buscarPorId(Long id) throws ValidationException {
		return repository.findById(id).orElseThrow(() -> new ValidationException("usuario nao encontrado."));
	}
	
	
}

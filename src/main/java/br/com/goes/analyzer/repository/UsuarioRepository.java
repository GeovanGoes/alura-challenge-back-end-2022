package br.com.goes.analyzer.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.goes.analyzer.model.Usuario;

public interface UsuarioRepository  extends CrudRepository<Usuario, Long>{
	Optional<Usuario> findByEmail(String email);
	Optional<Usuario> findByEmailAndAtivoTrue(String email);
}

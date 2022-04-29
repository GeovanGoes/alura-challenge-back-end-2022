package br.com.goes.analyzer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

	@Autowired
	private UsuarioService service;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return service.buscarPorEmailAndAtivo(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
	}
}

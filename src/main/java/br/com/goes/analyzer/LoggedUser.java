package br.com.goes.analyzer;

import org.springframework.security.core.context.SecurityContextHolder;

import br.com.goes.analyzer.model.Usuario;

public class LoggedUser {
	
	
	public static Usuario getUsuario () {
		return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

}

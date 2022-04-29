package br.com.goes.analyzer.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Entity
@Data
public class Usuario implements UserDetails {
	
	@Id
	@GeneratedValue
	private Long id;
	private String nome;
	@Column(unique = true)
	private String email;
	private String senha;
	private Boolean ativo;
	
	public Usuario(String nome, String email, String senha, Boolean ativo) {
		super();
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.ativo = ativo;
	}
	
	public Usuario() {
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return null;
	}

	@Override
	public String getPassword() {
		return this.getSenha();
	}

	@Override
	public String getUsername() {
		return this.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return getAtivo();
	}

	@Override
	public boolean isAccountNonLocked() {
		return getAtivo();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return getAtivo();
	}

	@Override
	public boolean isEnabled() {
		return getAtivo();
	}
	
}

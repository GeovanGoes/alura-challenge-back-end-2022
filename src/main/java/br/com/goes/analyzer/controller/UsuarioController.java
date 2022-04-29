package br.com.goes.analyzer.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.goes.analyzer.LoggedUser;
import br.com.goes.analyzer.exceptions.ValidationException;
import br.com.goes.analyzer.model.Usuario;
import br.com.goes.analyzer.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class UsuarioController {

	
	@Autowired
	private UsuarioService service;
	
	@GetMapping("/usuarios")
	public String lista(Model model) {
		
		Set<Usuario> users = service.listarUsuariosAtivos();
		model.addAttribute("usuarios", users);
		
		return "lista-usuarios";
	}
	
	
	@GetMapping("/usuarios/form-usuarios")
	public String formUsuarios(@RequestParam(name = "id", value = "id", required = false, defaultValue = "") Long id, Model model) {
		log.info("idUsuario: " + (id == null ? "null" : id));
		if (id == null || id == 0L)
			return "formulario-usuarios";
		else {
			Usuario usuario;
			try {
				usuario = service.buscarPorId(id);
				colocarDadosNoModel(model, usuario);
				return "formulario-usuarios";
			} catch (ValidationException e) {
				log.error("",e);
				model.addAttribute("error", e.getMessage());
				return "formulario-usuarios";
			}
		}
	}
	
	
	@PostMapping("/usuarios/save")
	public String save (@RequestParam(value = "id", required = false)Long id, @RequestParam("nome")String nome, @RequestParam("email")String email, Model model) {
		
		log.info(""+id);
		log.info(nome);
		log.info(email);
		
		try {
			if (id == null || id == 0) {
				service.criar(nome, email);
			} else {
				service.atualizar(id, nome, email);
			}
			model.addAttribute("success", "Operação realizada com sucesso!");
			return "redirect:/usuarios";
		} catch (ValidationException e) {
			log.error("",e);
			model.addAttribute("error", e.getMessage());
			Usuario usuario = new Usuario(nome, email, null, true);
			colocarDadosNoModel(model, usuario);
		} catch (Exception e) {
			log.error("",e);
			model.addAttribute("error", "Falha na operação");
			Usuario usuario = new Usuario(nome, email, null, true);
			colocarDadosNoModel(model, usuario);
		}
		return "formulario-usuarios";
	}
	
	
	private void colocarDadosNoModel(Model model, Usuario usuario) {
		model.addAttribute("nome", usuario.getNome());
		model.addAttribute("email", usuario.getEmail());
		model.addAttribute("id", usuario.getId());
	}
	
	
	@GetMapping("/usuarios/deletar")
	private String delete(@RequestParam(value = "id", required = true)Long id, Model model) {
		try {
			
			if (!id.equals(LoggedUser.getUsuario().getId()))
				service.deletarUsuario(id);
			else
				model.addAttribute("error", "Não é possível deletar voce mesmo.");
		} catch (ValidationException e) {
			log.error("",e);
			model.addAttribute("error", e.getMessage());
		} catch (Exception e) {
			log.error("",e);
			model.addAttribute("error", "Falha na operação");
		}
		return "redirect:/usuarios";
	}
	
	
}

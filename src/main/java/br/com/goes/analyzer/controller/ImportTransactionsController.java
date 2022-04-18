package br.com.goes.analyzer.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.goes.analyzer.exceptions.ValidationException;
import br.com.goes.analyzer.model.Upload;
import br.com.goes.analyzer.service.UploadService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ImportTransactionsController {

	@Autowired
	private UploadService service;
	
	@GetMapping("/import")
	public String index(Model model) {
		List<Upload> list = service.list();
		model.addAttribute("list", list);
		return "index";
	}
	
	@PostMapping("/import")
	public String importar(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes, Model model) {
		
		try {
			service.salvar(file);
			model.addAttribute("success", "Operação realizada com sucesso!");
		} catch (ValidationException e) {
			log.error("",e);
			model.addAttribute("error", e.getMessage());
		} catch (Exception e) {
			log.error("",e);
			model.addAttribute("error", "Falha na operação");
		}
		List<Upload> list = service.list();
		model.addAttribute("list", list);
		return "index";
	}
	
}

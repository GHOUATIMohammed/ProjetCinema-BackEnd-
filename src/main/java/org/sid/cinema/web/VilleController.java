package org.sid.cinema.web;

import java.util.List;

import javax.validation.Valid;

import org.sid.cinema.dao.CinemaRepository;
import org.sid.cinema.dao.VilleRepository;
import org.sid.cinema.entities.Cinema;
import org.sid.cinema.entities.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VilleController {

	@Autowired
	VilleRepository villeRepository;


	

	@GetMapping(path = "/ville")
	public String ville(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "motCle", defaultValue = "") String motCle) {
		Page<Ville> pagevilles = villeRepository.findByNameContains(motCle, PageRequest.of(page, size));
		model.addAttribute("pagevilles", pagevilles);
		model.addAttribute("currentpage", page);
		model.addAttribute("motCle", motCle);
		model.addAttribute("size", size);
		model.addAttribute("pages", new int[pagevilles.getTotalPages()]);
		return "ville";
	}

	@GetMapping(path = "/deleteVille")
	public String deleteville(Long id, String motCle, int page, int size, Model model) {
		Ville v = villeRepository.findById(id).get();
		model.addAttribute("id_courant", id);
		if (v.getCinemas().isEmpty()) {
			villeRepository.deleteById(id);
			model.addAttribute("modeSup", "Autorise");
		} else {
			model.addAttribute("modeSup", "nonAutorise");
		}
		return ville(model, page, size, motCle);
	}

	@GetMapping(path = "/formVille")
	public String formVille(Model model) {
		model.addAttribute("ville", new Ville());
		model.addAttribute("mode", "new");
		return "formVille";
	}

	@PostMapping(path = "/saveVille")
	public String saveVille(@Valid Ville v, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors())
		{
			
			model.addAttribute("mode", "new");
			return "FormVille";
		}
		villeRepository.save(v);
		model.addAttribute("ville", v);
		return "confirmationVille";
	}

	@GetMapping(path = "/editVille")
	public String editVille(Model model, Long id) {

		Ville v = villeRepository.findById(id).get();
		model.addAttribute("ville", v);
		model.addAttribute("mode", "edit");
		return "formVille";
	}
}

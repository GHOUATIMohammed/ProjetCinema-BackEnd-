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
public class CinemaController {

	@Autowired
	CinemaRepository cinemaRepository;
	@Autowired
	VilleRepository villeRepository;

	

	@GetMapping(path = "/cinema")
	public String cinemas(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "motCle", defaultValue = "") String motCle,@RequestParam(name = "nomVille", defaultValue = "") String nomVille) {
		Page<Cinema> pagecinemas;
		if(nomVille.isEmpty())
			pagecinemas = cinemaRepository.findByNameContains(motCle, PageRequest.of(page, size));
		else
			pagecinemas =cinemaRepository.findByVilleNameContains(nomVille, PageRequest.of(page, size));
		model.addAttribute("pagecinemas", pagecinemas);
		model.addAttribute("currentpage", page);
		model.addAttribute("motCle", motCle);
		model.addAttribute("size", size);
		model.addAttribute("pages", new int[pagecinemas.getTotalPages()]);
		return "cinema";
	}

	@GetMapping(path = "/deleteCinema")
	public String deletecinema(Long id, String motCle,@RequestParam(name = "nomVille", defaultValue = "") String nomVille, int page, int size, Model model) {
		Cinema c = cinemaRepository.findById(id).get();
		model.addAttribute("id_courant", id);
		if (c.getSalles().isEmpty()) {
			cinemaRepository.deleteById(id);
			model.addAttribute("modeSup", "Autorise");
		} else {
			model.addAttribute("modeSup", "nonAutorise");
		}
		return cinemas(model, page, size, motCle,nomVille);
	}

	@GetMapping(path = "/formCinema")
	public String formCinema(Model model) {
		List<Ville> villes = villeRepository.findAll();
		model.addAttribute("villes", villes);
		model.addAttribute("cinema", new Cinema());
		model.addAttribute("mode", "new");
		return "formCinema";
	}

	@PostMapping(path = "/saveCinema")
	public String saveCinema(@Valid Cinema c, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors())
		{
			List<Ville> villes = villeRepository.findAll();
			model.addAttribute("villes", villes);
			model.addAttribute("mode", "new");
			return "FormCinema";
		}
		cinemaRepository.save(c);
		model.addAttribute("cinema", c);
		return "confirmationCinema";
	}

	@GetMapping(path = "/editCinema")
	public String editCinema(Model model, Long id) {
		List<Ville> villes = villeRepository.findAll();
		model.addAttribute("villes", villes);

		Cinema c = cinemaRepository.findById(id).get();
		model.addAttribute("cinema", c);
		model.addAttribute("mode", "edit");
		return "formCinema";
	}
}

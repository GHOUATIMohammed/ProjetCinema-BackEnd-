package org.sid.cinema.web;

import java.util.List;

import javax.validation.Valid;

import org.sid.cinema.dao.PlaceRepository;
import org.sid.cinema.dao.ProjectionRepository;
import org.sid.cinema.dao.TicketRepository;
import org.sid.cinema.entities.Place;
import org.sid.cinema.entities.Projection;
import org.sid.cinema.entities.Ticket;

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
public class TicketController {

	@Autowired
	TicketRepository ticketRepository;
	@Autowired
	PlaceRepository placeRepository;
	@Autowired
	ProjectionRepository projectionRepository;

	@GetMapping(path = "/ticket")
	public String ticket(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "20") int size,
			@RequestParam(name = "motCle", defaultValue = "0") Long motCle,@RequestParam(name = "idProj", defaultValue = "0") Long idProj) {
		Page<Ticket> pagetickets;
		if (motCle != 0)
			pagetickets = ticketRepository.findById(motCle,PageRequest.of(page, size));
		else if(idProj==0)
			pagetickets = ticketRepository.findAll(PageRequest.of(page, size));
		else
			pagetickets = ticketRepository.findByProjectionId(idProj,PageRequest.of(page, size));
		model.addAttribute("pagetickets", pagetickets);
		model.addAttribute("currentpage", page);
		model.addAttribute("motCle", motCle);
		model.addAttribute("size", size);
		model.addAttribute("idProj", idProj);
		model.addAttribute("pages", new int[pagetickets.getTotalPages()]);
		return "ticket";
	}

	@GetMapping(path = "/deleteTicket")
	public String deleteticket(Long id, Long motCle, int page, int size, Model model,@RequestParam(name = "idProj", defaultValue = "0") Long idProj) {
		Ticket t = ticketRepository.findById(id).get();
		model.addAttribute("id_courant", id);
		ticketRepository.deleteById(id);
		model.addAttribute("modeSup", "Autorise");
		return ticket(model, page, size, motCle,idProj);
	}

	@GetMapping(path = "/formTicket")
	public String formTicket(Model model) {
		List<Place> places = placeRepository.findAll();
		List<Projection> projections=projectionRepository.findAll();
		model.addAttribute("places", places);
		model.addAttribute("projections", projections);
		model.addAttribute("ticket", new Ticket());
		model.addAttribute("mode", "new");
		return "formTicket";
	}

	@PostMapping(path = "/saveTicket")
	public String saveTicket(@Valid Ticket t, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			List<Place> places = placeRepository.findAll();
			List<Projection> projections=projectionRepository.findAll();
			model.addAttribute("places", places);
			model.addAttribute("projections", projections);
			return "FormTicket";
		}
		ticketRepository.save(t);
		model.addAttribute("ticket", t);
		return "confirmationTicket";
	}

	@GetMapping(path = "/editTicket")
	public String editTicket(Model model, Long id) {
		List<Place> places = placeRepository.findAll();
		List<Projection> projections=projectionRepository.findAll();
		model.addAttribute("places", places);
		model.addAttribute("projections", projections);

		Ticket t = ticketRepository.findById(id).get();
		model.addAttribute("ticket", t);
		model.addAttribute("mode", "edit");
		return "formTicket";
	}

}

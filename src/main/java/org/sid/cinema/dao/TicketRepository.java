package org.sid.cinema.dao;



import org.sid.cinema.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource
@CrossOrigin("*")
public interface TicketRepository extends JpaRepository<Ticket, Long>{

	public Page<Ticket> findById(Long mc,Pageable pageable);
	public Page<Ticket> findByProjectionId(Long id,Pageable pageable);
}

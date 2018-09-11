package com.infotech.book.ticket.app.controller;

import com.hazelcast.util.IterableUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infotech.book.ticket.app.entities.Ticket;
import com.infotech.book.ticket.app.service.TicketBookingService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping(value="/api/tickets")
public class TicketBookingController {

	private final Logger logger = Logger.getLogger(TicketBookingController.class);

	@Autowired
	private TicketBookingService ticketBookingService;
	
	@GetMapping(value="/ticket/{ticketId}")
	public Ticket getTicketById(@PathVariable("ticketId")Integer ticketId){
		String logFormat = "%s call took %d millis with result: %s";
		long start1 = nanoTime();
		Ticket ticket = ticketBookingService.getTicketById(ticketId);
		long end1 = nanoTime();
		TimeUnit tu = TimeUnit.NANOSECONDS;
		Long nilai = end1 - start1;
		logger.info("Rest {" + ticketId + "} in ms" + tu.toMillis(nilai));

		return ticket;
	}

    @GetMapping(value="/ticket")
    public List<Ticket> getTicketAll(){

        long start1 = nanoTime();
        Iterable<Ticket> tickets = ticketBookingService.getTicketAll(-1);
        long end1 = nanoTime();
        TimeUnit tu = TimeUnit.NANOSECONDS;
        Long nilai = end1 - start1;
        logger.info("Rest all in ms" + tu.toMillis(nilai));

        List<Ticket> list = new ArrayList<>();
        tickets.iterator().forEachRemaining(list::add);
        return list;

    }

	private Long nanoTime(){
		return System.nanoTime();
	}


	@DeleteMapping(value="/ticket/{ticketId}")
	public void deleteTicket(@PathVariable("ticketId")Integer ticketId){
		ticketBookingService.deleteTicket(ticketId);
	}
	
	@PutMapping(value="/ticket/{ticketId}/{newEmail:.+}")
	public Ticket updateTicket(@PathVariable("ticketId")Integer ticketId,@PathVariable("newEmail")String newEmail){
		Ticket t = ticketBookingService.updateTicket(ticketId,newEmail);
        // ticketBookingService.getTicketAll(-1);
        return t;
	}
}

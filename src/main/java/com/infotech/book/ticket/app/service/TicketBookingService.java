package com.infotech.book.ticket.app.service;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.infotech.book.ticket.app.TicketBookingManagementApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.infotech.book.ticket.app.dao.TicketBookingDao;
import com.infotech.book.ticket.app.entities.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class  TicketBookingService {

	@Autowired
	private TicketBookingDao ticketBookingDao;
	
	//@Cacheable(value="ticketsCache",key="#ticketId",unless="#result==null")
	public Ticket getTicketById(Integer ticketId) {

		IMap<Integer, Ticket> map =   TicketBookingManagementApplication.instance.getMap("ticketsCache");
		return map.get(ticketId);
	}

	private List<Ticket> findByNama(String nama){

		IMap<Integer, Ticket> map =  TicketBookingManagementApplication.instance.getMap("ticketsCache");
		return map
				.entrySet()
				.stream()
				.filter(ticket -> Objects.equals(ticket.getValue(), nama))
				.map(Map.Entry::getValue)
				.collect(Collectors.toList());

	}
	// @Cacheable( value = "ticketsCache", sync = true)
	public List<Ticket> getTicketAll(Integer ticketId) {
		IMap<Integer, Ticket> map =  TicketBookingManagementApplication.instance.getMap("ticketsCache");
		System.out.println("map value == " +map);
		if (map.size()<1){
			Iterable<Ticket> ticketList = ticketBookingDao.findAll();
			ticketList.forEach(ticket -> {
				map.put(ticket.getTicketId(), ticket);
			});

		}
		List<Ticket> ticketList = new ArrayList<>();
		map.values()
				.iterator()
				.forEachRemaining(ticket -> {
					ticketList.add(ticket);
				});

		ticketList.sort((x,y) -> x.getTicketId().compareTo(y.getTicketId()));
		return ticketList ;
	}

	@CacheEvict(value="ticketsCache",key="#ticketId")
	public void deleteTicket(Integer ticketId) {
		ticketBookingDao.delete(ticketId);
	}


//	@CachePut(value="ticketsCache"  ,key="#ticketId")
	public Ticket updateTicket(Integer ticketId, String newEmail) {
		Ticket upadedTicket = null;
		Ticket ticketFromDb = ticketBookingDao.findOne(ticketId);
		if(ticketFromDb != null){
			ticketFromDb.setEmail(newEmail);
			upadedTicket = ticketBookingDao.save(ticketFromDb);	
		}
		// this.getTicketAll(-1);

		IMap<Integer, Ticket> map = TicketBookingManagementApplication.instance.getMap("ticketsCache");
		map.put(ticketId, upadedTicket);
		return upadedTicket;
	}
}

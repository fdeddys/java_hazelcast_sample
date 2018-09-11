package com.infotech.book.ticket.app;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TicketBookingManagementApplication {

	public static HazelcastInstance instance;
	public static void main(String[] args) {
		SpringApplication.run(TicketBookingManagementApplication.class, args);

		instance = Hazelcast.newHazelcastInstance();
	}
}

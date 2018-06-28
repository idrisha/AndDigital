package com.and.digital.telecomprovider.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.and.digital.telecomprovider.model.Customer;
import com.and.digital.telecomprovider.service.TelecomService;
@RestController
public class TelecomController {

	@Autowired
	private TelecomService telecomService;

	@GetMapping("/phoneNumbers")
	public List<String> retrieveAllPhoneNumbers() {
		return telecomService.retrievePhoneNumbers();
	}

	@GetMapping("/phoneNumber/{customerId}")
	public Customer retrievePhoneNumbersOfCustomer(@PathVariable int customerId) {
		return telecomService.retrievePhoneNumberCustomer(customerId);
	}

	@PostMapping("/activateNumber/{customerId}/{phoneNumber}")
	public ResponseEntity<Void> activateNumber(@PathVariable int customerId, @PathVariable String phoneNumber) {

		Customer customer = telecomService.activateNumber(customerId, phoneNumber);

		if (customer == null)
			return ResponseEntity.noContent().build();

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(customer.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
}

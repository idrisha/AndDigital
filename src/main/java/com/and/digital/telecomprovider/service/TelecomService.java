package com.and.digital.telecomprovider.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.and.digital.telecomprovider.exception.CustomerNotFoundException;
import com.and.digital.telecomprovider.exception.PhoneNumberException;
import com.and.digital.telecomprovider.model.Customer;

@Component
public class TelecomService {

	private static List<Customer> customerList = new ArrayList<>();

	static {
		ArrayList<String> numberList1 = (ArrayList<String>) Stream.of("9861071589", "7867891456", "7745623484")
				.collect(Collectors.toList());
		Customer Customer1 = new Customer(1, "cust1", numberList1);
		ArrayList<String> numberList2 = (ArrayList<String>) Stream.of("9861071589", "7865291457", "7745621485")
				.collect(Collectors.toList());
		Customer Customer2 = new Customer(2, "cust2", numberList2);
		ArrayList<String> numberList3 = (ArrayList<String>) Stream.of("9861071589", "7567891456", "7709623484")
				.collect(Collectors.toList());
		Customer Customer3 = new Customer(3, "cust3", numberList3);
		ArrayList<String> numberList4 = (ArrayList<String>) Stream.of("9861071589", "7867551456", "7745553484")
				.collect(Collectors.toList());
		Customer Customer4 = new Customer(4, "cust4", numberList4);
		customerList.add(Customer1);
		customerList.add(Customer2);
		customerList.add(Customer3);
		customerList.add(Customer4);
	}

	public List<String> retrievePhoneNumbers() {
		List<String> phoneNumberList = new ArrayList<>();
		customerList.forEach(cust -> cust.getphoneNumbersMap().forEach((k, v) -> phoneNumberList.add(k)));
		return phoneNumberList;
	}

	public Customer retrievePhoneNumberCustomer(int custId) {
		for (Customer customer : customerList) {
			if (customer.getId() == custId) {
				return customer;
			} else {
				throw new CustomerNotFoundException("Customer not found in Database.");
			}
		}
		return null;

	}

	public Customer activateNumber(int customerId, String phoneNumber) {
		Customer customer = null;
		if (validatePhoneNumber(phoneNumber)) {
			// check if it's a phone number present in the directory
			if (retrievePhoneNumbers().contains(phoneNumber)) {
				customer = findCustomer(customerId);
				// check if the phone number belongs to that customer or not
				if (customer.getphoneNumbersMap().containsKey(phoneNumber)) {
					// check if the phone number is active or not
					if (!customer.getphoneNumbersMap().get(phoneNumber)) {
						customer.getphoneNumbersMap().put(phoneNumber, Boolean.TRUE);
					} else {
						throw new PhoneNumberException("Phone number already activated.");
					}
				} else {
					throw new PhoneNumberException("Phone number does not belong to the customer.");
				}

			} else {
				throw new PhoneNumberException("Phone number is not present in the directory.");
			}
		} else {
			throw new PhoneNumberException("Not a valid phone number.");
		}
		return customer;
	}

	private Customer findCustomer(int customerId) {
		return customerList.stream().filter(cust -> (cust.getId() == customerId)).findFirst().orElse(null);

	}

	private boolean validatePhoneNumber(String phoneNo) {
		// validate phone numbers of format "1234567890"
		if (phoneNo.matches("\\d{10}"))
			return true;
		// validating phone number with -, . or spaces
		else if (phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}"))
			return true;
		// validating phone number with extension length from 3 to 5
		else if (phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}"))
			return true;
		// validating phone number where area code is in braces ()
		else if (phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}"))
			return true;
		// return false if nothing matches the input
		else
			return false;

	}

}

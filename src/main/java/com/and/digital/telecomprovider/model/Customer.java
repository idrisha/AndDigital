package com.and.digital.telecomprovider.model;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Customer {

	private int id;
	private String name;
	private Map<String, Boolean> phoneNumbersMap;

	public Customer(int id, String name, List<String> phoneNumbersList) {
		super();
		this.id = id;
		this.name = name;
		phoneNumbersMap = phoneNumbersList.stream().collect(Collectors.toMap(Function.identity(), k -> Boolean.FALSE));
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Map<String, Boolean> getphoneNumbersMap() {
		return phoneNumbersMap;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", phoneNumber=" + phoneNumbersMap + "]";
	}
}

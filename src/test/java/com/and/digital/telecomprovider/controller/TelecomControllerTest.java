package com.and.digital.telecomprovider.controller;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.and.digital.telecomprovider.model.Customer;
import com.and.digital.telecomprovider.service.TelecomService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = TelecomController.class, secure = false)
public class TelecomControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TelecomService telecomService;

	@Test
	public void testRetrievePhoneNumbers() throws Exception {

		List<String> phoneNumberList = Arrays.asList(new String[] { "7867891456", "7745623484", "7867551456" });

		Mockito.when(telecomService.retrievePhoneNumbers()).thenReturn(phoneNumberList);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/phoneNumbers").accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		String expected = "[7867891456,7745623484,7867551456]";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString().replace("\"", ""), false);
	}

	@Test
	public void testRetrievePhoneNumbersCustomer() throws Exception {

		List<String> phoneNumberList = Arrays.asList(new String[] { "7867891456", "7745623484", "7867551456" });

		Mockito.when(telecomService.retrievePhoneNumberCustomer(1))
				.thenReturn(new Customer(1, "cust1", phoneNumberList));

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/phoneNumber/1").accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		String expected = "{\"id\":1,\"name\":\"cust1\",\"phoneNumbersMap\":{\"7867891456\":false,\"7745623484\":false,\"7867551456\":false}}";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void testActivateNumber() throws Exception {
		Customer mockCustomer = new Customer(1, "cust1", Arrays.asList("9861071589", "7867891456", "7745623484"));

		Mockito.when(telecomService.activateNumber(Mockito.anyInt(), Mockito.anyString())).thenReturn(mockCustomer);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/activateNumber/1/9861071589")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		System.out.println("here -> " + response);

		assertEquals(HttpStatus.CREATED.value(), response.getStatus());

		assertEquals("http://localhost/activateNumber/1/9861071589", response.getHeader(HttpHeaders.LOCATION));

	}
}

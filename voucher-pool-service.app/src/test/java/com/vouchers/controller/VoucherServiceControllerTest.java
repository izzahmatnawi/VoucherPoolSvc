package com.vouchers.controller;

import com.vouchers.VoucherPoolApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = VoucherPoolApplication.class)
@AutoConfigureMockMvc
public class VoucherServiceControllerTest {

	private static final String VOUCHERS_ENDPOINT = "/vouchers";
	private static final String KEY_SPECIAL_OFFER = "special_offer";
	private static final String KEY_EXPIRY_DATE = "expiry_date";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_VOUCHER_CODE = "voucher_code";

	@Autowired
	private MockMvc mvc;

	@Test
	public void test() {
		//TODO: finish write test
		System.out.println();
	}

/*	@Test
	public void createVoucherCodes_returnSuccess() throws Exception {
		final MockHttpServletRequestBuilder post = MockMvcRequestBuilders
				.post(VOUCHERS_ENDPOINT)
				.accept(MediaType.APPLICATION_JSON)
				.param(KEY_SPECIAL_OFFER, "test");

		System.out.println(mvc.perform(post).andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
	}*/

	/*@Test
	public void useVoucherCode_returnSuccess() throws Exception {
		System.out.println(mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
	}

	@Test
	public void getVoucherCodes_returnVoucherCodes() throws Exception {
		System.out.println(mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
	}*/
}

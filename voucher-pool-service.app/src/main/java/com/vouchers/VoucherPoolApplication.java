package com.vouchers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class VoucherPoolApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(VoucherPoolApplication.class, args);
	}

}

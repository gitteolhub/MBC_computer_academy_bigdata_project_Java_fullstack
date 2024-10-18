package com.javateam.healthyFoodProject;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class SocialIdTest {

	@Test
	public void test() {
		BigDecimal num = new BigDecimal(5);
		String str = num.toString();

		log.info("[SocialIdTest][str]: {}", str);
	}

}

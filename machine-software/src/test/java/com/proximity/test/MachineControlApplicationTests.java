package com.proximity.test;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import com.google.gson.Gson;
import com.proximity.dto.ExtractionDTO;
import com.proximity.dto.OrderDTO;
import com.proximity.enums.PaymentUnits;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test1")
class MachineControlApplicationTests {

	@Autowired
	private ServletWebServerApplicationContext webServerAppCtxt;

	private String server;

	@PostConstruct
	private void post() {
		server = "http://localhost:" + webServerAppCtxt.getWebServer().getPort();
	};

	@Test
	public void testMoneyInMachine_Case1() throws IOException {
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(server + "/machine/cash/available", String.class);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody(), "{\"ammount\":85}");
	}

	@Test
	public void testMoneyInMachine_Case2() throws IOException {
		TestRestTemplate restTemplate = new TestRestTemplate();

		// First check of cash amount
		ResponseEntity<String> r1 = restTemplate.getForEntity(server + "/machine/cash/available", String.class);

		// Make an extraction of 25 cents
		HttpEntity<?> req2 = toEntity(ExtractionDTO.class, "{'ammount': 25}");
		ResponseEntity<Void> r2 = restTemplate.exchange(server + "machine/cash/extraction?pass=abcd", HttpMethod.PUT,
				req2, Void.class);

		// Check of cash amount after extraction
		ResponseEntity<String> r3 = restTemplate.getForEntity(server + "/machine/cash/available", String.class);

		assertEquals(r1.getStatusCode(), HttpStatus.OK);
		assertEquals(r2.getStatusCode(), HttpStatus.OK);
		assertEquals(r3.getStatusCode(), HttpStatus.OK);

		assertEquals(r1.getBody(), "{\"ammount\":85}");
		assertEquals(r3.getBody(), "{\"ammount\":60}");

	}

	@Test
	public void testExtractionMachineBlock() throws IOException {
		TestRestTemplate restTemplate = new TestRestTemplate();

		// Make an extraction of 25 cents
		HttpEntity<?> req2 = toEntity(ExtractionDTO.class, "{'ammount': 25}");
		ResponseEntity<String> r1 = restTemplate.exchange(server + "machine/cash/extraction?pass=wrong", HttpMethod.PUT,
				req2, String.class);

		ResponseEntity<String> r2 = restTemplate.exchange(server + "machine/cash/extraction?pass=wrong", HttpMethod.PUT,
				req2, String.class);
		
		ResponseEntity<String> r3 = restTemplate.exchange(server + "machine/cash/extraction?pass=wrong", HttpMethod.PUT,
				req2, String.class);
		
		ResponseEntity<String> r4 = restTemplate.exchange(server + "machine/cash/extraction?pass=wrong", HttpMethod.PUT,
				req2, String.class);

		assertEquals(r1.getBody(), "{ message : La contrasena ingresada es invalida.  }");
		assertEquals(r2.getBody(), "{ message : La contrasena ingresada es invalida.  }");
		assertEquals(r3.getBody(), "{ message : La contrasena ingresada es invalida.  }");
		assertEquals(r4.getBody(), "{ message : La maquina se encuentra bloqueada.  }");
		
	}
	
	@Test
	public void testExtractionMachineBlockAndRecoverOK() throws IOException {
		TestRestTemplate restTemplate = new TestRestTemplate();

		// Make an extraction of 25 cents
		HttpEntity<?> req2 = toEntity(ExtractionDTO.class, "{'ammount': 25}");
		ResponseEntity<String> r1 = restTemplate.exchange(server + "machine/cash/extraction?pass=wrong", HttpMethod.PUT,
				req2, String.class);

		ResponseEntity<String> r2 = restTemplate.exchange(server + "machine/cash/extraction?pass=wrong", HttpMethod.PUT,
				req2, String.class);
		
		ResponseEntity<String> r3 = restTemplate.exchange(server + "machine/cash/extraction?pass=abcd", HttpMethod.PUT,
				req2, String.class);
		
		ResponseEntity<String> r4 = restTemplate.exchange(server + "machine/cash/extraction?pass=wrong", HttpMethod.PUT,
				req2, String.class);

		ResponseEntity<String> r5 = restTemplate.exchange(server + "machine/cash/extraction?pass=abcd", HttpMethod.PUT,
				req2, String.class);
		
		assertEquals(r1.getBody(), "{ message : La contrasena ingresada es invalida.  }");
		assertEquals(r2.getBody(), "{ message : La contrasena ingresada es invalida.  }");
		assertEquals(r3.getStatusCode(), HttpStatus.OK);
		assertEquals(r4.getBody(), "{ message : La contrasena ingresada es invalida.  }");
		assertEquals(r5.getStatusCode(), HttpStatus.OK);
		
	}
	
	

	@Test
	public void testListAll() throws IOException {
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(server + "/machine/cash", String.class);
		System.out.println("holaa");
	}

	@Test
	public void testBuyOk() throws IOException {
		TestRestTemplate restTemplate = new TestRestTemplate();

		OrderDTO req = new OrderDTO();
		req.setItemId(1);
		req.setPayment(Arrays.asList(PaymentUnits.COIN_50_CENTS, PaymentUnits.COIN_25_CENTS));
		req.setQuantity(1);

		ResponseEntity<String> response = restTemplate.postForEntity(server + "/machine/buy", req, String.class);
		if (!response.getStatusCode().equals(HttpStatus.OK))
			fail();
	}


	@Test
	public void testBuyInvalidProd() throws IOException {
		TestRestTemplate restTemplate = new TestRestTemplate();

		OrderDTO req = new OrderDTO();
		req.setItemId(9999999);
		req.setPayment(Arrays.asList(PaymentUnits.COIN_50_CENTS, PaymentUnits.COIN_25_CENTS));
		req.setQuantity(1);

		ResponseEntity<String> response = restTemplate.postForEntity(server + "/machine/buy", req, String.class);
		if (!response.getStatusCode().equals(HttpStatus.BAD_REQUEST))
			fail();
	}
	

	@Test
	public void testBuyInvalidQuatity() throws IOException {
		TestRestTemplate restTemplate = new TestRestTemplate();

		OrderDTO req = new OrderDTO();
		req.setItemId(1);
		req.setPayment(Arrays.asList(PaymentUnits.COIN_50_CENTS, PaymentUnits.COIN_25_CENTS));
		req.setQuantity(-50);

		ResponseEntity<String> response = restTemplate.postForEntity(server + "/machine/buy", req, String.class);
		if (!response.getStatusCode().equals(HttpStatus.BAD_REQUEST))
			fail();
	}
	
	@Test
	public void testBuyInvalidMixPayment() throws IOException {
		TestRestTemplate restTemplate = new TestRestTemplate();

		OrderDTO req = new OrderDTO();
		req.setItemId(1);
		req.setPayment(Arrays.asList(PaymentUnits.COIN_50_CENTS, PaymentUnits.COIN_25_CENTS, PaymentUnits.CREDIT_CARD));
		req.setQuantity(1);

		ResponseEntity<String> response = restTemplate.postForEntity(server + "/machine/buy", req, String.class);
		if (!response.getStatusCode().equals(HttpStatus.BAD_REQUEST))
			fail();
	}
	
	@Test
	public void testBuyNoPayment() throws IOException {
		TestRestTemplate restTemplate = new TestRestTemplate();

		OrderDTO req = new OrderDTO();
		req.setItemId(1);
		req.setQuantity(1);

		ResponseEntity<String> response = restTemplate.postForEntity(server + "/machine/buy", req, String.class);
		if (!response.getStatusCode().equals(HttpStatus.BAD_REQUEST))
			fail();
	}

	@Test
	public void testOutOfStock() throws IOException {
		TestRestTemplate restTemplate = new TestRestTemplate();

		OrderDTO req = new OrderDTO();
		req.setItemId(1);
		req.setQuantity(1);
		req.setPayment(Arrays.asList(PaymentUnits.COIN_50_CENTS, PaymentUnits.COIN_25_CENTS));

		ResponseEntity<String> response = restTemplate.postForEntity(server + "/machine/buy", req, String.class);
		if (!response.getStatusCode().equals(HttpStatus.OK))
			fail();
		
		response = restTemplate.postForEntity(server + "/machine/buy", req, String.class);
		if (!response.getStatusCode().equals(HttpStatus.BAD_REQUEST))
			fail();
	}
	
	public static HttpEntity<?> toEntity(Class<?> c, String text) {
		Gson g = new Gson();
		return new HttpEntity<>(g.fromJson(text.replaceAll("'", "\""), c));
	}
}

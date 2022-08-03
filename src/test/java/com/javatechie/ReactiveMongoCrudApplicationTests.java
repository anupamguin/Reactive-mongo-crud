package com.javatechie;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.javatechie.controller.ProductController;
import com.javatechie.dto.ProductDto;
import com.javatechie.service.ProductService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

//@SpringBootTest
@WebFluxTest(ProductController.class)
class ReactiveMongoCrudApplicationTests {

	@Autowired
	private WebTestClient client;
	
	@MockBean
	private ProductService service;
	
	@Test
	public void addProductTest() {
		
		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("23","Super",1,1000)).log();
		when(service.saveProduct(productDtoMono)).thenReturn(productDtoMono);
		
		client.post().uri("/products/save")
			.body(Mono.just(productDtoMono),ProductDto.class)
			.exchange()
			.expectStatus()
			.isOk();
	}
	
	@Test
	public void getProductsTest() {
		
		Flux<ProductDto> productDtoFlux = Flux.just(new ProductDto("32", "Thirty", 10, 4566),
													new ProductDto("33", "Fourty", 20, 1222));
		
		when(service.getProducts()).thenReturn(productDtoFlux);
		
		Flux<ProductDto> responseBody = client.get().uri("/products")
			.exchange()
			.expectStatus().isOk()
			.returnResult(ProductDto.class)
			.getResponseBody();
		
		responseBody.subscribe(System.out::println);
		
		StepVerifier.create(responseBody)
			.expectSubscription()
			.expectNext(new ProductDto("32", "Thirty", 10, 4566))
			.expectNext(new ProductDto("33", "Fourty", 20, 1222))
			.verifyComplete();
	}
	
	@Test
	public void getProductTest() {
		Mono<ProductDto> mono = Mono.just(new ProductDto("32", "Thirty", 10, 4566));
		when(service.getProduct(any())).thenReturn(mono);
		
		Flux<ProductDto> resp = client.get().uri("/products/32")
			.exchange()
			.expectStatus().isOk()
			.returnResult(ProductDto.class)
			.getResponseBody();
		
		StepVerifier.create(resp)
			.expectSubscription()
			.expectNextMatches(p -> p.getName().equals("Thirty"))
			.verifyComplete();
	}
	
	@Test
	public void updateProductTest() {
		Mono<ProductDto> resp = Mono.just(new ProductDto("32", "Thirty", 10, 4566));
		when(service.updateProduct(resp, "32")).thenReturn(resp);
		
		Flux<ProductDto> responseBody = client.put().uri("/products/update/32")
			.body(Mono.just(resp),ProductDto.class)
			.exchange()
			.expectStatus().isOk()
			.returnResult(ProductDto.class)
			.getResponseBody();
			
//		StepVerifier.create(responseBody)
//			.expectSubscription()
//			.expectNextMatches(s -> s.getPrice() == 4566)
//			.verifyComplete();
	}
	
	@Test
	public void deleteProductTest() {
		given(service.deleteProduct(any())).willReturn(Mono.empty());
		
		client.delete().uri("/products/delete/32")
				.exchange()
				.expectStatus().isOk();
	}
}

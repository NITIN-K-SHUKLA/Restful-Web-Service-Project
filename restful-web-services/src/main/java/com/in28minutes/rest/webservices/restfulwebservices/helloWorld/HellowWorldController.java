package com.in28minutes.rest.webservices.restfulwebservices.helloWorld;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HellowWorldController {

	@GetMapping(path = "/hello-world")
	public String HelloWorld() {
		return "Hello-World";
	}
	
	@GetMapping(path = "/hello-world-bean")
	public HelloWorldBean getBean() {
		return new HelloWorldBean("Hello World in bean");
	}
	
	@GetMapping(path = "/hello-world-{name}")
	public String giveName(@PathVariable String name)
	{
		return "Hi " + name + "Welcome on this rest api page";
	}
}

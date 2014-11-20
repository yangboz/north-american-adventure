package com.rushucloud.eip.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rushucloud.eip.dto.JsonString;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
public class HelloController {
	@RequestMapping(method = RequestMethod.GET, value = "hello")
	@ApiOperation(httpMethod = "GET", value = "Greetings from Swagger")
	public JsonString sayHello() {
		return new JsonString("world!");
	}

}
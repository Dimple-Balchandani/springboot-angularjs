package com.vs.BaseProject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.vs.BaseProject.utilities.Constants;

import io.swagger.annotations.ApiOperation;

@Controller
public class IndexController {
	
	@ApiOperation(value = Constants.MAIN_DESCRIPTION)
	@RequestMapping(value = "/")
	public String helloWorld(){
		return "index";
	}
}

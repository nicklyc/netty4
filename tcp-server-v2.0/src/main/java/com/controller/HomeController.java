package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeController {
	/**
	 * 首页
	 * 
	 */
	@GetMapping("/index")
	public String  index(){
		return "index";
	}
	/**
	 * websocket
	 * 
	 */
	@GetMapping("/websocket")
	public String  websocket(){
		return "websocket";
	}
}

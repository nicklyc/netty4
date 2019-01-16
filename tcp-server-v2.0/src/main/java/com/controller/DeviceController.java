package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.handler.SendMessageHandler;
import com.server.SessionChannle;
import com.util.HexString;

/**
 * 与设备相关的的控制器
 * @author admin
 *
 */
@RestController
public class DeviceController {
	@Autowired
	private SessionChannle sessionChannle;
	@Autowired
	private SendMessageHandler sendMessage;
	/**
	 * 查询所有的在线设备
	 * 
	 */
	@GetMapping("/getAllOnlieDevice")
	
	public List getAllOnlieDevice(Model model ){
		model.addAttribute("oneLineDevices", sessionChannle.getAllOnline());
		
		return sessionChannle.getAllOnline();
	}
	
	/**
	 * 向指定的设备下发指令
	 * @param deviceId 设备id
	 * @param  order   指令
	 * 
	 */
	@PostMapping("/instructOrderToDevice")
	public void instructOrderToDevice (String deviceId,String order){
		 //TODO service调用
		sendMessage.sendMess(HexString.hexStringToBytes(order), deviceId);
		
	}
	
	
	/**
	 * 向批量设备下发指令
	 * @param deviceId
	 */
	@GetMapping("/instructOrderToDevices")
	public void instructOrderToDevices (String[] deviceIds){
		
	}
}

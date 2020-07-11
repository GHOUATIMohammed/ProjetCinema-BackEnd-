package org.sid.cinema.security;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {
	@GetMapping("/notAuthorized")
	public String error()
{
	return "notAuthorized";
}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) throws ServletException
{
		request.logout();
	return "redirect:";
}
	
}

package org.owasp.webgoat.challenges.challenge1;

import java.io.IOException;
import java.security.SecureRandom;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;

@WebServlet(name = "ImageServlet", urlPatterns = "/challenge/logo")
public class ImageServlet extends HttpServlet {
	
	private static final long serialVersionUID = 9132775506936676850L;
	static final public int PINCODE = new SecureRandom().nextInt(10000);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		byte[] in = new ClassPathResource("images/webgoat2.png").getInputStream().readAllBytes();
		
		String pincode = String.format("%04d", PINCODE);
		
		in[81216]=(byte) pincode.charAt(0);
		in[81217]=(byte) pincode.charAt(1);
		in[81218]=(byte) pincode.charAt(2);
		in[81219]=(byte) pincode.charAt(3);
		
	    response.setContentType(MediaType.IMAGE_PNG_VALUE);
	    FileCopyUtils.copy(in, response.getOutputStream());
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
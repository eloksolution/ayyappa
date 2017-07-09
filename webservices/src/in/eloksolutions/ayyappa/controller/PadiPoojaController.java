package in.eloksolutions.ayyappa.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import in.eloksolutions.ayyappa.model.Group;
import in.eloksolutions.ayyappa.model.Padipooja;
import in.eloksolutions.ayyappa.service.PadipoojaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/padipooja")
public class PadiPoojaController {
	@Autowired
	PadipoojaService padipoojaService;
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addPadipooja(@RequestBody Padipooja padipooja){
		System.out.println("Request is coming "+padipooja);
		padipoojaService.addPadipooja(padipooja);
		return "success";
	}
		
	@ResponseBody
	@RequestMapping(value = "/getpadipooja", method = RequestMethod.GET)
	public List getPadipooja( HttpServletRequest request){
		System.out.println("Request padipooja xxxx is coming "+request);
		List<Padipooja> padipoojaCol = padipoojaService.getPadipooja();
		System.out.println("Colection is coming "+padipoojaCol);
		return padipoojaCol;
	}
		

}

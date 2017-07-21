package in.eloksolutions.ayyappa.controller;

import in.eloksolutions.ayyappa.model.Group;
import in.eloksolutions.ayyappa.model.Padipooja;
import in.eloksolutions.ayyappa.service.PadipoojaService;
import in.eloksolutions.ayyappa.vo.GroupVO;
import in.eloksolutions.ayyappa.vo.PadiMember;
import in.eloksolutions.ayyappa.vo.PadipoojaVo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
	@RequestMapping(value = "/getpoojas", method = RequestMethod.GET)
	public List getPadipooja( HttpServletRequest request){
		System.out.println("Request padipooja xxxx is coming "+request);
		List<Padipooja> padipoojaCol = padipoojaService.getPadipooja();
		System.out.println("Colection is coming "+padipoojaCol);
		return padipoojaCol;
	}
	
	@ResponseBody
	@RequestMapping(value = "/padipoojaEdit/{padipoojaid}")
	public Padipooja memberEdit(@PathVariable("padipoojaid") String padipoojaid, HttpServletRequest request) {
		System.out.println("Fetching all padipoojaid with X00001 memberEdit " + padipoojaid);
		Padipooja  groupedit = padipoojaService.searchById(padipoojaid);
		System.out.println("Fetching all Group details " + groupedit);
		return groupedit;
	}
		
	@ResponseBody
	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public String join( @RequestBody PadiMember padiMember){
		System.out.println("Request xxxx is padiMember  "+padiMember);
		String msg = padipoojaService.join(padiMember);
		return msg;
	}
	
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updatePadipooja(@RequestBody PadipoojaVo padiVO){
		System.out.println("Request is coming padiVO "+padiVO);
		Padipooja uPadi=new Padipooja(padiVO.getPadipoojaId(),padiVO.getEventName(),padiVO.getLocation(),padiVO.getDescription(),padiVO.getDate(),padiVO.getTime(),padiVO.getMemId(),padiVO.getName());
		 padipoojaService.update(uPadi);
		 System.out.println("Updating  is coming groupVO "+uPadi);
		 return "success";
	}
}

package in.eloksolutions.ayyappa.service;

import java.util.List;

import in.eloksolutions.ayyappa.dao.PadipojaDAO;
import in.eloksolutions.ayyappa.model.Padipooja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("/padipoojaService")
public class PadipoojaService {
	@Autowired
	PadipojaDAO padipoojaDao ;

	public void addPadipooja(Padipooja padipooja) {
		padipoojaDao.addPadipooja(padipooja);
	}

	public List<Padipooja> getPadipooja() {
		
		return padipoojaDao.getPadipooja();
	}
	
}

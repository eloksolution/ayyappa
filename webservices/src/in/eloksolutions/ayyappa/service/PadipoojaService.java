package in.eloksolutions.ayyappa.service;

import java.util.List;

import in.eloksolutions.ayyappa.dao.PadipojaDAO;
import in.eloksolutions.ayyappa.model.Padipooja;
import in.eloksolutions.ayyappa.vo.GroupMember;
import in.eloksolutions.ayyappa.vo.PadiMember;

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

	public Padipooja searchById(String padipoojaid) {
		
		return padipoojaDao.searchById(padipoojaid);
	}
	public String join(PadiMember padiMember ) {
		return padipoojaDao.join(padiMember);
	}

	public String update(Padipooja uPadi) {
		return padipoojaDao.update(uPadi);
		
	}
}

package in.eloksolutions.ayyappa.service;

import in.eloksolutions.ayyappa.dao.PadipojaDAO;
import in.eloksolutions.ayyappa.model.Padipooja;
import in.eloksolutions.ayyappa.vo.DeekshaVO;
import in.eloksolutions.ayyappa.vo.PadiMember;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("/padipoojaService")
public class PadipoojaService {
	@Autowired
	PadipojaDAO padipoojaDao ;

	public String addPadipooja(Padipooja padipooja) {
		return padipoojaDao.addPadipooja(padipooja);
	}

	public List<Padipooja> getTopPadipooja(String userId) {
		return padipoojaDao.getTOPPadipooja(userId);
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

	public String leave(PadiMember padiMember) {
		return padipoojaDao.leave(padiMember);
	}

	public List<Padipooja> getPadipooja(String userId) {
		return padipoojaDao.getPadiPoojas(userId);
	}

	
}

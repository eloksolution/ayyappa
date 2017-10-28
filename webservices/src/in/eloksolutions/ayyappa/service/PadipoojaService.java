package in.eloksolutions.ayyappa.service;

import in.eloksolutions.ayyappa.dao.PadipojaDAO;
import in.eloksolutions.ayyappa.dao.UserDAO;
import in.eloksolutions.ayyappa.model.Padipooja;
import in.eloksolutions.ayyappa.model.User;
import in.eloksolutions.ayyappa.vo.PadiMember;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("/padipoojaService")
public class PadipoojaService {
	@Autowired
	PadipojaDAO padipoojaDao ;
	@Autowired
	UserDAO userDAO;	
	public String addPadipooja(Padipooja padipooja) {
		User user=userDAO.searchById(padipooja.getMemId());
		String id= padipoojaDao.addPadipooja(padipooja,user);
		padipooja.setPadipoojaId(id);
		userDAO.addUserPadis(padipooja);
		return id;
	}

	public List<Padipooja> getTopPadipooja(String userId) {
		return padipoojaDao.getTOPPadipooja(userId);
	}

	public Padipooja searchById(String padipoojaid) {
		return padipoojaDao.searchById(padipoojaid);
	}
	public String join(PadiMember padiMember ) {
		padiMember.setImgPath(userDAO.getImagePath(padiMember.getUserId()));
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

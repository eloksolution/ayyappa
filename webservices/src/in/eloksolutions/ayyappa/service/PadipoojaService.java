package in.eloksolutions.ayyappa.service;

import in.eloksolutions.ayyappa.dao.PadipojaDAO;
import in.eloksolutions.ayyappa.model.Padipooja;
import in.eloksolutions.ayyappa.vo.PadiMember;

import java.util.List;

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

<<<<<<< HEAD
	public String update(Padipooja uPadi) {
		return padipoojaDao.update(uPadi);
=======
	public void update(Padipooja uPadi) {
		// TODO Auto-generated method stub
>>>>>>> 73af88f715561ee3a6f02b296589559b1fd56a2e
		
	}
}

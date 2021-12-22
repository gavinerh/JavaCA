package edu.nus.java_ca.service;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import edu.nus.java_ca.model.Holidays;
import edu.nus.java_ca.repository.HolidayRepo;

@Service
public class HolidayServiceImpl implements HolidayService {
	@Autowired
	HolidayRepo hrepo;

	@Override
	@Transactional
	public ArrayList<Holidays> findAll() {
		// TODO Auto-generated method stub
		return (ArrayList<Holidays>)hrepo.findAll();
	}

	@Override
	@Transactional
	public Holidays findById(Integer i) {
		// TODO Auto-generated method stub
		return hrepo.findById(i).orElse(null);
	}

	@Override
	@Transactional
	public Holidays findByHoliday(LocalDate d) {
		// TODO Auto-generated method stub
		return hrepo.findByHoliday(d);
	}

	@Override
	@Transactional
	@Modifying
	public void deleteById(Integer i) {
		// TODO Auto-generated method stub
		hrepo.deleteById(i);
	}

	@Override
	@Transactional
	public void createHoliday(Holidays h) {
		// TODO Auto-generated method stub
		hrepo.saveAndFlush(h);
	}
	

}

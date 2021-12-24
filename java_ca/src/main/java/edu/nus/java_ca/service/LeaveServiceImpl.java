package edu.nus.java_ca.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import edu.nus.java_ca.model.Department;
import edu.nus.java_ca.model.Leave;
import edu.nus.java_ca.model.LeaveBalance;
import edu.nus.java_ca.model.LeaveStatus;
import edu.nus.java_ca.model.User;
import edu.nus.java_ca.repository.HolidayRepo;
import edu.nus.java_ca.repository.LeaveBalanceRepo;
import edu.nus.java_ca.repository.LeaveRepo;
import edu.nus.java_ca.repository.UserRepository;

@Service // need to impl component type to be recognized as spring bean
public class LeaveServiceImpl implements LeaveService {

	@Autowired
	LeaveRepo lrepo;

	final Set<String> weekends = Set.of("SATURDAY", "SUNDAY");

	@Autowired
	UserRepository uRepo;
	@Autowired
	LeaveBalanceRepo lbrepo;
	@Autowired
	HolidayService hservice;

//	public LeaveServiceImpl() {
//		this.holidays = (ArrayList)hrepo.findAll();
//	}
	// Base function
	@Transactional
	public List<Leave> listAllLeaves() {
		return lrepo.findAll();
	}

	@Override
	@Transactional
	public Leave createLeave(Leave l) {
		// TODO Auto-generated method stub
		return lrepo.saveAndFlush(l);
	}

	@Override
	@Transactional
	public Leave changeLeave(Leave l) {
		// TODO Auto-generated method stub
		return lrepo.saveAndFlush(l);
	}

	@Transactional
	public void cancelLeave(Leave l) {
		l.setStatus(LeaveStatus.CANCELLED);
		lrepo.save(l);
	}

	//NEW QUERY
	@Transactional
	public List<Leave> findLeavesByYearandMonth(int yy, int mm){
		List<Leave> LeavesByMMYY = lrepo.getByYearandMonth(yy, mm);
		return LeavesByMMYY;			
	}

	// Manager's function
	@Transactional
	public List<Leave> listLeavesByUserId(Long id) {

		User user1 = uRepo.findByUserId(id);

		List<Leave> EmplLeave = lrepo.findByUser(user1);
		return EmplLeave;
	}

	@Override
	@Transactional
	public ArrayList<Leave> findAppliedLeaves() {
		// TODO Auto-generated method stub
		return lrepo.findAppliedLeaves();
	}

	@Transactional
	public List<Leave> listLeaveToApprove() {
		List<Leave> LeavetoApprove = lrepo.findLeaveToApprove(LeaveStatus.APPLIED, 
				LeaveStatus.UPDATED);
		return LeavetoApprove;
	}	
	@Transactional
	public List<Leave> listLeaveToApprove(Department d) {
		List<Leave> LeavetoApprove = lrepo.findLeaveToApprove(LeaveStatus.APPLIED, LeaveStatus.UPDATED, d);
		return LeavetoApprove;
	}

	@Modifying // if its CUD need to inform server its not a readonly query
	@Transactional
	public void approveLeave(Leave l) {
		l.setStatus(LeaveStatus.APPROVED);
		lrepo.save(l);
	}

	@Modifying
	@Transactional
	public void rejectLeave(Leave l) {
		l.setStatus(LeaveStatus.REJECTED);
		lrepo.save(l);
	}

	@Transactional
	public Leave findLeaveById(Long id) {
		return lrepo.findLeaveById(id);
	}

	@Transactional
	public Long countLeaves(LocalDate s, LocalDate e, User u) {
		Long count;
		ArrayList<Leave> c = lrepo.findDupeLeaveByUser(u);
		ArrayList<Leave> le = c.stream()
				.filter(l-> !(l.getStatus().equals(LeaveStatus.DELETED)||
					l.getStatus().equals(LeaveStatus.CANCELLED)))
				.collect(Collectors
	                    .toCollection(ArrayList::new));
		ArrayList<LocalDate> userleave = new ArrayList<>();
		for(Leave lea:le) {
			userleave.addAll(lea.getStartDate().datesUntil(lea.getEndDate().plusDays(1))
					.collect(Collectors
		                    .toCollection(ArrayList::new)));
		}
				
		ArrayList<LocalDate> holidays = hservice.findHolidays();
		count = s.datesUntil(e.plusDays(1)).count();
		if (count <= 14) {
			count = s.datesUntil(e.plusDays(1))
					.filter(t -> !weekends.contains(t.getDayOfWeek().name()))
					.filter(t -> !holidays.contains(t))
					.filter(t-> !userleave.contains(t))
					.count();
		}
		return count;
	}

	@Transactional
	public Boolean checkDupes(LocalDate s, LocalDate e, User u) {
		ArrayList<Leave> c = lrepo.findDupeLeaveByUser(u);
		ArrayList<Leave> le = c.stream()
				.filter(l-> !(l.getStatus().equals(LeaveStatus.DELETED)||
					l.getStatus().equals(LeaveStatus.CANCELLED)))
				.collect(Collectors
	                    .toCollection(ArrayList::new));
		Long count = le.stream()
				.filter(x -> (x.getStartDate().isBefore(s) && x.getEndDate().isAfter(e))
						|| (x.getStartDate().isEqual(s) && x.getEndDate().isEqual(e))
						|| (x.getStartDate().isEqual(s) && x.getEndDate().isAfter(e))
						|| (x.getEndDate().isEqual(e) && x.getStartDate().isBefore(s)))
				.count();
		if (count > 0) {
			return true;
		}
		return false;
	}

	@Override
	@Modifying
	@Transactional
	public Boolean deductleave(Leave l, User u, Integer i) {
		String s = l.getType();
		LeaveBalance lb = lbrepo.findTop1ByUserAndLeavetype(u, l.getType());
		Integer in = lb.getBalance();
		Integer bal = in - i;
		if (bal >= 0) {
			lb.setBalance(bal);
			lbrepo.saveAndFlush(lb);
			return true;
		}
		return false;
	}
	@Override
	@Modifying
	@Transactional
	public Boolean refundleave(Leave l, User u, Integer i) {
		String s = l.getType();
		LeaveBalance lb = lbrepo.findTop1ByUserAndLeavetype(u, l.getType());
		Integer in = lb.getBalance();
		Integer bal = in + i;
		lb.setBalance(bal);
		lbrepo.saveAndFlush(lb);
		return true;
	}

	@Override
	@Modifying
	@Transactional
	public Boolean refundleave(String s, User u, Integer i) {
		// TODO Auto-generated method stub
		LeaveBalance lb = lbrepo.findTop1ByUserAndLeavetype(u, s);
		Integer in = lb.getBalance();
		Integer bal = in + i;
		lb.setBalance(bal);
		lbrepo.saveAndFlush(lb);
		return true;
	}

	@Override
	@Transactional
	public Page<Leave> findByUser(User u, Pageable p) {
		// TODO Auto-generated method stub

		return lrepo.findBypageUser(u, p);
	}


	@Override
	public List<Leave> listAllLeaves1() {
		List<Leave> list = lrepo.findAll();
		return list;
	}

	@Override
	public List<Leave> getAllLeaves(int pageNo, int pageSize, User u) {
		Pageable paging = PageRequest.of(pageNo, pageSize);

		Page<Leave> pageResult = findByUser(u, paging);

		List<Leave> list = pageResult.getContent();

		return list;
	}

	@Override
	public ArrayList<Leave> findByUser(User u) {
		// TODO Auto-generated method stub
		return lrepo.findByUser(u);
	}

}

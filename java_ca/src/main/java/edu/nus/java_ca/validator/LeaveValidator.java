package edu.nus.java_ca.validator;

import java.util.Calendar;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edu.nus.java_ca.model.Leave;

public class LeaveValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Leave.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors e) {
		// TODO Auto-generated method stub
		Leave l = (Leave) target;

		if ((l.getStartDate()!=null && l.getEndDate()!=null)&&(l.getStartDate().compareTo(l.getEndDate())>=0)) {
		
			e.rejectValue("startDate", "error.startDate", "**StartDate must be Before EndDate**");
			e.rejectValue("endDate", "error.endDate", "**EndDate must be After StartDate**");
		}
	
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "reason", "error.reason","**Reason Is Required.**");
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "startDate", "error.startDate", "**Start Date is required.**");
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "endDate", "error.endDate", "**End Date is required.**");
		
		if(l.getStartDate()!=null) {
			int i = l.getStartDate().getDayOfWeek().getValue();
			if ((i==6||i==7)) {
			e.rejectValue("startDate", "error.startDate", "**StartDate must be Weekdays**");}
		}
		
		if(l.getEndDate()!=null) {
			int j = l.getEndDate().getDayOfWeek().getValue();
			if (j==6||j==7) {
				e.rejectValue("endDate", "error.endDate", "**EndDate must be Weekdays**");}
		}
	}

}

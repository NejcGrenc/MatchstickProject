package grenc.masters.uservalidation;

import javax.servlet.http.HttpServletRequest;

import grenc.masters.database.dao.SubjectDAO;
import grenc.masters.database.entities.Subject;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;

@Bean
public class ValidateUserSession
{
	@InsertBean
	private SubjectDAO subjectDao;
	
	public boolean isFreshIP(HttpServletRequest request)
	{
		String ip = IpAddress.getClientIpAddress(request);
		
		if (ip == null)
			return true;  // Cannot determine IP - probably a software issue
//		if (ip.equals("0:0:0:0:0:0:0:1"))
//			return true;  // Local IP - probably testing on local machine
			
		Subject existingSubject = this.subjectDao.findSubjectByIp(ip);		
		return existingSubject == null;
	}
	
	public Subject updateSubject(Subject subject, HttpServletRequest request)
	{
		BrowserDetails browserData = new BrowserDetails(request);
		String ip = IpAddress.getClientIpAddress(request);
		Geolocation geolocation = new Geolocation(ip);
		
		String address;
		if (geolocation.isDataFound())
			address = "" + geolocation.getContinentName() + ", " + geolocation.getCountryName() + ", " 
						 + geolocation.getCityName() + ", " + geolocation.getState() + ", " + geolocation.getPostal();
		else 
			address = "null";
		
		return subjectDao.updateSubjectFetchedData(subject.getId(), geolocation.getIp(), address, browserData.getOs(), browserData.getBrowser());
	}
}

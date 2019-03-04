package grenc.masters.uservalidation;

import javax.servlet.http.HttpServletRequest;

import grenc.masters.database.dao.SubjectDAO;
import grenc.masters.database.entities.Subject;


public class ValidateUserSession
{
	
	private SubjectDAO subjectDao;
	
	private HttpServletRequest request;
	
	public ValidateUserSession(HttpServletRequest request)
	{
		this.request = request;
		this.subjectDao = SubjectDAO.getInstance();
	}
	
	
	public boolean isFreshIP()
	{
		String ip = IpAddress.getClientIpAddress(request);
		
		if (ip == null)
			return true;  // Cannot determine IP - probably a software issue
//		if (ip.equals("0:0:0:0:0:0:0:1"))
//			return true;  // Local IP - probably testing on local machine
			
		Subject existingSubject = this.subjectDao.findSubjectByIp(ip);		
		return existingSubject == null;
	}
	
	@Deprecated
	public boolean subjectNameAlreadyExists(String subjectName)
	{
		if (subjectName != null)
		{
			return subjectDao.findSubjectsByNameAndComplete(subjectName, true).size() > 0;
		}
		return false;
	}
	
	public Subject updateSubject(Subject subject)
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

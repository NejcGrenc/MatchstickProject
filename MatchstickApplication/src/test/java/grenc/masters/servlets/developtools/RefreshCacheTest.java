package grenc.masters.servlets.developtools;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.mockito.Mockito;

import grenc.masters.servlets.developtools.RefreshCache.RefreshCacheData;
import grenc.masters.webpage.builder.WebpageBuilder;

public class RefreshCacheTest
{

	// Subject
	RefreshCache refreshCache = new RefreshCache();
	
	@Test
	public void shouldDeleteOldData()
	{
		// given: a valid and obsolete data object
		RefreshCache.RefreshCacheData validData = refreshCache.new RefreshCacheData();
		validData.originalTime = LocalDateTime.now().minus(1, ChronoUnit.MINUTES);
	
		RefreshCache.RefreshCacheData obsoleteData = refreshCache.new RefreshCacheData();
		obsoleteData.originalTime = LocalDateTime.now().minus(100, ChronoUnit.MINUTES);
		
		HashMap<String, RefreshCacheData> cache = new HashMap<>();
		cache.put("valid", validData);
		cache.put("obsolete", obsoleteData);
		refreshCache = new RefreshCache(cache);
		
		// when: obsolete data gets deleted
		refreshCache.deleteTimeoutedData();
		
		// then: only obsolete data is really deleted
		assertTrue(refreshCache.getHashedSessions().contains("valid"));
		assertFalse(refreshCache.getHashedSessions().contains("obsolete"));
	}
	
	
	@Test
	public void shouldDetemineRefreshDataRelevance()
	{
		// given: an original request cached
		HashMap<String, String> parameters = new HashMap<>();
		parameters.put("session", "session123");
		parameters.put("data", "123");
		HttpServletRequest mockedRequest = mockRequest(parameters);
		WebpageBuilder mockedResponse = Mockito.mock(WebpageBuilder.class);		
		refreshCache.cacheResponse(mockedRequest, mockedResponse);
		
		// and: a repeated request
		HashMap<String, String> repeatedParameters = new HashMap<>();
		repeatedParameters.put("session", "session123");
		repeatedParameters.put("data", "123");
		HttpServletRequest repeatedRequest = mockRequest(repeatedParameters);
		
		// and: a different request
		HashMap<String, String> differentParameters = new HashMap<>();
		differentParameters.put("session", "session123");
		differentParameters.put("data", "555");
		differentParameters.put("additional_data", "333");
		HttpServletRequest differentRequest = mockRequest(differentParameters);
		
		// then: repeated request should have the same response
		assertTrue(refreshCache.isEligibleForRefresh(repeatedRequest));
		
		// and: a different request should not point to the same response
		assertFalse(refreshCache.isEligibleForRefresh(differentRequest));
	}
	
	private HttpServletRequest mockRequest(HashMap<String, String> parameters)
	{
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		
	    Enumeration<String> parameterNames = Collections.enumeration(parameters.keySet());
		Mockito.doReturn(parameterNames).when(request).getParameterNames();
		
		for (String key: parameters.keySet())
			Mockito.doReturn(parameters.get(key)).when(request).getParameter(key);
		
		return request;
	}
}

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
		HashMap<String, String> attributes = new HashMap<>();
		attributes.put("session", "session123");
		attributes.put("data", "123");
		HttpServletRequest mockedRequest = mockRequest(attributes);
		WebpageBuilder mockedResponse = Mockito.mock(WebpageBuilder.class);		
		refreshCache.cacheResponse(mockedRequest, mockedResponse);
		
		// and: a repeated request
		HashMap<String, String> repeatedAttributes = new HashMap<>();
		repeatedAttributes.put("session", "session123");
		repeatedAttributes.put("data", "123");
		HttpServletRequest repeatedRequest = mockRequest(repeatedAttributes);
		
		// and: a different request
		HashMap<String, String> differentAttributes = new HashMap<>();
		differentAttributes.put("session", "session123");
		differentAttributes.put("data", "555");
		differentAttributes.put("additional_data", "333");
		HttpServletRequest differentRequest = mockRequest(differentAttributes);
		
		// then: repeated request should have the same response
		assertTrue(refreshCache.isEligibleForRefresh(repeatedRequest));
		
		// and: a different request should not point to the same response
		assertFalse(refreshCache.isEligibleForRefresh(differentRequest));
	}
	
	private HttpServletRequest mockRequest(HashMap<String, String> attributes)
	{
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		
	    Enumeration<String> attributeNames = Collections.enumeration(attributes.keySet());
		Mockito.doReturn(attributeNames).when(request).getAttributeNames();
		
		for (String key: attributes.keySet())
			Mockito.doReturn(attributes.get(key)).when(request).getAttribute(key);
		
		return request;
	}
}

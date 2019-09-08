package grenc.masters.servlets.developtools;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.simpleton.annotation.Bean;

@Bean
public class RefreshCache
{
	private static final int resreshTimeoutDelayInMinutes = 30;
	
	private HashMap<String, RefreshCacheData> cache = new HashMap<>();

	public RefreshCache() {}
	public RefreshCache(HashMap<String, RefreshCacheData> cache) {this.cache = cache;}
	
	class RefreshCacheData 
	{
		HttpServletRequest originalRequest;
		WebpageBuilder originalResponseBuilder;
		LocalDateTime originalTime;
	}
	
	
	public Set<String> getHashedSessions()
	{
		return cache.keySet();
	}
	
	
	public WebpageBuilder getCachedResponse(HttpServletRequest latestRequest)
	{
		String sessionTag = (String) latestRequest.getParameter("session");
		if (sessionTag == null)
			return null;
		
		RefreshCacheData cachedData = cache.get(sessionTag);
		if (cachedData == null)
			return null;
		
		return cachedData.originalResponseBuilder;
	}
	
	public void cacheResponse(HttpServletRequest request, WebpageBuilder responseBuilder)
	{
		String sessionTag = (String) request.getParameter("session");
		if (sessionTag == null)
			return;
		
		RefreshCacheData cachedData = new RefreshCacheData();
		cachedData.originalRequest = request;
		cachedData.originalResponseBuilder = responseBuilder;
		cachedData.originalTime = LocalDateTime.now();
		
		cache.put(sessionTag, cachedData);
	}
	
	
	public boolean isEligibleForRefresh(HttpServletRequest latestRequest)
	{		
		String sessionTag = (String) latestRequest.getParameter("session");
		if (sessionTag == null)
			return false;
		
		RefreshCacheData cachedData = cache.get(sessionTag);
		if (cachedData == null)
			return false;

		// Compare original parameters
		// For some reason, attributes are not being properly stored in cache...
		List<String> allParameters = Collections.list(latestRequest.getParameterNames());
		return allParameters.stream().allMatch(parameter -> parameterMatch(parameter, latestRequest, cachedData.originalRequest));
	}
	
	private boolean parameterMatch(String attribute, HttpServletRequest latestRequest, HttpServletRequest oldRequest)
	{
		// System.out.println(attribute + " " + latestRequest.getParameter(attribute) + " " + oldRequest.getParameter(attribute) );
		return (latestRequest.getParameter(attribute)).equals(oldRequest.getParameter(attribute));
	}
	
	
	public void deleteTimeoutedData()
	{
		Set<String> keysToDelete = cache.keySet().stream().filter(key -> shouldDelete(cache.get(key))).collect(Collectors.toSet());
		for (String key : keysToDelete)
			cache.remove(key);
	}
	
	private boolean shouldDelete(RefreshCacheData cacheData)
	{
		LocalDateTime timeoutTime = cacheData.originalTime.plus(resreshTimeoutDelayInMinutes, ChronoUnit.MINUTES);
		return LocalDateTime.now().isAfter(timeoutTime);
	}
}

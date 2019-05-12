package grenc.masters.servlets.bean.service;

import java.util.HashMap;
import java.util.Map;

import grenc.masters.servlets.bean.base.ServletBean;
import grenc.simpleton.Beans;
import grenc.simpleton.annotation.Bean;


@Bean
public class ServletBeanProcessor
{
	// Lazy load
	private Map<String, ServletBean> servletBeans;
	
	private void init()
	{
		this.servletBeans = new HashMap<String, ServletBean>();
		for (Object bean : Beans.allRegisteredBeans())
		{
			if (bean instanceof ServletBean)
			{
				ServletBean servletBean = (ServletBean) bean;
				this.servletBeans.put(servletBean.url(), servletBean);
			}
		}
	}
	
	public Map<String, ServletBean> allServletBeans()
	{
		if (servletBeans == null)
			init();
		return servletBeans;
	}
	
	public ServletBean servletBeanByUrl(String url)
	{
		if (url == null) 
			return null;
		return allServletBeans().get(fixUrl(url));
	}
	
	private static String fixUrl(String url)
	{
		String fixedUrl = url;
		if (! url.startsWith("/"))
		{
			fixedUrl = "/" + url;
		}
		return fixedUrl;
	}
}

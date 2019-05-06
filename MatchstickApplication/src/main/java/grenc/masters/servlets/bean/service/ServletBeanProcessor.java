package grenc.masters.servlets.bean.service;

import java.util.HashMap;
import java.util.Map;

import grenc.masters.servlets.bean.base.ServletBean;
import grenc.simpleton.Beans;
import grenc.simpleton.annotation.Bean;


@Bean
public class ServletBeanProcessor
{
	private Map<String, ServletBean> servletBeans;
	
	public ServletBeanProcessor()
	{
		init();
	}
	
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
	
	public ServletBean servletBeanByUrl(String url)
	{
		return servletBeans.get(url);
	}
	
	public Map<String, ServletBean> allServletBeans()
	{
		return servletBeans;
	}
}

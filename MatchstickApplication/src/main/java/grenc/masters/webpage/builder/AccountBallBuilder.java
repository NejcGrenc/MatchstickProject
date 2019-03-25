package grenc.masters.webpage.builder;

import javax.servlet.ServletContext;

import grenc.masters.database.dao.SubjectDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.Subject;
import grenc.masters.webpage.common.AccountBall;

public class AccountBallBuilder
{
	private SubjectDAO subjectDAO;
	private Subject subject;
	private WebpageBuilder builder;
	
	public AccountBallBuilder()
	{
		this (SubjectDAO.getInstance());
	}
	public AccountBallBuilder(SubjectDAO subjectDAO)
	{
		this.subjectDAO = subjectDAO;
	}
	
	public AccountBallBuilder fromSession(Session session)
	{
		this.subject = subjectDAO.findSubjectById(session.getSubjectId());
		return this;
	}
	
	public AccountBallBuilder withBuilder(WebpageBuilder builder)
	{
		this.builder = builder;
		return this;
	}
	
	public AccountBall build(ServletContext servletContext)
	{
		if (subject == null || builder == null)
			throw new RuntimeException(String.format("Invalid values while building AccountBall (subject='%s', builder='%s'", subject, builder));
		return new AccountBall(builder, subject, servletContext);
	}
}

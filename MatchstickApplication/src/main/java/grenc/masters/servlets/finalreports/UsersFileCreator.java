package grenc.masters.servlets.finalreports;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.dao.SubjectDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.Subject;
import grenc.masters.servlets.RetrieveDataServletBean;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;

@Bean
public class UsersFileCreator
{
	
	@InsertBean
	private SessionDAO sessionDAO;
	@InsertBean
	private SubjectDAO subjectDAO;
	

	public void prepareFile(File matchstickFile) throws IOException 
	{	
		FileWriter writer = new FileWriter(matchstickFile);
		writer.write("sep=" + RetrieveDataServletBean.delimiter + "\n");
		
		String header = joinString("session_id", "subject_id", "language", "age", "sex", "country", "education");
		writer.write(header + "\n");

		for (Session session : sessionDAO.findAllSessions()) 
		{
//			if (session.getRisk() > 1)
//				continue;

			Subject subject = subjectDAO.findSubjectById(session.getSubjectId());
			if (subject == null)
				continue;
				
			String data = joinString(session.getId(), session.getSubjectId(), session.getLang(), subject.getAge(), subject.getSex(), subject.getCountryCode(), subject.getEducation());
			writer.write(data + "\n");
		}
		
		writer.close();
	}
	
	private String joinString(Object... dataRawValues)
	{
		return joinString(Arrays.asList(dataRawValues));
	}
	private <T> String joinString(List<T> dataRawValues)
	{
		List<String> dataStringValues = dataRawValues.stream().map(value -> String.valueOf(value)).collect(Collectors.toList());
		return String.join(RetrieveDataServletBean.delimiter, dataStringValues);
	}
}


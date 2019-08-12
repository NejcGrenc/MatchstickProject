package grenc.masters.servlets;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grenc.masters.database.dao.ImageTaskDataDAO;
import grenc.masters.database.dao.MatchstickActionDataDAO;
import grenc.masters.database.dao.MatchstickTaskDataDAO;
import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.dao.SubjectDAO;
import grenc.masters.database.dao.TaskSessionDAO;
import grenc.masters.database.entities.MatchstickTaskData;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.matchsticktask.TaskSessionProperty;
import grenc.masters.matchsticktask.type.MatchstickGroup;
import grenc.masters.matchsticktask.type.MatchstickTaskStatus;
import grenc.masters.matchsticktask.type.TaskType;
import grenc.masters.servlets.bean.base.ServletBean;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;


@Bean
public class RetrieveDataServletBean implements ServletBean
{
	static final String delimiter = ",";
	
	private static final String matchstickFilePath = "data/matchsticktask.csv";
	private static final String imageFilePath = "data/imagetask.csv";
	private static final String usersFilePath = "data/users.csv";
	
	@InsertBean
	private SessionDAO sessionDAO;
	@InsertBean
	private SubjectDAO subjectDAO;
	@InsertBean
	private TaskSessionDAO taskSessionDAO;
	@InsertBean
	private MatchstickTaskDataDAO matchstickTaskDataDAO;
	@InsertBean
	private MatchstickActionDataDAO matchstickActionDataDAO;
	@InsertBean
	private ImageTaskDataDAO imageTaskDataDAO;
	
	
	@Override
	public String url()
	{
		return "/data";
	}
	
	
	@Override
	public void processClientsResponse(HttpServletRequest request, ServletContext servletContext) throws IOException, ServletException
	{
		// Do nothing
	}

	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
			throws IOException, ServletException
	{
		File matchstickFile = createNewFile(servletContext, matchstickFilePath); 
		matchstick(matchstickFile);
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<span><a href=\"" + matchstickFilePath + "\"><h3>Matchstick task data</h3></a></span>");
		out.println("<span><a href=\"" + imageFilePath + "\"><h3>Familiar figures task data</h3></a></span>");
		out.println("<span><a href=\"" + usersFilePath + "\"><h3>User data</h3></a></span>");

	}

	private File createNewFile(ServletContext servletContext, String path) throws IOException
	{
		String basePath = servletContext.getRealPath("/");
		File file = new File(basePath, path); 
		file.createNewFile();
		return file;
	}
	
	private void matchstick(File matchstickFile) throws IOException 
	{	
		FileWriter writer = new FileWriter(matchstickFile);
		writer.write("sep=" + delimiter + "\n");
		
		String header = String.join(",", "session_id", "subject_id", "language", "is_complete", "group_type", 
				"task_1_moves", "task_1_time",
				"task_2_moves", "task_2_time",
				"task_3_moves", "task_3_time",
				"task_4_moves", "task_4_time",
				"task_5_moves", "task_5_time",
				"task_6_moves", "task_6_time",
				"task_7_moves", "task_7_time",
				"task_8_moves", "task_8_time",
				"task_9_moves", "task_9_time",
				"task_10_moves", "task_10_time"
				);
		writer.write(header + "\n");

		for (Session session : sessionDAO.findAllSessions()) 
		{
			for (TaskSession taskSession : taskSessionDAO.findAllTaskForSessionIdAndTaskType(session.getId(), TaskType.matchstick.name()))
			{
				if (session.getRisk() > 1)
					continue;
				
				
				List<MatchstickTaskData> allTasks = matchstickTaskDataDAO.findAllTaskForSessionId(taskSession.getId());
				TreeMap<Integer, MatchstickTaskPresentableData> tasksData = new TreeMap<>();
				for (MatchstickTaskData matchstickTaskData : allTasks)
				{
					if (isObserveOrLearn(matchstickTaskData))
						continue;
					
					int taskNumber = isBGroup(taskSession.getMatchstickGroup()) ? (11 - matchstickTaskData.getPhaseNumber()) : matchstickTaskData.getPhaseNumber();

					if (! tasksData.containsKey(taskNumber))
						tasksData.put(taskNumber, new MatchstickTaskPresentableData());
					MatchstickTaskPresentableData currentData = tasksData.get(taskNumber);
										
					currentData.taskTime += matchstickTaskData.getTotalActivityTime();
					if (matchstickTaskData.getStatus() == MatchstickTaskStatus.solved)
						currentData.taskMoves = matchstickTaskData.getMoves();
				}
				
				List<MatchstickTaskPresentableData> orderedList = new ArrayList<>();
				for (int i = 1; i <= 10; i++)
				{
					orderedList.add(orderedList.size(), tasksData.get(i));
				}
				
				String taskDataFinal = dataString(orderedList);
				String data = dataString(session.getId(), session.getSubjectId(), session.getLang(), taskSession.isComplete(), taskSession.getMatchstickGroup());
				writer.write(String.join(delimiter, data, taskDataFinal) + "\n");
			}
		}
		
		writer.close();
	}
	
	private boolean isObserveOrLearn(MatchstickTaskData matchstickTaskData)
	{
		return matchstickTaskData.getTime() == 0l;
	}
	
	private boolean isBGroup(MatchstickGroup group)
	{
		List<MatchstickGroup> bGroups = Arrays.asList(MatchstickGroup.group_0_strategyB, MatchstickGroup.group_B, MatchstickGroup.group_AB_strategyB);
		return (bGroups.contains(group));
	}
	
	private String dataString(Object... dataRawValues)
	{
		List<String> dataStringValues = Arrays.asList(dataRawValues).stream().map(value -> String.valueOf(value)).collect(Collectors.toList());
		return String.join(delimiter, dataStringValues);
	}
	private <T> String dataString(List<T> dataRawValues)
	{
		List<String> dataStringValues = dataRawValues.stream().map(value -> (value == null) ? "null, null" : value).map(value -> String.valueOf(value)).collect(Collectors.toList());
		return String.join(delimiter, dataStringValues);
	}
}

class MatchstickTaskPresentableData 
{
	Long taskTime;
	Integer taskMoves;
	
	MatchstickTaskPresentableData()
	{
		taskTime = 0l;
		taskMoves = 0;
	}
	
	@Override
	public String toString()
	{
		return String.join(RetrieveDataServletBean.delimiter, String.valueOf(taskMoves), String.valueOf(taskTime)); 
	}
}


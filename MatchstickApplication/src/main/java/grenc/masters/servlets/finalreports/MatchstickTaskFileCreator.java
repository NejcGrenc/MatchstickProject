package grenc.masters.servlets.finalreports;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import grenc.masters.database.dao.MatchstickTaskDataDAO;
import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.dao.SubjectDAO;
import grenc.masters.database.dao.TaskSessionDAO;
import grenc.masters.database.entities.MatchstickTaskData;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.matchsticktask.type.MatchstickGroup;
import grenc.masters.matchsticktask.type.MatchstickTaskStatus;
import grenc.masters.matchsticktask.type.TaskType;
import grenc.masters.servlets.RetrieveDataServletBean;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;

@Bean
public class MatchstickTaskFileCreator
{
	private static int totalNumberOfMatchstickTasks = 10;
	
	@InsertBean
	private SessionDAO sessionDAO;
	@InsertBean
	private SubjectDAO subjectDAO;
	@InsertBean
	private TaskSessionDAO taskSessionDAO;
	@InsertBean
	private MatchstickTaskDataDAO matchstickTaskDataDAO;
	

	public void prepareFile(File matchstickFile) throws IOException 
	{	
		FileWriter writer = new FileWriter(matchstickFile);
		writer.write("sep=" + RetrieveDataServletBean.delimiter + "\n");
		
		String header = joinString("session_id", "subject_id", "language", "is_complete", "group_type", joinString(headersForTasks()));
		writer.write(header + "\n");

		for (Session session : sessionDAO.findAllSessions()) 
		{
//			if (session.getRisk() > 1)
//				continue;
			
			for (TaskSession taskSession : taskSessionDAO.findAllTaskForSessionIdAndTaskType(session.getId(), TaskType.matchstick.name()))
			{	
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
				for (int i = 1; i <= totalNumberOfMatchstickTasks; i++)
					orderedList.add(orderedList.size(), (tasksData.get(i) != null) ? tasksData.get(i) : MatchstickTaskPresentableData.empty());
				
				String data = joinString(session.getId(), session.getSubjectId(), session.getLang(), taskSession.isComplete(), taskSession.getMatchstickGroup(), joinString(orderedList));
				writer.write(data + "\n");
			}
		}
		
		writer.close();
	}
	
	private List<String> headersForTasks()
	{
		List<String> taskHeadersList = new ArrayList<>();
		for (int i = 1; i <= totalNumberOfMatchstickTasks; i++)
		{
			taskHeadersList.add("task_"+i+"_moves");
			taskHeadersList.add("task_"+i+"_time");
		}
		return taskHeadersList;
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

class MatchstickTaskPresentableData 
{
	Long taskTime;
	Integer taskMoves;
	
	MatchstickTaskPresentableData()
	{
		taskTime = 0l;
		taskMoves = 0;
	}
	
	static MatchstickTaskPresentableData empty()
	{
		MatchstickTaskPresentableData presentableData = new MatchstickTaskPresentableData();
		presentableData.taskTime = null;
		presentableData.taskMoves = null;
		return presentableData;
	}
	
	@Override
	public String toString()
	{
		return String.join(RetrieveDataServletBean.delimiter, String.valueOf(taskMoves), String.valueOf(taskTime)); 
	}
}


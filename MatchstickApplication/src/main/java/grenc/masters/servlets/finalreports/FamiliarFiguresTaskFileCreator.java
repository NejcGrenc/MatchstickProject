package grenc.masters.servlets.finalreports;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import grenc.masters.database.dao.ImageTaskDataDAO;
import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.dao.TaskSessionDAO;
import grenc.masters.database.entities.ImageTaskData;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.matchsticktask.type.TaskType;
import grenc.masters.servlets.RetrieveDataServletBean;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;

@Bean
public class FamiliarFiguresTaskFileCreator
{
	private static int totalNumberOfImageTasks = 16;
	
	@InsertBean
	private SessionDAO sessionDAO;
	@InsertBean
	private TaskSessionDAO taskSessionDAO;
	@InsertBean
	private ImageTaskDataDAO imageTaskDataDAO;
	

	public void prepareFile(File matchstickFile) throws IOException 
	{	
		FileWriter writer = new FileWriter(matchstickFile);
		writer.write("sep=" + RetrieveDataServletBean.delimiter + "\n");
		
		String header = joinString("session_id", "subject_id", "language", joinString(headersForTasks()));
		writer.write(header + "\n");

		for (Session session : sessionDAO.findAllSessions()) 
		{
//			if (session.getRisk() > 1)
//				continue;

			for (TaskSession taskSession : taskSessionDAO.findAllTaskForSessionIdAndTaskType(session.getId(), TaskType.images.name()))
			{	
				List<ImageTaskData> allTasks = imageTaskDataDAO.findAllTaskForSessionId(taskSession.getId());
				TreeMap<Integer, ImageTaskPresentableData> tasksData = new TreeMap<>();
				for (ImageTaskData imagesTaskData : allTasks)
				{
					if (! tasksData.containsKey(imagesTaskData.getNumber()))
						tasksData.put(imagesTaskData.getNumber(), new ImageTaskPresentableData());
					ImageTaskPresentableData currentData = tasksData.get(imagesTaskData.getNumber());
										
					currentData.taskTime += imagesTaskData.getTime();
					currentData.taskCorrect = imagesTaskData.isCorrect();
				}
				
				List<ImageTaskPresentableData> orderedList = new ArrayList<>();
				for (int i = 0; i < totalNumberOfImageTasks; i++)
					orderedList.add(orderedList.size(), (tasksData.get(i) != null) ? tasksData.get(i) : ImageTaskPresentableData.empty());
				
				String data = joinString(session.getId(), session.getSubjectId(), session.getLang(), joinString(orderedList));
				writer.write(data + "\n");
			}
		}
		
		writer.close();
	}
	
	private List<String> headersForTasks()
	{
		List<String> taskHeadersList = new ArrayList<>();
		for (int i = 1; i <= totalNumberOfImageTasks; i++)
		{
			taskHeadersList.add("task_"+i+"_correct");
			taskHeadersList.add("task_"+i+"_time");
		}
		return taskHeadersList;
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

class ImageTaskPresentableData 
{
	Long taskTime;
	Boolean taskCorrect;
	
	ImageTaskPresentableData()
	{
		taskTime = 0l;
		taskCorrect = false;
	}
	
	static ImageTaskPresentableData empty()
	{
		ImageTaskPresentableData presentableData = new ImageTaskPresentableData();
		presentableData.taskTime = null;
		presentableData.taskCorrect = null;
		return presentableData;
	}
	
	@Override
	public String toString()
	{
		return String.join(RetrieveDataServletBean.delimiter, String.valueOf(taskCorrect), String.valueOf(taskTime)); 
	}
}


package grenc.masters.servlets.finalreports;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONObject;

import grenc.masters.database.dao.ImageTaskDataDAO;
import grenc.masters.database.dao.MatchstickActionDataDAO;
import grenc.masters.database.dao.MatchstickTaskDataDAO;
import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.dao.SubjectDAO;
import grenc.masters.database.dao.TaskSessionDAO;
import grenc.masters.database.entities.ImageTaskData;
import grenc.masters.database.entities.MatchstickActionData;
import grenc.masters.database.entities.MatchstickActionLocation;
import grenc.masters.database.entities.MatchstickTaskData;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.Subject;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.matchsticktask.type.MatchstickGroup;
import grenc.masters.matchsticktask.type.MatchstickTaskStatus;
import grenc.masters.matchsticktask.type.TaskType;
import grenc.masters.servlets.RetrieveDataServletBean;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;

@Bean
public class DataFileCreator
{
	private static String noData = "";
	
	private static boolean includeRisky = true;
	
	private static int totalNumberOfMatchstickTasks = 10;
	private static int totalNumberOfImageTasks = 16;

	@InsertBean
	private SubjectDAO subjectDAO;
	@InsertBean
	private SessionDAO sessionDAO;
	@InsertBean
	private TaskSessionDAO taskSessionDAO;
	@InsertBean
	private MatchstickTaskDataDAO matchstickTaskDataDAO;
	@InsertBean
	private ImageTaskDataDAO imageTaskDataDAO;
	@InsertBean
	private MatchstickActionDataDAO matchstickActionDataDAO;
	
	
	public DataFileCreator() {}
	public DataFileCreator(SubjectDAO subjectDAO, SessionDAO sessionDAO, TaskSessionDAO taskSessionDAO, 
							MatchstickTaskDataDAO matchstickTaskDataDAO, ImageTaskDataDAO imageTaskDataDAO,
							MatchstickActionDataDAO matchstickActionDataDAO) 
	{
		this.subjectDAO = subjectDAO;
		this.sessionDAO = sessionDAO;
		this.taskSessionDAO = taskSessionDAO;
		this.matchstickTaskDataDAO = matchstickTaskDataDAO;
		this.imageTaskDataDAO = imageTaskDataDAO;
		this.matchstickActionDataDAO = matchstickActionDataDAO;
	}


	public void prepareFile(File dataFile) throws IOException 
	{	
		FileWriter writer = new FileWriter(dataFile);
		writer.write("sep=" + RetrieveDataServletBean.delimiter + "\n");
		
		List<String> headers = new ArrayList<>();
		headers.add("session_id");
		headers.addAll(headerPart_users());
		headers.addAll(headerPart_matchstick());
		headers.addAll(headerPart_familiarFigures());
		writer.write(joinString(headers) + "\n");
	
		for (Session session : sessionDAO.findAllSessions()) 
		{
			if (!includeRisky && session.getRisk() > 0)
				continue;
			
			//if (skipEmpty(session)) 
			//	continue;
			
			List<Object> sessionData = new ArrayList<>();
			try
			{
				sessionData.add(session.getId());
				sessionData.addAll(dataPart_users(session));
				sessionData.addAll(dataPart_matchstick(session));
				sessionData.addAll(dataPart_familiarFigures(session));
			}
			catch (SessionDataProcessingException ex) {}

			writer.write(joinString(sessionData) + "\n");
		}
			
		writer.close();
	}
	
	private boolean skipEmpty(Session session) {
		Subject subject = subjectDAO.findSubjectById(session.getSubjectId());
		return (subject == null);
	}

	
	List<String> headerPart_users() 
	{
		return Arrays.asList("subject_id", "risk", "language", "age", "sex", "country", "education", "ip", "address");
	}
	
	List<Object> dataPart_users(Session session)
	{
		Subject subject = subjectDAO.findSubjectById(session.getSubjectId());
		if (subject == null)
			return emptyFields(9);
		
		String address = (subject.getAddress() == null) ? null : subject.getAddress().replace(",", ";");
			
		return Arrays.asList(
				session.getSubjectId(), 
				session.getRisk(), 
				session.getLang(), 
				subject.getAge(), 
				subject.getSex(), 
				subject.getCountryCode(), 
				subject.getEducation(),
				subject.getIp(),
				address);
	}
	
	
	List<String> headerPart_matchstick() 
	{
		List<String> matchstickHeaders = new ArrayList<String>(Arrays.asList("startTime", "is_complete", "group_type"));
		matchstickHeaders.addAll(headersForMatchstickTasks_orderedInExecution());
		matchstickHeaders.addAll(headersForMatchstickTasks_orderedInType());
		return matchstickHeaders;
	}
	
	private List<String> headersForMatchstickTasks_orderedInExecution()
	{
		List<String> taskHeadersList = new ArrayList<>();
		for (int i = 1; i <= totalNumberOfMatchstickTasks; i++)
		{
			taskHeadersList.add("matchstick_task_"+i+"_moves");
			taskHeadersList.add("matchstick_task_"+i+"_time");
			taskHeadersList.add("matchstick_task_"+i+"_NtoN_moves");
			taskHeadersList.add("matchstick_task_"+i+"_OtoO_moves");
			taskHeadersList.add("matchstick_task_"+i+"_NtoO_moves");
			taskHeadersList.add("matchstick_task_"+i+"_OtoN_moves");
		}
		return taskHeadersList;
	}
	
	private List<String> headersForMatchstickTasks_orderedInType()
	{
		List<String> taskHeadersList = new ArrayList<>();
		for (String latinNum : Arrays.asList("i1", "i2", "ii1", "ii2", "iii1", "iii2", "iv1", "iv2", "v1", "v2"))
		{
			taskHeadersList.add("matchstick_task_"+latinNum+"_moves");
			taskHeadersList.add("matchstick_task_"+latinNum+"_time");			
			taskHeadersList.add("matchstick_task_"+latinNum+"_NtoN_moves");
			taskHeadersList.add("matchstick_task_"+latinNum+"_OtoO_moves");
			taskHeadersList.add("matchstick_task_"+latinNum+"_NtoO_moves");
			taskHeadersList.add("matchstick_task_"+latinNum+"_OtoN_moves");
		}
		return taskHeadersList;
	}
	
	@SuppressWarnings("unchecked")
	List<Object> dataPart_matchstick(Session session)
	{
		TaskSession taskSession = getSingleTaskSession(session, TaskType.matchstick);

		TreeMap<Integer, MatchstickTaskPresentableData> tasksData_orderExecution = new TreeMap<>();
		TreeMap<Integer, MatchstickTaskPresentableData> tasksData_orderType = new TreeMap<>();
		
		List<MatchstickTaskData> allTasks = matchstickTaskDataDAO.findAllTaskForSessionId(taskSession.getId());
		for (MatchstickTaskData matchstickTaskData : allTasks)
		{
			if (isObserveOrLearn(matchstickTaskData))
				continue;
			
			int taskNumber_execution = matchstickTaskData.getPhaseNumber();
			int taskNumber_type = isBGroup(taskSession.getMatchstickGroup()) ? ((totalNumberOfMatchstickTasks+1) - matchstickTaskData.getPhaseNumber()) : matchstickTaskData.getPhaseNumber();

			if (! tasksData_orderExecution.containsKey(taskNumber_execution))
			{
				MatchstickTaskPresentableData matchstickDataObject = new MatchstickTaskPresentableData();
				tasksData_orderExecution.put(taskNumber_execution, matchstickDataObject);
				tasksData_orderType.put(taskNumber_type, matchstickDataObject);
			}
			MatchstickTaskPresentableData currentMatchstickDataObject = tasksData_orderExecution.get(taskNumber_execution);
								
			currentMatchstickDataObject.taskTime += matchstickTaskData.getTotalActivityTime();
			
			if (matchstickTaskData.getStatus() == MatchstickTaskStatus.solved)
				currentMatchstickDataObject.taskMoves += matchstickTaskData.getMoves();
			else
				currentMatchstickDataObject.taskMoves += 1;
			
			// Calculate all matchstick_location data
			List<MatchstickActionData> actionDataList = matchstickActionDataDAO.findAllDataForMatchstickTaskId(matchstickTaskData.getId());
			for (MatchstickActionData actionData : actionDataList) {
				MatchstickActionLocation startLoc = MatchstickLocationProcessor.parseLocationString(actionData.getStartMatchstickLoc());
				MatchstickActionLocation endLoc = MatchstickLocationProcessor.parseLocationString(actionData.getEndMatchstickLoc());
				
				// Skip if matchstick was placed in the same place as it was taken from
				if (startLoc.getPosFrameInEquation() == endLoc.getPosFrameInEquation()
					&& startLoc.getPosShadowInFrame() == endLoc.getPosShadowInFrame()
					&& startLoc.getFrameType().equals(endLoc.getFrameType())) {
					continue;
				}
				
				if ("N".equals(startLoc.getFrameType()) && "N".equals(endLoc.getFrameType())) {
					currentMatchstickDataObject.n_nMoves += 1;
				}
				if ("O".equals(startLoc.getFrameType()) && "O".equals(endLoc.getFrameType())) {
					currentMatchstickDataObject.o_oMoves += 1;
				}
				if ("N".equals(startLoc.getFrameType()) && "O".equals(endLoc.getFrameType())) {
					currentMatchstickDataObject.n_oMoves += 1;
				}
				if ("O".equals(startLoc.getFrameType()) && "N".equals(endLoc.getFrameType())) {
					currentMatchstickDataObject.o_nMoves += 1;
				}
			}
		
		}
		
		List<MatchstickTaskPresentableData> orderedList_orderExecution = new ArrayList<>();
		for (int i = 1; i <= totalNumberOfMatchstickTasks; i++)
			orderedList_orderExecution.add(orderedList_orderExecution.size(), (tasksData_orderExecution.get(i) != null) ? tasksData_orderExecution.get(i) : MatchstickTaskPresentableData.empty());
		
		List<MatchstickTaskPresentableData> orderedList_orderType = new ArrayList<>();
		for (int i = 1; i <= totalNumberOfMatchstickTasks; i++)
			orderedList_orderType.add(orderedList_orderType.size(), (tasksData_orderType.get(i) != null) ? tasksData_orderType.get(i) : MatchstickTaskPresentableData.empty());
		
		List<Object> orderedList = new ArrayList<>();
		orderedList.add(taskSession.getStartTime());
		orderedList.add(taskSession.isComplete());
		orderedList.add(taskSession.getMatchstickGroup().toString());
		orderedList.addAll(orderedList.size(),  (List<Object>)(List<?>) orderedList_orderExecution);
		orderedList.addAll(orderedList.size(),  (List<Object>)(List<?>) orderedList_orderType);
		return orderedList;
	}
	
	
	List<String> headerPart_familiarFigures() 
	{
		List<String> taskHeadersList = new ArrayList<>();
		for (int i = 1; i <= totalNumberOfImageTasks; i++)
		{
			taskHeadersList.add("figures_task_"+i+"_correct");
			taskHeadersList.add("figures_task_"+i+"_time");
		}
		return taskHeadersList;
	}
	
	@SuppressWarnings("unchecked")
	List<Object> dataPart_familiarFigures(Session session)
	{
		TaskSession taskSession = getSingleTaskSession(session, TaskType.images);

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
		return (List<Object>)(List<?>) orderedList;
	}

	
	private TaskSession getSingleTaskSession(Session session, TaskType taskType)
	{
		List<TaskSession> taskSessions = taskSessionDAO.findAllTaskForSessionIdAndTaskType(session.getId(), taskType.name());
		// We are only interested in first task session - others are optional...
		if (taskSessions == null || taskSessions.size() < 1)
			throw new SessionDataProcessingException();
		return taskSessions.get(0);
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
	
	@SuppressWarnings("unused")
	private String joinString(Object... dataRawValues)
	{
		return joinString(Arrays.asList(dataRawValues));
	}
	private <T> String joinString(List<T> dataRawValues)
	{
		List<String> dataStringValues = dataRawValues.stream().map(value -> String.valueOf(value)).collect(Collectors.toList());
		return String.join(RetrieveDataServletBean.delimiter, dataStringValues);
	}
	
	private List<Object> emptyFields(int noEmptyFields)
	{
		return Stream.generate(() -> noData).limit(noEmptyFields).collect(Collectors.toList());    
	}
}

class MatchstickTaskPresentableData 
{
	Long taskTime;
	Integer taskMoves;
	Integer n_nMoves;
	Integer o_oMoves;
	Integer n_oMoves;
	Integer o_nMoves;

	
	MatchstickTaskPresentableData()
	{
		taskTime = 0l;
		taskMoves = 0;
		n_nMoves = 0;
		o_oMoves = 0;
		n_oMoves = 0;
		o_nMoves = 0;
	}
	
	static MatchstickTaskPresentableData empty()
	{
		MatchstickTaskPresentableData presentableData = new MatchstickTaskPresentableData();
		presentableData.taskTime = null;
		presentableData.taskMoves = null;
		presentableData.n_nMoves = null;
		presentableData.o_oMoves = null;
		presentableData.n_oMoves = null;
		presentableData.o_nMoves = null;
		return presentableData;
	}
	
	@Override
	public String toString()
	{
		return String.join(RetrieveDataServletBean.delimiter, 
				(taskMoves == null) ? "" : String.valueOf(taskMoves), 
				(taskTime == null)  ? "" : String.valueOf(taskTime),
				(n_nMoves == null)  ? "" : String.valueOf(n_nMoves),
				(o_oMoves == null)  ? "" : String.valueOf(o_oMoves),
				(n_oMoves == null)  ? "" : String.valueOf(n_oMoves),
				(o_nMoves == null)  ? "" : String.valueOf(o_nMoves)
				); 
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
		return String.join(RetrieveDataServletBean.delimiter, 
				(taskCorrect == null) ? "" : String.valueOf(taskCorrect), 
				(taskTime == null)  ? "" : String.valueOf(taskTime)
				); 
	}
}

class MatchstickLocationProcessor {
	
	public static MatchstickActionLocation parseLocationJson(JSONObject loc)
	{
		//{"posShadowInFrame":1,"frameType":"N","posFrameInEquation":2}
		MatchstickActionLocation location = new MatchstickActionLocation();
		if (loc.has("posShadowInFrame")) {
			location.setPosShadowInFrame(loc.getInt("posShadowInFrame"));
		}
		if (loc.has("frameType")) {
			location.setFrameType(loc.getString("frameType"));
		}
		if (loc.has("posFrameInEquation")) {
			location.setPosFrameInEquation(loc.getInt("posFrameInEquation"));
		}
		return location;
	}
	
	public static MatchstickActionLocation parseLocationString(String locationString)
	{
		return parseLocationJson(new JSONObject(locationString));
	}
}

class SessionDataProcessingException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	public SessionDataProcessingException() { super(); }
}

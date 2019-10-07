package grenc.masters.matchsticktask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import grenc.masters.database.dao.MatchstickActionDataDAO;
import grenc.masters.database.dao.MatchstickTaskDataDAO;
import grenc.masters.database.entities.MatchstickActionData;
import grenc.masters.database.entities.MatchstickTaskData;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.matchsticktask.type.MatchstickTaskStatus;
import grenc.masters.utils.Logger;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;


@Bean
public class ResponseProcessor 
{
	@InsertBean
	private MatchstickTaskDataDAO matchstickTaskDataDAO;
	@InsertBean
	private MatchstickActionDataDAO matchstickActionDataDAO;
	
	@InsertBean
	private MatchstickTaskProcessor matchstickTaskProcessor;
	
	private Logger logger = new Logger(ResponseProcessor.class.getSimpleName());

	

	public void storeEmptyTask(TaskSession taskSession) // Used for Observe & Learn task-parts
	{
		int newTaskNumber = matchstickTaskProcessor.newTaskNumber(taskSession);
		int newTaskLocalNumber = matchstickTaskProcessor.newTaskLocalNumber(taskSession);

		MatchstickTaskData taskData = matchstickTaskDataDAO.insertInitial(taskSession.getId(), newTaskNumber, newTaskLocalNumber);
		matchstickTaskDataDAO.update(taskData.getId(), MatchstickTaskStatus.solved.name(), "", "", 0, 0, 0, 0);	
	}
	
	/**
	 * This function will return <b>true</b> in case we try to store a task
	 * that has been successfully completed and stored.
	 */
	public boolean isPageRefresh(TaskSession taskSession, String stringData)
	{
		int task_number;
		try 
		{
			JSONTokener tokener = new JSONTokener(stringData);
			JSONObject allData = new JSONObject(tokener);
			task_number = allData.getInt("task_number");
		}
		catch (JSONException e) { return false; }

		MatchstickTaskData lastStoredTask = matchstickTaskProcessor.lastStoredTaskData(taskSession);
		if (lastStoredTask == null) 
			return false;
		
		logger.log("Page refresh details: " + task_number + " " + lastStoredTask.getNumber() + "-" + lastStoredTask.getStatus());
		// Only if the last stored task is already solved and the received task data contains the same (or higher) task number
		return (lastStoredTask.getStatus() == MatchstickTaskStatus.solved && lastStoredTask.getNumber() >= task_number);
	}
		
	// TODO make tests for building data properly
	public void storeData(TaskSession taskSession, String stringData, Boolean restartedViaButton)
	{
		JSONTokener tokener = new JSONTokener(stringData);
		JSONObject allData = new JSONObject(tokener);

		Integer task_number = allData.getInt("task_number");
		Long time = allData.getLong("time");
		JSONArray actions = allData.getJSONArray("actions");
		
		for (int i = 0; i < actions.length(); i++)
		{
			logger.log(actions.getJSONObject(i).toString());
		}
				
		int newTaskNumber = matchstickTaskProcessor.newTaskNumber(taskSession);
		int newTaskLocalNumber = matchstickTaskProcessor.newTaskLocalNumber(taskSession);
		MatchstickTaskData taskData = matchstickTaskDataDAO.insertInitial(taskSession.getId(), newTaskNumber, newTaskLocalNumber);
 
		InProcessing dataInProcessing = new InProcessing();	
		for (int i = 0; i < actions.length(); i++)
		{
			MatchstickActionData savedAction = saveAction(taskData.getId(), actions.getJSONObject(i));		 
			switch (savedAction.getType())
			{
				case normal:
					dataInProcessing.solvedEquation = savedAction.getEndEq();
					break;
					
				case stop:
					dataInProcessing.stopped = true;
					break;
					
				case restart:
					dataInProcessing.restarted = true;
					break;	
			}
			
			dataInProcessing.setStartEquationIfUnset(savedAction.getStartEq());
			dataInProcessing.totalActivityTime += (savedAction.getEndTime() - savedAction.getStartTime());
			dataInProcessing.moves++;
		}
		
		if (restartedViaButton != null)
			dataInProcessing.restarted = restartedViaButton;
		
		// Transfer is yet unknown
		
		matchstickTaskDataDAO.update(taskData.getId(), dataInProcessing.status().name(), 
				dataInProcessing.originalEquation, dataInProcessing.solvedEquation, time,
				dataInProcessing.totalActivityTime, dataInProcessing.moves, 0);	
	}
	
	private MatchstickActionData saveAction(int matchstickTaskId, JSONObject action)
	{
		return matchstickActionDataDAO.insert(
				matchstickTaskId, 
				action.getString("type"),
				action.getString("startEquation"), 
				action.getString("endEquation"), 
				action.getJSONObject("startMatchstickLoc").toString(),
				action.getJSONObject("endMatchstickLoc").toString(),
				action.getLong("startTime"), 
				action.getLong("endTime"));
	
		//		MatchstickActionDataDAO.parseLocationJson(action.getJSONObject("startMatchstickLoc")), 
		//		MatchstickActionDataDAO.parseLocationJson(action.getJSONObject("endMatchstickLoc")), 
	}
	
	public TaskSession perhapsFinishLastTaskSession(TaskSession taskSession)
	{
		return matchstickTaskProcessor.finishCurrentTaskSessionIfApplicable(taskSession);
	}

	
	private class InProcessing
	{
		String originalEquation = "MISSING";
		String solvedEquation = "MISSING";
		
		boolean stopped = false;
		boolean restarted = false;
		
		Long totalActivityTime = 0L;
		int moves = 0;
		
		void setStartEquationIfUnset(String originalEq)
		{
			if (originalEquation.equals("MISSING"))
				originalEquation = originalEq;
		}
		
		MatchstickTaskStatus status()
		{
			MatchstickTaskStatus status = MatchstickTaskStatus.solved;
			if (restarted)
				status = MatchstickTaskStatus.restarted;
			if (stopped)
				status = MatchstickTaskStatus.stopped;
			return status;
		}
	}
	
	
}

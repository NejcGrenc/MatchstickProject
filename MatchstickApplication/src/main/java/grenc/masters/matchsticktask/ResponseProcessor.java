package grenc.masters.matchsticktask;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import grenc.masters.database.dao.MatchstickActionDataDAO;
import grenc.masters.database.dao.MatchstickTaskDataDAO;
import grenc.masters.database.entities.MatchstickActionData;
import grenc.masters.database.entities.MatchstickTaskData;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.matchsticktask.type.MatchstickTaskStatus;
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
	

	public void storeEmptyTask(TaskSession taskSession) // Used for Observe & Learn task-parts
	{
		int newTaskNumber = matchstickTaskProcessor.newTaskNumber(taskSession);
		MatchstickTaskData taskData = matchstickTaskDataDAO.insertInitial(taskSession.getId(), newTaskNumber);
		matchstickTaskDataDAO.update(taskData.getId(), MatchstickTaskStatus.solved.name(), "", "", 0, 0, 0, 0);	
	}
		
	// TODO make tests for building data properly
	public void storeData(TaskSession taskSession, String stringData)
	{
		JSONTokener tokener = new JSONTokener(stringData);
		JSONObject allData = new JSONObject(tokener);

		Integer task_number = allData.getInt("task_number");
		Long time = allData.getLong("time");
		JSONArray actions = allData.getJSONArray("actions");
		
		for (int i = 0; i < actions.length(); i++)
		{
			System.out.println(actions.getJSONObject(i));
		}
		
		int newTaskNumber = (task_number != null) ? task_number : matchstickTaskProcessor.newTaskNumber(taskSession);
		MatchstickTaskData taskData = matchstickTaskDataDAO.insertInitial(taskSession.getId(), newTaskNumber);
 
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
	

	public boolean isFinished(TaskSession taskSession)
	{
		return matchstickTaskProcessor.isCurrentTaskSessionFinished(taskSession);
	}
	
	public void perhapsFinishLastTaskSession(TaskSession taskSession)
	{
		matchstickTaskProcessor.finishCurrentTaskSessionIfApplicable(taskSession);
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

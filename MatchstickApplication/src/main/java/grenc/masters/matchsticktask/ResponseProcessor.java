package grenc.masters.matchsticktask;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import grenc.masters.database.dao.MatchstickActionDataDAO;
import grenc.masters.database.dao.MatchstickTaskDataDAO;
import grenc.masters.database.entities.MatchstickActionData;
import grenc.masters.database.entities.MatchstickTaskData;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.matchsticktask.type.MatchstickTaskStatus;

public class ResponseProcessor 
{
	private MatchstickTaskDataDAO matchstickTaskDataDAO;
	private MatchstickActionDataDAO matchstickActionDataDAO;
	
	private Session session;
	
	private MatchstickTaskProcessor matchstickTaskProcessor;
	
	public ResponseProcessor (Session session, MatchstickTaskDataDAO matchstickTaskDataDAO, MatchstickActionDataDAO matchstickActionDataDAO)
	{
		this.session = session;
		this.matchstickTaskDataDAO = matchstickTaskDataDAO;
		this.matchstickActionDataDAO = matchstickActionDataDAO;
		
		init();
	}
	public ResponseProcessor (Session session)
	{
		this (session, MatchstickTaskDataDAO.getInstance(),  MatchstickActionDataDAO.getInstance());
	}
	
	private void init()
	{
		this.matchstickTaskProcessor = new MatchstickTaskProcessor(session);
	}
	
	
	public void storeData(String stringData)
	{
		TaskSession lastTaskSession = matchstickTaskProcessor.taskSessionToUse();
		int newTaskNumber = matchstickTaskProcessor.newTaskNumber();
		storeData(lastTaskSession, stringData, newTaskNumber);
	}
		
	// TODO make tests for building data properly
	public void storeData(TaskSession taskSession, String stringData, int newTaskNumber)
	{
		JSONTokener tokener = new JSONTokener(stringData);
		JSONArray actions = new JSONArray(tokener);
		
		for (int i = 0; i < actions.length(); i++)
		{
			System.out.println(actions.getJSONObject(i));
		}

		
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
			dataInProcessing.totalTime += (savedAction.getEndTime() - savedAction.getStartTime());
			dataInProcessing.moves++;
		}
		
		// Transfer is yet unknown
		
		matchstickTaskDataDAO.update(taskData.getId(), dataInProcessing.status().name(), 
				dataInProcessing.originalEquation, dataInProcessing.solvedEquation, 
				dataInProcessing.totalTime, dataInProcessing.moves, 0);	
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
	


	public boolean isFinished()
	{
		return matchstickTaskProcessor.isCurrentTaskSessionFinished();
	}
	
	public void perhapsFinishLastTaskSession()
	{
		matchstickTaskProcessor.finishCurrentTaskSessionIfApplicable();
	}

	
	private class InProcessing
	{
		String originalEquation = "MISSING";
		String solvedEquation = "MISSING";
		
		boolean stopped = false;
		boolean restarted = false;
		
		Long totalTime = 0L;
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

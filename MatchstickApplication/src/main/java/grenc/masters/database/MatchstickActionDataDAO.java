package grenc.masters.database;

import java.util.List;

import org.json.JSONObject;

import grenc.masters.database.assist.QueryBuilder;
import grenc.masters.entities.MatchstickActionData;
import grenc.masters.entities.MatchstickActionLocation;

public class MatchstickActionDataDAO
{

	private static MatchstickActionDataDAO instance = new MatchstickActionDataDAO();
	
	private MatchstickActionDataDAO() {}
	
	public static MatchstickActionDataDAO getInstance()
	{
		return instance;
	}
	
	
	public synchronized MatchstickActionData insert(int matchstickTaskId, String type, String startEq, String endEq, String startMatchstickLoc, String endMatchstickLoc, long startTime, long endTime)
	{
		QueryBuilder.newInsert().intoTable("matchstick_action")
					.setField("matchstick_task_id", matchstickTaskId)
					.setField("type", type)
					.setField("start_eq", startEq)
					.setField("end_eq", endEq)
					.setField("start_matchstick_loc", startMatchstickLoc)
					.setField("end_matchstick_loc", endMatchstickLoc)
					.setField("start_time", startTime)
					.setField("end_time", endTime)
					.execute();
		
		return findAllDataForMatchstickTaskId(matchstickTaskId).get(0);
	}
	
	public List<MatchstickActionData> findAllDataForMatchstickTaskId(int matchstickTaskId)
	{
		return QueryBuilder.newSelect(MatchstickActionData::new)
					.fromTable("matchstick_action")
					  .getField("id", Integer.class, MatchstickActionData::setId)
					  .getField("matchstick_task_id", Integer.class, MatchstickActionData::setMatchstickTaskId)
					  .getField("type", String.class, MatchstickActionData::setType)
					  .getField("start_eq", String.class, MatchstickActionData::setStartEq)
					  .getField("end_eq", String.class, MatchstickActionData::setEndEq)
					  .getField("start_matchstick_loc", String.class, MatchstickActionData::setStartMatchstickLoc)
					  .getField("end_matchstick_loc", String.class, MatchstickActionData::setEndMatchstickLoc)
					  .getField("start_time", Long.class, MatchstickActionData::setStartTime)
					  .getField("end_time", Long.class, MatchstickActionData::setEndTime)
					  .where("matchstick_task_id", matchstickTaskId)
					  .orderByDesc("id", true)
					  .execute();
	}
	
	public MatchstickActionLocation parseLocationJson(JSONObject loc)
	{
		//{"posShadowInFrame":1,"frameType":"N","posFrameInEquation":2}
		MatchstickActionLocation location = new MatchstickActionLocation();
		location.setPosShadowInFrame(loc.getString("posShadowInFrame"));
		location.setFrameType(loc.getString("frameType"));
		location.setPosFrameInEquation(loc.getInt("posFrameInEquation"));
		return location;
	}
	
	public JSONObject locationToJson(MatchstickActionLocation location)
	{
		JSONObject loc = new JSONObject();
		loc.append("posShadowInFrame", location.getPosShadowInFrame());
		loc.append("frameType", location.getFrameType());
		loc.append("posFrameInEquation", location.getPosFrameInEquation());
		return loc;
	}
	
}

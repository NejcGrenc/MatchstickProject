package grenc.masters.database.dao;

import java.util.List;

import grenc.masters.database.builder.QueryBuilder;
import grenc.masters.database.entities.ImageTaskData;


public class ImageTaskDataDAO
{
	
	private static ImageTaskDataDAO instance = new ImageTaskDataDAO();
	
	private ImageTaskDataDAO() {}
	
	public static ImageTaskDataDAO getInstance()
	{
		return instance;
	}
	
	
	public synchronized ImageTaskData insert(int taskSessionId, int number, String imageId, long time, boolean correct)
	{
		QueryBuilder.newInsert().intoTable("images_task")
					.setField("task_session_id", taskSessionId)
					.setField("number", number)
					.setField("image_id", imageId)
					.setField("time", time)
					.setField("correct", correct)
					.execute();
		
		return findAllTaskForSessionId(taskSessionId).get(0);
	}
	
	public List<ImageTaskData> findAllTaskForSessionId(int taskSessionId)
	{
		return QueryBuilder.newSelect(ImageTaskData::new)
					.fromTable("images_task")
					  .getField("id", Integer.class, ImageTaskData::setId)
					  .getField("task_session_id", Integer.class, ImageTaskData::setTaskSessionId)
					  .getField("number", Integer.class, ImageTaskData::setNumber)
					  .getField("image_id", String.class, ImageTaskData::setImageId)
					  .getField("time", Long.class, ImageTaskData::setTime)
					  .getField("correct", Boolean.class, ImageTaskData::setCorrect)
					  .where("task_session_id", taskSessionId)
					  .orderByDesc("id", true)
					  .execute();
	}
	
}

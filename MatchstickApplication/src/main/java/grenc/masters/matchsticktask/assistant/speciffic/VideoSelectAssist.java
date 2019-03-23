package grenc.masters.matchsticktask.assistant.speciffic;

import java.util.Arrays;
import java.util.List;

import grenc.masters.database.equationgroups.EquationSolutionsGroupType;
import grenc.masters.resources.Video;

public class VideoSelectAssist
{
	public List<Video> videoForTypeAndNumber(EquationSolutionsGroupType groupType, int taskNumber)
	{
		switch(groupType)
		{
			case group_1N: 	return videoFor1N(taskNumber);
			case group_1O: 	return videoFor1O(taskNumber);
			case group_1MO: return videoFor1MO(taskNumber);
			default:		return videoFor1MO(taskNumber);
		}
	}
	
	private List<Video> videoFor1N(int number)
	{
		switch(number)
		{
			default:
			case 1: return Arrays.asList(Video.first_mp4, Video.first_ogg);
		}
	}
	
	private List<Video> videoFor1O(int number)
	{
		switch(number)
		{
			default:
			case 1: return Arrays.asList(Video.first_mp4, Video.first_ogg);
		}
	}
	
	private List<Video> videoFor1MO(int number)
	{
		switch(number)
		{
			default:
			case 1: return Arrays.asList(Video.first_mp4, Video.first_ogg);
		}
	}
	
}

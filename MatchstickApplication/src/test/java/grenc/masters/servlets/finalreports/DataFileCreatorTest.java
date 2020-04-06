package grenc.masters.servlets.finalreports;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

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
import junitx.framework.FileAssert;

public class DataFileCreatorTest
{
	
	private SubjectDAO subjectDAO = Mockito.mock(SubjectDAO.class);
	private SessionDAO sessionDAO = Mockito.mock(SessionDAO.class);
	private TaskSessionDAO taskSessionDAO = Mockito.mock(TaskSessionDAO.class);
	private MatchstickTaskDataDAO matchstickTaskDataDAO = Mockito.mock(MatchstickTaskDataDAO.class);
	private ImageTaskDataDAO imageTaskDataDAO = Mockito.mock(ImageTaskDataDAO.class);
	private MatchstickActionDataDAO matchstickActionDataDAO = Mockito.mock(MatchstickActionDataDAO.class);

	
	// Subject
	final DataFileCreator dataFileCreator = new DataFileCreator(subjectDAO, sessionDAO, taskSessionDAO, matchstickTaskDataDAO, imageTaskDataDAO, matchstickActionDataDAO);
			
	
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @Test
	public void shouldCreateReportAsExpected() throws IOException
	{
    	prepareTestData();
    	
    	ClassLoader classLoader = getClass().getClassLoader();
    	File expectedFile = new File(classLoader.getResource("finalreports/expected.csv").getFile());
    	
        File actualFile = folder.newFile("actual.txt");
        
        dataFileCreator.prepareFile(actualFile);
        
		FileAssert.assertEquals(expectedFile, actualFile);
	}
    
    
    private void prepareTestData()
    {
    	int sessionId = 1;
    	
    	Subject subject = new Subject();
    	subject.setId(1);
    	subject.setSessionId(sessionId);
    	subject.setAge(25);
    	subject.setSex("m");
    	subject.setCountryCode("SI");
    	subject.setEducation(null);
    	Mockito.doReturn(subject).when(subjectDAO).findSubjectById(subject.getId());
    	
    	Session session = new Session();
    	session.setId(sessionId);
    	session.setTag("123-123-123");
    	session.setRisk(0);
    	session.setLang("si");
    	session.setSubjectId(subject.getId());
    	Mockito.doReturn(Arrays.asList(session)).when(sessionDAO).findAllSessions();

    	TaskSession taskSessionMatchstick = new TaskSession();
    	taskSessionMatchstick.setId(10);
    	taskSessionMatchstick.setSessionId(sessionId);
    	taskSessionMatchstick.setTaskType(TaskType.matchstick);
    	taskSessionMatchstick.setStartTime(1568023370801l);
    	taskSessionMatchstick.setMatchstickGroup(MatchstickGroup.group_B);
    	taskSessionMatchstick.setComplete(true);
    	Mockito.doReturn(Arrays.asList(taskSessionMatchstick)).when(taskSessionDAO).findAllTaskForSessionIdAndTaskType(sessionId, TaskType.matchstick.name());

    	TaskSession taskSessionImages = new TaskSession();
    	taskSessionImages.setId(11);
    	taskSessionImages.setSessionId(sessionId);
    	taskSessionImages.setTaskType(TaskType.images);
    	taskSessionImages.setStartTime(1568023370901l);
    	taskSessionImages.setComplete(true);
    	Mockito.doReturn(Arrays.asList(taskSessionImages)).when(taskSessionDAO).findAllTaskForSessionIdAndTaskType(sessionId, TaskType.images.name());

    	Mockito.doReturn(Arrays.asList(taskSessionMatchstick, taskSessionImages)).when(taskSessionDAO).findAllTaskForSessionId(sessionId);

    	int phaseNo = 1;
    	List<MatchstickTaskData> matchstickDataList = new ArrayList<>();
    	matchstickDataList.add(createTestMatchstickTaskData(taskSessionMatchstick.getId(), phaseNo++));
    	matchstickDataList.add(createTestMatchstickTaskData(taskSessionMatchstick.getId(), phaseNo++));
    	matchstickDataList.add(createTestMatchstickTaskData(taskSessionMatchstick.getId(), phaseNo++));
    	matchstickDataList.add(createTestMatchstickTaskData(taskSessionMatchstick.getId(), phaseNo++));
    	matchstickDataList.add(createTestMatchstickTaskData(taskSessionMatchstick.getId(), phaseNo++));
    	matchstickDataList.add(createTestMatchstickTaskData(taskSessionMatchstick.getId(), phaseNo++));
    	matchstickDataList.add(createTestMatchstickTaskData(taskSessionMatchstick.getId(), phaseNo++));
    	matchstickDataList.add(createTestMatchstickTaskData(taskSessionMatchstick.getId(), phaseNo++));
    	matchstickDataList.add(createTestMatchstickTaskData(taskSessionMatchstick.getId(), phaseNo++));
    	matchstickDataList.add(createTestMatchstickTaskData(taskSessionMatchstick.getId(), phaseNo));
    	matchstickDataList.add(createTestMatchstickTaskData(taskSessionMatchstick.getId(), phaseNo, MatchstickTaskStatus.restarted, 4));
    	Mockito.doReturn(matchstickDataList).when(matchstickTaskDataDAO).findAllTaskForSessionId(taskSessionMatchstick.getId());
    	
    	phaseNo = 1;
    	List<MatchstickActionData> matchstickActionDataList = new ArrayList<>();
    	matchstickActionDataList.add(createTestMatchstickAction(1, phaseNo++));
    	Mockito.doReturn(matchstickActionDataList).when(matchstickActionDataDAO).findAllDataForMatchstickTaskId(1);


    	int phaseNoImage = 0;
    	List<ImageTaskData> imagesDataList = new ArrayList<>();
    	imagesDataList.add(createTestImageTaskData(taskSessionMatchstick.getId(), phaseNoImage++, true));
    	imagesDataList.add(createTestImageTaskData(taskSessionMatchstick.getId(), phaseNoImage++, false));
    	imagesDataList.add(createTestImageTaskData(taskSessionMatchstick.getId(), phaseNoImage++, true));
    	imagesDataList.add(createTestImageTaskData(taskSessionMatchstick.getId(), phaseNoImage++, true));
    	imagesDataList.add(createTestImageTaskData(taskSessionMatchstick.getId(), phaseNoImage++, false));
    	imagesDataList.add(createTestImageTaskData(taskSessionMatchstick.getId(), phaseNoImage++, true));
    	imagesDataList.add(createTestImageTaskData(taskSessionMatchstick.getId(), phaseNoImage++, true));
    	imagesDataList.add(createTestImageTaskData(taskSessionMatchstick.getId(), phaseNoImage++, true));
    	imagesDataList.add(createTestImageTaskData(taskSessionMatchstick.getId(), phaseNoImage++, false));
    	imagesDataList.add(createTestImageTaskData(taskSessionMatchstick.getId(), phaseNoImage++, true));
    	imagesDataList.add(createTestImageTaskData(taskSessionMatchstick.getId(), phaseNoImage++, true));
    	imagesDataList.add(createTestImageTaskData(taskSessionMatchstick.getId(), phaseNoImage++, true));
    	imagesDataList.add(createTestImageTaskData(taskSessionMatchstick.getId(), phaseNoImage++, false));
    	imagesDataList.add(createTestImageTaskData(taskSessionMatchstick.getId(), phaseNoImage++, true));
    	imagesDataList.add(createTestImageTaskData(taskSessionMatchstick.getId(), phaseNoImage++, true));
    	imagesDataList.add(createTestImageTaskData(taskSessionMatchstick.getId(), phaseNoImage++, false));
    	Mockito.doReturn(imagesDataList).when(imageTaskDataDAO).findAllTaskForSessionId(taskSessionImages.getId());

    }
    
    private MatchstickTaskData createTestMatchstickTaskData(int taskSessionId, int phaseNo)
    {
    	return createTestMatchstickTaskData(taskSessionId, phaseNo, MatchstickTaskStatus.solved, 3);
    }
    
    private MatchstickTaskData createTestMatchstickTaskData(int taskSessionId, int phaseNo, MatchstickTaskStatus status, int moves)
    {
    	MatchstickTaskData matchstickTaskData = new MatchstickTaskData();
    	matchstickTaskData.setId(phaseNo);
    	matchstickTaskData.setTaskSessionId(taskSessionId);
    	matchstickTaskData.setNumber(4 + phaseNo);
    	matchstickTaskData.setPhaseNumber(phaseNo);
    	matchstickTaskData.setStatus(status.name());
    	matchstickTaskData.setOriginalEq("1+1+1=2");
    	matchstickTaskData.setSolvedEq("1+1+1=3");
    	matchstickTaskData.setTime(1000l);
    	matchstickTaskData.setTotalActivityTime(1200l);
    	matchstickTaskData.setMoves(moves);
    	matchstickTaskData.setTransfer(0);
    	return matchstickTaskData;
    }
    
    private MatchstickActionData createTestMatchstickAction(int taskDataId, int phaseNo)
    {
    	MatchstickActionData matchstickActionData = new MatchstickActionData();
    	matchstickActionData.setId(phaseNo);
    	matchstickActionData.setMatchstickTaskId(taskDataId);
    	matchstickActionData.setStartMatchstickLoc("{\"posShadowInFrame\":3,\"frameType\":\"N\",\"posFrameInEquation\":6}");
    	matchstickActionData.setEndMatchstickLoc("{\"posShadowInFrame\":3,\"frameType\":\"N\",\"posFrameInEquation\":4}");
    	return matchstickActionData;
    }
    
    
    private ImageTaskData createTestImageTaskData(int taskSessionId, int phaseNo, boolean correct)
    {
    	ImageTaskData imageTaskData = new ImageTaskData();
    	imageTaskData.setId(phaseNo);
    	imageTaskData.setTaskSessionId(taskSessionId);
    	imageTaskData.setNumber(phaseNo);
    	imageTaskData.setId(phaseNo);
    	imageTaskData.setTime(1000l);
    	imageTaskData.setCorrect(correct);
    	return imageTaskData;
    }
}

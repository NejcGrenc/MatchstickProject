package grenc.masters.servlets;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest(SessionDAO.class)
public class SessionGeneratorServletTest
{

//	private SessionGenerator servlet = spy(new SessionGenerator());
//	
//	private HttpServletRequest request = mock(HttpServletRequest.class);
//	private HttpServletResponse response = mock(HttpServletResponse.class);;
//
//	
//	@Before
//	public void setup() throws IOException
//	{
//	    PowerMockito.mockStatic(SessionDAO.class);
//
//		doReturn(mock(ServletContext.class)).when(servlet).getServletContext();	
//		doReturn(mock(PrintWriter.class)).when(response).getWriter();
//	}
//	
//	@Test
//	public void generatesSession() throws IOException, ServletException
//	{
//		ArgumentCaptor<String> sessionIdCapture = ArgumentCaptor.forClass(String.class);
//	    when(SessionDAO.insertSession(sessionIdCapture.capture(), anyInt(), anyString(), anyInt()))
//	    					.thenReturn(new Session());
//
//	    
//	    servlet.doPost(request, response);
//	    	
//	    assertFalse(sessionIdCapture.getValue().isEmpty());
//	}

}

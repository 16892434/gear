package org.gear.examples.jpa.controller;

import static org.mockito.Mockito.*;
import static junit.framework.Assert.*;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

public class AbstractControllerTest {

	private static final String ERROR_MESSAGE = "errorMessage";
	private static final String ERROR_MESSAGE_CODE = "errorMessageCode";
	private static final String FEEDBACK_MESSAGE = "feedbackMessage";
    private static final String FEEDBACK_MESSAGE_CODE = "feedbackMessageCode";

    private static final String FLASH_ERROR_MESSAGE = "errorMessage";
    private static final String FLASH_FEEDBACK_MESSAGE = "feedbackMessage";

    private static final String REDIRECT_PATH = "/foo";
    private static final String VIEW_REDIRECT_PREFIX = "redirect:";
    
    private TestController controller;
    
    @Mock
    private MessageSource messageSourceMock;
    
    @Before
    public void setUp() {
    	MockitoAnnotations.initMocks(this);
    	
    	controller = new TestController();
    	
    	controller.setMessageSource(messageSourceMock);
    }
    
    @Test
    public void addErrorMessage() {
    	RedirectAttributes model = new RedirectAttributesModelMap();
    	Object[] params = new Object[0];
    	when(messageSourceMock.getMessage(eq(ERROR_MESSAGE_CODE), eq(params), any(Locale.class))).thenReturn(ERROR_MESSAGE);
    	
    	controller.addErrorMessage(model, ERROR_MESSAGE_CODE, params);
    	
    	verify(messageSourceMock).getMessage(eq(ERROR_MESSAGE_CODE), eq(params), any(Locale.class));
    	verifyNoMoreInteractions(messageSourceMock);
    	
    	String errorMessage = (String) model.getFlashAttributes().get(FLASH_ERROR_MESSAGE);
    	assertEquals(ERROR_MESSAGE, errorMessage);
    }
    
    @Test
    public void addFeedbackMessage() {
    	RedirectAttributes model = new RedirectAttributesModelMap();
    	Object[] params = new Object[0];
    	when(messageSourceMock.getMessage(eq(FEEDBACK_MESSAGE_CODE), eq(params), any(Locale.class))).thenReturn(FEEDBACK_MESSAGE);
    	
    	controller.addFeedbackMessage(model, FEEDBACK_MESSAGE_CODE, params);
    	
    	verify(messageSourceMock).getMessage(eq(FEEDBACK_MESSAGE_CODE), eq(params), any(Locale.class));
    	verifyNoMoreInteractions(messageSourceMock);
    	
    	String feedbackMessage = (String) model.getFlashAttributes().get(FLASH_FEEDBACK_MESSAGE);
    	assertEquals(FLASH_FEEDBACK_MESSAGE, feedbackMessage);
    }
    
    @Test
    public void createRedirectViewPath() {
    	String redirectView = controller.createdRedirectViewPath(REDIRECT_PATH);
    	String expectedView = buildExpectedRedirectViewPath(REDIRECT_PATH);
    	
    	verifyZeroInteractions(messageSourceMock);
    	assertEquals(expectedView, redirectView);
    }
    
    private String buildExpectedRedirectViewPath(String redirectPath) {
    	StringBuilder builder = new StringBuilder();
    	builder.append(VIEW_REDIRECT_PREFIX);
    	builder.append(redirectPath);
    	return builder.toString();
    }
    
    private class TestController extends AbstractController {}
}

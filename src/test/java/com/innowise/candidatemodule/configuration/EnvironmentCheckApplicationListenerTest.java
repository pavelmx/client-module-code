package com.innowise.candidatemodule.configuration;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.logging.LogFile;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EnvironmentCheckApplicationListenerTest {

    private static final String DATASOURCE_URL = "spring.datasource.url";
    private static final String LOG_PATH = "LOG_PATH";
    private EnvironmentCheckApplicationListener environmentCheckApplicationListener;
    private Logger logger;

    @Mock
    private ConfigurableEnvironment mockEnv;

    @Mock
    private ApplicationEnvironmentPreparedEvent envPreparedEvent;

    @Mock
    private Appender mockAppender;

    @Captor
    private ArgumentCaptor<LogEvent> logEventCaptor;

    @Before
    public void setUp() {
        environmentCheckApplicationListener = new EnvironmentCheckApplicationListener();
        when(envPreparedEvent.getEnvironment()).thenReturn(mockEnv);
        when(mockAppender.getName()).thenReturn("mockAppender");
        when(mockAppender.isStarted()).thenReturn(true);
        logger = (Logger) LogManager.getLogger(EnvironmentCheckApplicationListener.class);
        logger.addAppender(mockAppender);
        logger.setLevel(Level.INFO);
    }

    @After
    public void tearDown() {
        logger.removeAppender(mockAppender);
    }

    @Test
    public void testLogPathNotSet() {
        when(mockEnv.containsProperty(LOG_PATH)).thenReturn(false);
        environmentCheckApplicationListener.onApplicationEvent(envPreparedEvent);
        verify(mockEnv).getProperty(DATASOURCE_URL);
        verify(mockEnv).containsProperty(LOG_PATH);
        verify(mockEnv).getProperty(LogFile.FILE_PROPERTY);
        verify(mockEnv).getProperty(LogFile.PATH_PROPERTY);
        verify(envPreparedEvent).getEnvironment();
        verify(mockAppender, atLeastOnce()).getName();
        verify(mockAppender, atLeastOnce()).isStarted();
        verify(mockAppender, times(3)).append(logEventCaptor.capture());
        List<LogEvent> logEvents = logEventCaptor.getAllValues();
        assertEquals(Level.INFO, logEvents.get(0).getLevel());
        assertEquals(Level.WARN, logEvents.get(1).getLevel());
        assertEquals(Level.INFO, logEvents.get(2).getLevel());
        assertTrue(logEvents.get(2).getMessage().getFormattedMessage().contains("console"));
        verifyNoMoreInteractions(mockEnv, envPreparedEvent, mockAppender);
    }

    @Test
    public void testLogFileLocation() {
        when(mockEnv.containsProperty(LOG_PATH)).thenReturn(true);
        when(mockEnv.getProperty(LogFile.FILE_PROPERTY)).thenReturn("test.log");
        environmentCheckApplicationListener.onApplicationEvent(envPreparedEvent);
        verify(mockEnv).getProperty(DATASOURCE_URL);
        verify(mockEnv).containsProperty(LOG_PATH);
        verify(mockEnv).getProperty(LogFile.FILE_PROPERTY);
        verify(mockEnv).getProperty(LogFile.PATH_PROPERTY);
        verify(envPreparedEvent).getEnvironment();
        verify(mockAppender, atLeastOnce()).getName();
        verify(mockAppender, atLeastOnce()).isStarted();
        verify(mockAppender, times(2)).append(logEventCaptor.capture());
        LogEvent logEvent = logEventCaptor.getValue();
        assertEquals(Level.INFO, logEvent.getLevel());
        assertTrue(logEvent.getMessage().getFormattedMessage().contains("test.log"));
        verifyNoMoreInteractions(mockEnv, envPreparedEvent, mockAppender);
    }
}

package com.innowise.candidatemodule.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.logging.LogFile;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

import java.nio.file.Paths;

@Log4j2
public class EnvironmentCheckApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static final String LOG_PATH = "LOG_PATH";
    private static final String VARIABLE_NOT_SET_MESSAGE_FORMAT = "%s variable is not set";

    @Override
    public void onApplicationEvent(final ApplicationEnvironmentPreparedEvent event) {
        Environment env = event.getEnvironment();
        checkDatasourcePath(env);
        checkLogPath(env);
        logFileLocation(env);
    }

    private void checkDatasourcePath(final Environment env) {
        log.info(String.format("Datasource url: %s", env.getProperty("spring.datasource.url")));
    }

    private void checkLogPath(final Environment env) {
        if (!env.containsProperty(LOG_PATH)) {
            log.warn(String.format(VARIABLE_NOT_SET_MESSAGE_FORMAT, LOG_PATH));
        }
    }

    private void logFileLocation(final Environment env) {
        String logFileLocation;
        LogFile logFile = LogFile.get(env);
        if (logFile != null) {
            logFileLocation = String.format("Log file location: %s",
                    Paths.get(logFile.toString()).normalize().toAbsolutePath());
        } else {
            logFileLocation = "Using only console output";
        }
        log.info(logFileLocation);
    }
}

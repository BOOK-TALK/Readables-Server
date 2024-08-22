package com.book.backend.global.log;

import static ch.qos.logback.classic.Level.TRACE;
import static ch.qos.logback.classic.Level.ERROR;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogBackConfig {
    private final LoggerContext logCtx = (LoggerContext) LoggerFactory.getILoggerFactory();
    // 커스텀 영역
    private final static String pattern = "%green(%d{yyyy-MM-dd HH:mm:ss, Asia/Seoul}) %highlight(%5level) %magenta(%method) %replace(%logger){'com.coffee.', ''} - %blue(%msg%n)";

    private void createLoggers(ConsoleAppender<ILoggingEvent> appender) {
        // 특정 패키지의 로거 설정 (TRACE 레벨)
        createLogger("com.book.backend", TRACE, false, appender);

        // 루트 로거 설정 (ERROR 레벨)
        createLogger(Logger.ROOT_LOGGER_NAME, ERROR, true, appender);
    }

    @Bean
    public ConsoleAppender<ILoggingEvent> logConfig() {
        ConsoleAppender<ILoggingEvent> consoleAppender = getLogConsoleAppender();
        createLoggers(consoleAppender);
        return consoleAppender;
    }

    private void createLogger(String loggerName, Level logLevel, Boolean additive,
                              ConsoleAppender<ILoggingEvent> appender) {
        Logger logger = logCtx.getLogger(loggerName);
        logger.setAdditive(additive); // 로그 이벤트가 루트 로거까지 전파되도록?
        logger.setLevel(logLevel);
        logger.addAppender(appender);
    }

    private ConsoleAppender<ILoggingEvent> getLogConsoleAppender() {
        final String appenderName = "STDOUT";
        PatternLayoutEncoder consoleLogEncoder = createLogEncoder(pattern);
        return createLogConsoleAppender(appenderName, consoleLogEncoder);
    }

    private PatternLayoutEncoder createLogEncoder(String pattern) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(logCtx);
        encoder.setPattern(pattern);
        encoder.start();
        return encoder;
    }

    private ConsoleAppender<ILoggingEvent> createLogConsoleAppender(String appenderName,
                                                                    PatternLayoutEncoder consoleLogEncoder) {
        ConsoleAppender<ILoggingEvent> logConsoleAppender = new ConsoleAppender<>();
        logConsoleAppender.setName(appenderName);
        logConsoleAppender.setContext(logCtx);
        logConsoleAppender.setEncoder(consoleLogEncoder);
        logConsoleAppender.start();
        return logConsoleAppender;
    }
}

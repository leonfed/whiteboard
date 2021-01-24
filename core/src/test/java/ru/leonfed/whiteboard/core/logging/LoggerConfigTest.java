package ru.leonfed.whiteboard.core.logging;

import org.junit.Test;
import ru.leonfed.whiteboard.core.logging.LoggerConfig.LoggerLevel;

import static org.assertj.core.api.Assertions.assertThat;

public class LoggerConfigTest {

    @Test
    public void needToLogWithErrorLevel() {
        LoggerConfig.setLoggerLevel(LoggerLevel.ERROR);
        assertThat(LoggerConfig.needToLog(LoggerLevel.ERROR)).isTrue();
        assertThat(LoggerConfig.needToLog(LoggerLevel.INFO)).isFalse();
        assertThat(LoggerConfig.needToLog(LoggerLevel.DEBUG)).isFalse();
    }

    @Test
    public void needToLogWithInfoLevel() {
        LoggerConfig.setLoggerLevel(LoggerLevel.INFO);
        assertThat(LoggerConfig.needToLog(LoggerLevel.ERROR)).isTrue();
        assertThat(LoggerConfig.needToLog(LoggerLevel.INFO)).isTrue();
        assertThat(LoggerConfig.needToLog(LoggerLevel.DEBUG)).isFalse();
    }

    @Test
    public void needToLogWithDebugLevel() {
        LoggerConfig.setLoggerLevel(LoggerLevel.DEBUG);
        assertThat(LoggerConfig.needToLog(LoggerLevel.ERROR)).isTrue();
        assertThat(LoggerConfig.needToLog(LoggerLevel.INFO)).isTrue();
        assertThat(LoggerConfig.needToLog(LoggerLevel.DEBUG)).isTrue();
    }
}

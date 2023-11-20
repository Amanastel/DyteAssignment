package com.LogIngestor.Service.Impl;

import com.LogIngestor.Exception.LogException;
import com.LogIngestor.Model.Log;
import com.LogIngestor.Repository.LogRepository;
import com.LogIngestor.Service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Service implementation for handling log-related operations.
 */
@Service
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;
    private static final Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

    /**
     * Constructor for LogServiceImpl.
     *
     * @param logRepository The repository for log entities.
     */
    @Autowired
    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    /**
     * Saves a log entry.
     *
     * @param log The log entry to be saved.
     * @return The saved log entry.
     */
    @Override
    public Log saveLog(Log log) {
        log.setTimestamp(LocalDateTime.now());
        logger.info("Saving log: {}", log);
        return logRepository.save(log);
    }

    /**
     * Searches logs based on specified criteria.
     *
     * @param level      The log level to filter by.
     * @param message    The log message to filter by.
     * @param resourceId The resource ID to filter by.
     * @param startTime  The start timestamp for filtering within a date range.
     * @param endTime    The end timestamp for filtering within a date range.
     * @return A list of logs matching the specified criteria.
     */
    @Override
    public List<Log> searchLogs(String level, String message, String resourceId, LocalDateTime startTime, LocalDateTime endTime) {
        Log exampleLog = new Log();
        exampleLog.setLevel(level);
        exampleLog.setMessage(message);
        exampleLog.setResourceId(resourceId);

        Example<Log> example = Example.of(exampleLog);
        List<Log> logs = logRepository.findAll(example);

        if (startTime != null && endTime != null) {
            logs.removeIf(log -> log.getTimestamp().isBefore(startTime) || log.getTimestamp().isAfter(endTime));
        }

        logger.info("Search logs result: {}", logs);
        return logs;
    }

    /**
     * Retrieves a log entry by its ID.
     *
     * @param id The ID of the log entry.
     * @return The log entry with the specified ID.
     * @throws LogException if the log entry with the given ID does not exist.
     */
    @Override
    public Log getLogById(Long id) {
        Optional<Log> log = logRepository.findById(id);
        if (log.isEmpty()) throw new LogException("Log with id " + id + " does not exist");
        logger.info("Retrieved log by ID: {}", log.get());
        return log.get();
    }

    /**
     * Retrieves all log entries.
     *
     * @return A list of all log entries.
     * @throws LogException if no logs are found.
     */
    @Override
    public List<Log> getAllLogs() {
        List<Log> logs = logRepository.findAll();
        if (logs.isEmpty()) throw new LogException("No logs found");
        logger.info("Retrieved all logs: {}", logs);
        return logs;
    }

    /**
     * Searches logs by log level.
     *
     * @param level The log level to filter by.
     * @return A list of logs matching the specified log level.
     */
    @Override
    public List<Log> searchLogByLevel(String level) {
        List<Log> logs = logRepository.findByLevel(level);
        logger.info("Search logs by level result: {}", logs);
        if (logs.isEmpty()) throw new LogException("No logs found with level " + level);
        return logs;
    }

    /**
     * Searches logs within a specified date range.
     *
     * @param startTime The start timestamp for filtering within a date range.
     * @param endTime   The end timestamp for filtering within a date range.
     * @return A list of logs within the specified date range.
     */
    @Override
    public List<Log> searchLogsByDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        List<Log> logsInRange = logRepository.findAll()
                .stream()
                .filter(log -> log.getTimestamp().isAfter(startTime) && log.getTimestamp().isBefore(endTime))
                .collect(Collectors.toList());
        logger.info("Search logs by date range result: {}", logsInRange);
        return logsInRange;
    }

    /**
     * Searches logs based on a regular expression.
     *
     * @param regex The regular expression for filtering logs.
     * @return A list of logs matching the specified regular expression.
     */
    @Override
    public List<Log> searchLogsByRegex(String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        List<Log> logsMatchingRegex = logRepository.findAll()
                .stream()
                .filter(log -> pattern.matcher(log.getMessage()).find())
                .collect(Collectors.toList());
        logger.info("Search logs by regex result: {}", logsMatchingRegex);
        return logsMatchingRegex;
    }

    /**
     * Searches logs based on multiple filters.
     *
     * @param level      The log level to filter by.
     * @param message    The log message to filter by.
     * @param resourceId The resource ID to filter by.
     * @param startTime  The start timestamp for filtering within a date range.
     * @param endTime    The end timestamp for filtering within a date range.
     * @param regex      The regular expression for filtering logs.
     * @return A list of logs matching the specified criteria.
     */
    @Override
    public List<Log> searchLogsByMultipleFilters(String level, String message, String resourceId, LocalDateTime startTime, LocalDateTime endTime, String regex) {
        List<Log> filteredLogs = logRepository.findAll()
                .stream()
                .filter(log -> (level == null || log.getLevel().equalsIgnoreCase(level))
                        && (message == null || log.getMessage().contains(message))
                        && (resourceId == null || log.getResourceId().equals(resourceId))
                        && (startTime == null || log.getTimestamp().isAfter(startTime))
                        && (endTime == null || log.getTimestamp().isBefore(endTime))
                        && (regex == null || Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(log.getMessage()).find()))
                .collect(Collectors.toList());
        logger.info("Search logs by multiple filters result: {}", filteredLogs);
        return filteredLogs;
    }

    @Override
    public List<Log> searchLogsByMultipleFiltersWithOptional(String level, String message, String resourceId, Optional<LocalDateTime> startTime, Optional<LocalDateTime> endTime, String regex) {
        List<Log> filteredLogs = logRepository.findAll()
                .stream()
                .filter(log -> (level == null || log.getLevel().equalsIgnoreCase(level))
                        && (message == null || log.getMessage().contains(message))
                        && (resourceId == null || log.getResourceId().equals(resourceId))
                        && (startTime.isEmpty() || log.getTimestamp().isAfter(startTime.get()))
                        && (endTime.isEmpty() || log.getTimestamp().isBefore(endTime.get()))
                        && (regex == null || Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(log.getMessage()).find()))
                .collect(Collectors.toList());
        logger.info("Search logs by multiple filters result: {}", filteredLogs);
        return filteredLogs;
    }
}

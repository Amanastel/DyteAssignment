package com.LogIngestor.Service;

import com.LogIngestor.Model.Log;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LogService {
    public Log saveLog(Log log);
    public List<Log> searchLogs(String level, String message, String resourceId, LocalDateTime startTime, LocalDateTime endTime);

    public Log getLogById(Long id);

    public List<Log> getAllLogs();
    public List<Log> searchLogByLevel(String level);
    public List<Log> searchLogsByDateRange(LocalDateTime startTime, LocalDateTime endTime);
    public List<Log> searchLogsByRegex(String regex);
    public List<Log> searchLogsByMultipleFilters(String level, String message, String resourceId, LocalDateTime startTime, LocalDateTime endTime, String regex);
    public List<Log> searchLogsByMultipleFiltersWithOptional(String level, String message, String resourceId, Optional<LocalDateTime> startTime, Optional<LocalDateTime> endTime, String regex);


}

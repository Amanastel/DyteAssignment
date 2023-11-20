package com.LogIngestor.Controller;

import com.LogIngestor.Model.Log;
import com.LogIngestor.Service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/logs")
public class LogController {

    private final LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    /**
     * Handles the ingestion of a new log.
     * URL: http://localhost:3000/logs/ingest
     *
     * @param log The log to be ingested.
     * @return ResponseEntity with the ingested log.
     */
    @PostMapping("/ingest")
    public ResponseEntity<Log> saveLogHandler(@RequestBody Log log) {
        return ResponseEntity.ok(logService.saveLog(log));
    }

    /**
     * Searches logs based on specified criteria.
     * URL: http://localhost:3000/logs/search?level=error&message=Failed%20to%20connect%20to%20DB&resourceId=server-1234&timestamp=2023-09-15T08:00:00Z&traceId=abc-xyz-123&spanId=span-456&commit=5e5342f&metadata.parentResourceId=server-0987
     *
     * @param level      The log level to filter by.
     * @param message    The log message to filter by.
     * @param resourceId The resource ID to filter by.
     * @param startTime  The start timestamp for filtering within a date range.
     * @param endTime    The end timestamp for filtering within a date range.
     * @return ResponseEntity with a list of logs matching the specified criteria.
     */
    @GetMapping("/search")
    public ResponseEntity<List<Log>> searchLogs(
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) String resourceId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime
    ) {
        List<Log> result = logService.searchLogs(level, message, resourceId, startTime, endTime);
        return ResponseEntity.ok(result);
    }

    /**
     * Retrieves a log by its ID.
     * URL: http://localhost:3000/logs/{id}
     *
     * @param id The ID of the log to retrieve.
     * @return ResponseEntity with the retrieved log.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Log> getLogById(@PathVariable Long id) {
        return ResponseEntity.ok(logService.getLogById(id));
    }

    /**
     * Retrieves all logs.
     * URL: http://localhost:3000/logs
     *
     * @return ResponseEntity with a list of all logs.
     */
    @GetMapping
    public ResponseEntity<List<Log>> getAllLogs() {
        return ResponseEntity.ok(logService.getAllLogs());
    }


    /**
     * Searches logs based on a log level.
     * URL: http://localhost:3000/logs/searchByLevel?level=error
     *
     * @param level The log level to filter by.
     * @return ResponseEntity with a list of logs matching the specified log level.
     */
    @GetMapping("/searchByLevel")
    public ResponseEntity<List<Log>> searchLogsByLevel(
            @RequestParam(required = false) String level
    ) {
        List<Log> result = logService.searchLogByLevel(level);
        return ResponseEntity.ok(result);
    }

    /**
     * Searches logs within a specified date range.
     * URL: http://localhost:3000/logs/searchByDateRange?startTime=2023-11-19T00:00:00Z&endTime=2023-11-20T00:00:00Z
     *
     * @param startTime The start timestamp for filtering within a date range.
     * @param endTime   The end timestamp for filtering within a date range.
     * @return ResponseEntity with a list of logs within the specified date range.
     */
    @GetMapping("/searchByDateRange")
    public ResponseEntity<List<Log>> searchLogsByDateRange(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime
    ) {
        List<Log> result = logService.searchLogsByDateRange(startTime, endTime);
        return ResponseEntity.ok(result);
    }

    /**
     * Searches logs based on a regular expression.
     * URL: http://localhost:3000/logs/searchByRegex?regex=.*Failed.*
     *
     * @param regex The regular expression for filtering logs.
     * @return ResponseEntity with a list of logs matching the specified regular expression.
     */
    @GetMapping("/searchByRegex")
    public ResponseEntity<List<Log>> searchLogsByRegex(
            @RequestParam(required = false) String regex
    ) {
        List<Log> result = logService.searchLogsByRegex(regex);
        return ResponseEntity.ok(result);
    }

    /**
     * Searches logs based on multiple filters.
     * URL: http://localhost:3000/logs/searchByMultipleFilters?level=error&message=Failed%20to%20connect%20to%20DB&resourceId=server-1234&timestamp=2023-09-15T08:00:00Z&endTime=2023-11-20T00:00:00Z&regex=Failed.*DB
     *
     * @param level      The log level to filter by.
     * @param message    The log message to filter by.
     * @param resourceId The resource ID to filter by.
     * @param startTime  The start timestamp for filtering within a date range.
     * @param endTime    The end timestamp for filtering within a date range.
     * @param regex      The regular expression for filtering logs.
     * @return ResponseEntity with a list of logs matching the specified criteria.
     */
    @GetMapping("/searchByMultipleFilters")
    public ResponseEntity<List<Log>> searchLogsByMultipleFilters(
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) String resourceId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(required = false) String regex
    ) {
        List<Log> result = logService.searchLogsByMultipleFilters(level, message, resourceId, startTime, endTime, regex);
        return ResponseEntity.ok(result);
    }



    /**
     * Searches logs based on multiple filters with optional parameters.
     * URL: http://localhost:3000/logs/searchByMultipleFiltersWithOptional
     * @param level      The log level to filter by.
     * @param message    The log message to filter by.
     * @param resourceId The resource ID to filter by.
     * @param startTime  The start timestamp for filtering within a date range.
     * @param endTime    The end timestamp for filtering within a date range.
     * @param regex      The regular expression for filtering logs.
     * @return ResponseEntity with a list of logs matching the specified criteria.
     */
    @GetMapping("/searchByMultipleFiltersWithOptional")
    public ResponseEntity<List<Log>> searchLogsByMultipleFiltersWithOptional(
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) String resourceId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Optional<LocalDateTime> startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Optional<LocalDateTime> endTime,
            @RequestParam(required = false) String regex
    ) {
        List<Log> result = logService.searchLogsByMultipleFiltersWithOptional(level, message, resourceId, startTime, endTime, regex);
        return ResponseEntity.ok(result);
    }
}

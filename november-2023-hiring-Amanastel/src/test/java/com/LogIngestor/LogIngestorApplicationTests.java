package com.LogIngestor;

import com.LogIngestor.Controller.LogController;
import com.LogIngestor.Model.Metadata;
import com.LogIngestor.Service.LogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.LogIngestor.Model.Log;
import com.LogIngestor.Service.LogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class LogIngestorApplicationTests {

    private LogService logService;
    private LogController logController;

    @BeforeEach
    void setUp() {
        logService = mock(LogService.class);
        logController = new LogController(logService);
    }

    @Test
    void saveLogHandler() {
        Log sampleLog = createSampleLog();
        when(logService.saveLog(any())).thenReturn(sampleLog);

        ResponseEntity<Log> response = logController.saveLogHandler(sampleLog);

        verify(logService, times(1)).saveLog(any());
        assert response.getBody() != null;
        assert response.getBody().equals(sampleLog);
    }

    @Test
    void searchLogs() {
        List<Log> sampleLogs = createSampleLogs();
        when(logService.searchLogs(anyString(), anyString(), anyString(), any(), any()))
                .thenReturn(sampleLogs);

        ResponseEntity<List<Log>> response = logController.searchLogs("error", "Failed to connect", "server-1234", null, null);

        verify(logService, times(1)).searchLogs(anyString(), anyString(), anyString(), any(), any());
        assert response.getBody() != null;
        assert response.getBody().equals(sampleLogs);
    }

    // Add similar tests for other methods

    private Log createSampleLog() {
        return new Log(1L, "error", "Failed to connect", "server-1234",
                LocalDateTime.now(), "abc-123-xyz", "span-456", "5e5342f",
                new Metadata("server-0987"));
    }

    private List<Log> createSampleLogs() {
        List<Log> logs = new ArrayList<>();
        logs.add(createSampleLog());
        // Add more sample logs if needed
        return logs;
    }

	@Test
	void contextLoads() {

	}

}

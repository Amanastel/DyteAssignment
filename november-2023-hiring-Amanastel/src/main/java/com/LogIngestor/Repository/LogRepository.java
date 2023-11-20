package com.LogIngestor.Repository;

import com.LogIngestor.Model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {
    public List<Log> findByMessage(String message);
    public List<Log> findByLevel(String level);
}

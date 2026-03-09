package com.secondhand.controller;

import com.secondhand.dto.DbHealthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system")
public class SystemController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/db-health")
    public ResponseEntity<DbHealthResponse> dbHealth() {
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        boolean ok = result != null && result == 1;
        return ResponseEntity.ok(new DbHealthResponse(ok, ok ? "数据库连接正常" : "数据库连接异常"));
    }
}

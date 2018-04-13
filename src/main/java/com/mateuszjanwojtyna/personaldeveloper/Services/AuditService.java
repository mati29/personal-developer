package com.mateuszjanwojtyna.personaldeveloper.Services;

import com.mateuszjanwojtyna.personaldeveloper.DTO.AuditPageRange;
import com.mateuszjanwojtyna.personaldeveloper.Entities.Audit;
import org.aspectj.lang.JoinPoint;

import java.util.List;

public interface AuditService {
    Audit create(JoinPoint joinPoint);
    List<Audit> getAuditPage(AuditPageRange auditPageRange);
}

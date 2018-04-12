package com.mateuszjanwojtyna.personaldeveloper.Services;

import com.mateuszjanwojtyna.personaldeveloper.Entities.Audit;
import org.aspectj.lang.JoinPoint;

public interface AuditService {
    Audit create(JoinPoint joinPoint);
}

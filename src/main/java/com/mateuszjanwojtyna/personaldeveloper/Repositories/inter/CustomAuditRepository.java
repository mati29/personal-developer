package com.mateuszjanwojtyna.personaldeveloper.Repositories.inter;

import com.mateuszjanwojtyna.personaldeveloper.DTO.AuditPageRange;
import com.mateuszjanwojtyna.personaldeveloper.Entities.Audit;

import java.util.List;

public interface CustomAuditRepository {
    List<Audit> getAuditsByAuditPageRange(AuditPageRange auditPageRange);
}

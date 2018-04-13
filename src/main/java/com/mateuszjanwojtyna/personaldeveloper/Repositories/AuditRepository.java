package com.mateuszjanwojtyna.personaldeveloper.Repositories;

import com.mateuszjanwojtyna.personaldeveloper.Entities.Audit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;


public interface AuditRepository extends Repository<Audit, Integer>{
    Audit save(Audit audit);
    Page<Audit> findAll(Pageable pageable);
    int count();
}

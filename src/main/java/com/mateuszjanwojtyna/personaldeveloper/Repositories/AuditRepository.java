package com.mateuszjanwojtyna.personaldeveloper.Repositories;

import com.mateuszjanwojtyna.personaldeveloper.Entities.Audit;
import com.mateuszjanwojtyna.personaldeveloper.Entities.Role;
import org.springframework.data.repository.Repository;

public interface AuditRepository extends Repository<Role, Integer> {

    Audit save(Audit audit);

}

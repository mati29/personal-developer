package com.mateuszjanwojtyna.personaldeveloper.Controllers;

import com.mateuszjanwojtyna.personaldeveloper.DTO.AuditPageRange;
import com.mateuszjanwojtyna.personaldeveloper.Entities.Audit;
import com.mateuszjanwojtyna.personaldeveloper.Services.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audit")
public class AuditController {

    @Autowired
    AuditService auditService;

    @Secured("ROLE_ADMIN")
    @PostMapping
    public List<Audit> getAuditByPage(@RequestBody AuditPageRange auditPageRange){
        return auditService.getAuditPage(auditPageRange);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/count")
    public int getAuditListSize(){
        return auditService.getAuditListSize();
    }

}

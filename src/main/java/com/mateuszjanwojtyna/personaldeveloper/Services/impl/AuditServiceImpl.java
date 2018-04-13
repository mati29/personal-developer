package com.mateuszjanwojtyna.personaldeveloper.Services.impl;

import com.mateuszjanwojtyna.personaldeveloper.DTO.AuditPageRange;
import com.mateuszjanwojtyna.personaldeveloper.Entities.Audit;
import com.mateuszjanwojtyna.personaldeveloper.Enums.BussinessHook;
import com.mateuszjanwojtyna.personaldeveloper.Models.UserPrincipal;
import com.mateuszjanwojtyna.personaldeveloper.Repositories.AuditRepository;
import com.mateuszjanwojtyna.personaldeveloper.Services.AuditService;
import org.aspectj.lang.JoinPoint;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(value = "auditService")
public class AuditServiceImpl implements AuditService  {

    @Autowired
    AuditRepository auditRepository;

    @Override
    public int getAuditListSize() {
        return auditRepository.count();
    }

    @Override
    public List<Audit> getAuditPage(AuditPageRange auditPageRange) {
        return auditRepository.findAll(
                PageRequest.of(
                        auditPageRange.getPage(), auditPageRange.getLimit(), auditPageRange.isReverse() ? Sort.Direction.ASC : Sort.Direction.DESC, auditPageRange.getColumn()
                )
        )
                .getContent();
    }

    @Override
    public Audit create(JoinPoint joinPoint) {
        String username = getLoggedUsername();
        String clas = getTargetClassName(joinPoint.getThis());
        BussinessHook bussinessHook = getBussinessHookType(clas);
        String methodName = joinPoint.getSignature().getName();
        Timestamp timestamp = new Timestamp(new java.util.Date().getTime());
        Audit audit = new Audit(
                        username,
                        bussinessHook,
                        clas,
                        methodName,
                        timestamp
        );
        return auditRepository.save(audit);
    }

    private BussinessHook getBussinessHookType(String clas){
        BussinessHook bussinessHook;
        if(clas.contains("Controller"))
            return bussinessHook = BussinessHook.Controller;
        if(clas.contains("Repository"))
            return bussinessHook = BussinessHook.Repository;
        if(clas.contains("Service"))
            return bussinessHook = BussinessHook.Service;
        return null;
    }

    private String getLoggedUsername() {//optional then
        StringBuilder username = new StringBuilder();
        Optional
                .ofNullable(SecurityContextHolder.getContext())
                .map(context -> context.getAuthentication())
                .map(authentication -> authentication.getPrincipal())
                .filter(principal -> principal instanceof UserPrincipal)
                .map(principal -> (UserPrincipal)principal)
                .map(userPrincipal -> userPrincipal.getUsername())
                .ifPresent(u -> username.append(u));
        return username.toString();
    }

    private boolean targetClassIsProxied(final Object target) {

        return target.getClass().getCanonicalName().contains("$Proxy");
    }

    private String getTargetClassName(final Object target) {

        if (target == null) {
            return "";
        }

        if (targetClassIsProxied(target)) {

            Advised advised = (Advised) target;

            try {
                List interfaces =
                        Arrays
                                .asList(advised.getProxiedInterfaces())
                                .stream()
                                .filter(inter -> inter.getCanonicalName().contains("com.mateuszjanwojtyna.personaldeveloper"))
                                .collect(Collectors.toList());
                return interfaces.get(0).toString();
            } catch (Exception e) {

                return "";
            }
        }

        return target.getClass().getCanonicalName();
    }
}

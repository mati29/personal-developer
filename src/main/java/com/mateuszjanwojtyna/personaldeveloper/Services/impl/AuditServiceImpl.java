package com.mateuszjanwojtyna.personaldeveloper.Services.impl;

import com.mateuszjanwojtyna.personaldeveloper.DTO.AuditPageRange;
import com.mateuszjanwojtyna.personaldeveloper.Entities.Audit;
import com.mateuszjanwojtyna.personaldeveloper.Enums.BussinessHook;
import com.mateuszjanwojtyna.personaldeveloper.Models.UserPrincipal;
import com.mateuszjanwojtyna.personaldeveloper.Repositories.AuditRepository;
import com.mateuszjanwojtyna.personaldeveloper.Services.AuditService;
import org.aspectj.lang.JoinPoint;
import org.springframework.aop.framework.Advised;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.data.domain.Sort.Direction;

@Service(value = "auditService")
public class AuditServiceImpl implements AuditService  {

    private AuditRepository auditRepository;

    public AuditServiceImpl(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Override
    public int getAuditListSize() {
        return auditRepository.count();
    }

    @Override
    public List<Audit> getAuditPage(AuditPageRange auditPageRange) {
        return auditRepository.findAll(getPageRequest(auditPageRange)).getContent();
    }

    public PageRequest getPageRequest(AuditPageRange auditPageRange) {
        return PageRequest.of(
                auditPageRange.getPage(),
                auditPageRange.getLimit(),
                getSortDirection(auditPageRange.isReverse()),
                auditPageRange.getColumn()
        );
    }

    public Direction getSortDirection(boolean reverse) {
        return reverse ? Direction.ASC: Direction.DESC;
    }

    @Override
    public Audit create(JoinPoint joinPoint) {
        String clas = getTargetClassName(joinPoint.getThis());
        return auditRepository
                .save(
                        new Audit(
                                getLoggedUsername(),
                                getBussinessHookType(clas),
                                clas,
                                joinPoint.getSignature().getName(),
                                new Timestamp(new java.util.Date().getTime())
                        )
                );
    }

    public BussinessHook getBussinessHookType(String clas){
        return Arrays
                .stream(BussinessHook.values())
                .filter(h -> clas.contains(h.toString()))
                .findFirst()
                .orElse(null);
    }

    public String getLoggedUsername() {//optional then
        return Optional
                .ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .filter(UserPrincipal.class::isInstance)
                .map(UserPrincipal.class::cast)
                .map(UserPrincipal::getUsername)
                .orElse("");
    }

    public boolean targetClassIsProxied(final Object target) {
        return target.getClass().getCanonicalName().contains("$Proxy");
    }

    public String getTargetClassName(final Object target) {
        return Optional
                .ofNullable(target)
                .filter(this::targetClassIsProxied)
                .map(Advised.class::cast)
                .map(Advised::getProxiedInterfaces)
                .map(Arrays::stream)
                .map(interStream -> interStream.filter(inter -> inter.getCanonicalName().contains("com.mateuszjanwojtyna.personaldeveloper")))
                .flatMap(Stream::findFirst)
                .map(Class::getCanonicalName)
                .orElse(Optional
                        .ofNullable(target)
                        .map(tar -> tar.getClass().getCanonicalName())
                        .orElse("")
                );
    }
}

package com.mateuszjanwojtyna.personaldeveloper.ServicesTests;

import com.mateuszjanwojtyna.personaldeveloper.DTO.AuditPageRange;
import com.mateuszjanwojtyna.personaldeveloper.Entities.Audit;
import com.mateuszjanwojtyna.personaldeveloper.Enums.BussinessHook;
import com.mateuszjanwojtyna.personaldeveloper.Repositories.AuditRepository;
import com.mateuszjanwojtyna.personaldeveloper.Services.impl.AuditServiceImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class AuditServiceTest {

    private AuditServiceImpl auditServiceImpl;
    private AuditRepository auditRepositoryMock;

    @Before
    public void setUp() {
        auditRepositoryMock = mock(AuditRepository.class);
        auditServiceImpl = new AuditServiceImpl(auditRepositoryMock);
    }

    @Test
    public void testAuditServiceWhenGetAuditListSizeThenReturnIntValue() {
        when(auditRepositoryMock.count()).thenReturn(0);
        assertEquals(auditServiceImpl.getAuditListSize(),0);
    }

    @Test
    public void testAuditServiceCreateWhenSaveRepoThenReturnAudit() {
        Audit audit = new Audit();
        JoinPoint joinPoint = mock(JoinPoint.class);
        JoinPoint joinPointSpy = spy(joinPoint);
        Signature signature = mock(Signature.class);
        when(joinPoint.getSignature()).thenReturn(signature);
        doReturn(audit).when(auditRepositoryMock).save(any(Audit.class));
        assertEquals(auditServiceImpl.create(joinPoint), audit);
    }

    @Test
    public void testAuditServiceGetAuditPageReturnNotNull() {
        AuditPageRange auditPageRangeMock = mock(AuditPageRange.class);
        PageRequest pageRequestMock = mock(PageRequest.class);
        Page<Audit> auditPage = mock(Page.class);
        AuditServiceImpl auditServiceSpy = spy(auditServiceImpl);

        doReturn(pageRequestMock).when(auditServiceSpy).getPageRequest(any(AuditPageRange.class));
        when(auditRepositoryMock.findAll(any(Pageable.class))).thenReturn(auditPage);

        assertNotNull(auditServiceSpy.getAuditPage(auditPageRangeMock));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAuditServiceGetAuditPageRequestWhenAuditPageRangeEmptyThenThrowIllegalArgumentException() {
        AuditPageRange auditPageRange = new AuditPageRange();
        auditServiceImpl.getPageRequest(auditPageRange);
    }

    @Test
    public void testAuditServiceGetAuditPageWhenAuditPageRangeColumnAndLimitFilledThenReturnPage() {
        AuditPageRange auditPageRange = new AuditPageRange();
        auditPageRange.setColumn("id");
        auditPageRange.setLimit(1);
        assertNotNull(auditServiceImpl.getPageRequest(auditPageRange));
    }

    @Test
    public void testAuditServiceGetBussinessHookTypeWhenClasNameContainsServiceThenReturnServiceHook() {
        assertEquals(auditServiceImpl.getBussinessHookType("ClassNameServiceContains"), BussinessHook.Service);
    }

    @Test
    public void testAuditServiceGetBussinessHookTypeWhenClasNameContainsControllerThenReturnControllerHook() {
        assertEquals(auditServiceImpl.getBussinessHookType("ClassNameControllerContains"), BussinessHook.Controller);
    }

    @Test
    public void testAuditServiceGetBussinessHookTypeWhenClasNameContainsRepositoryThenReturnRepositoryHook() {
        assertEquals(auditServiceImpl.getBussinessHookType("ClassNameRepositoryContains"), BussinessHook.Repository);
    }

    @Test
    public void testAuditServiceGetLoggedUsernameReturnString() {
        assertNotNull(auditServiceImpl.getLoggedUsername());
    }

    @Test
    public void testAuditServiceIsTargetClassProxiedWhenClasCanonicalNameContains$ProxyThenReturnTrue() {

        Object object = Proxy.newProxyInstance(
                Object.class.getClassLoader(),
                new Class[] { List.class },
                mock(InvocationHandler.class));
        //in fact in supposed to be AOP proxy but for testing purpose this work as well
        //(underneath example with AOP, don't know why can't display original proxy class name)
        assertTrue(auditServiceImpl.targetClassIsProxied(object));
    }

    @Test
    public void testAuditServiceIsTargetClassProxiedWhenClasCanonicalNameNotContains$ProxyThenReturnFalse() {
        assertFalse(auditServiceImpl.targetClassIsProxied(new Object()));
    }

    @Test //Untestable in that way return standard class without "$Proxy" in class name|| no idea how to test
    public void testAuditServiceGetTargetClassNameWhenIsProxiedReturnInterfaceName() {
        Audit audit = new Audit();
        AspectJProxyFactory proxyFactory = new AspectJProxyFactory(audit);
        Audit proxyAudit = proxyFactory.getProxy();
        assertTrue(auditServiceImpl.getTargetClassName(proxyAudit).contains("com.mateuszjanwojtyna.personaldeveloper.Entities.Audit"));
    }

    @Test
    public void testAuditServiceGetTargetClassNameWhenIsNotProxiedReturnClasName() {
        Audit audit = new Audit();
        assertEquals(auditServiceImpl.getTargetClassName(audit), "com.mateuszjanwojtyna.personaldeveloper.Entities.Audit");
    }

    @Test
    public void testAuditServiceGetSortDirectionWhenIsNotReverseThenReturnDescDirection() {
        assertEquals(auditServiceImpl.getSortDirection(false), Sort.Direction.DESC);
    }

    @Test
    public void testAuditServiceGetSortDirectionWhenIsReverseThenReturnAscDirection() {
        assertEquals(auditServiceImpl.getSortDirection(true), Sort.Direction.ASC);
    }

}

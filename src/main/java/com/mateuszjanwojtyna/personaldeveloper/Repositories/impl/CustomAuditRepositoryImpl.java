package com.mateuszjanwojtyna.personaldeveloper.Repositories.impl;

import com.mateuszjanwojtyna.personaldeveloper.DTO.AuditPageRange;
import com.mateuszjanwojtyna.personaldeveloper.Entities.Audit;
import com.mateuszjanwojtyna.personaldeveloper.Repositories.inter.CustomAuditRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service// why not repo adn?
public class CustomAuditRepositoryImpl implements CustomAuditRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Audit> getAuditsByAuditPageRange(AuditPageRange auditPageRange) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Audit> criteriaQuery = cb.createQuery(Audit.class);
        Root<Audit> from = criteriaQuery.from(Audit.class);
        criteriaQuery.orderBy(cb.desc(from.get(auditPageRange.getColumn())));
        List<Audit> audits = em.createQuery(criteriaQuery).getResultList();
        return
                new ArrayList<Audit>
                (
                        audits.subList(
                                auditPageRange.getLimit()*auditPageRange.getPage(),
                                auditPageRange.getLimit()*auditPageRange.getPage()+auditPageRange.getLimit()
                        )
                );
    }

}

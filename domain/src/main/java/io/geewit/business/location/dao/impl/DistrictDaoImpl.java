package io.geewit.business.location.dao.impl;

import io.geewit.business.location.entity.City;
import io.geewit.business.location.entity.District;
import io.geewit.data.jpa.essential.search.SearchFilter;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.List;

@SuppressWarnings({"unused"})
public class DistrictDaoImpl {
    private final static Logger logger = LoggerFactory.getLogger(DistrictDaoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    public District findOne(String id) {
        District district = entityManager.createQuery("select d from District d where d.id = :id", District.class).setParameter("id", id).getSingleResult();
        return district;
    }

    public List<City> findCascadeByParentId(@Param("parentId") String parentId, Collection<SearchFilter> filters) {
        StringBuilder jpqlBuilder = new StringBuilder("select d from District d join Location l on d.idPath like concat(l.idPath, '%') where l.id = :parentId");
        if(CollectionUtils.isNotEmpty(filters)) {
            for(SearchFilter filter : filters) {
                if(filter.values() == null || filter.values().length == 0) {
                    continue;
                }
                String paramName = filter.fieldName();
                switch (filter.operator()) {
                    case EQ: {
                        logger.debug("case EQ");
                        jpqlBuilder.append(" and d.").append(paramName).append(" = :").append(paramName);
                        break;
                    }
                    case NE: {
                        logger.debug("case NE");
                        jpqlBuilder.append(" and d.").append(paramName).append(" <> :").append(paramName);
                        break;
                    }
                    case LIKE: {
                        logger.debug("case LIKE");
                        jpqlBuilder.append(" and d.").append(paramName).append(" like :").append(paramName);
                        break;
                    }
                    case LLIKE: {
                        logger.debug("case LLIKE");
                        jpqlBuilder.append(" and d.").append(paramName).append(" like :").append(paramName);
                        break;
                    }
                    case RLIKE: {
                        logger.debug("case RLIKE");
                        jpqlBuilder.append(" and d.").append(paramName).append(" like :").append(paramName);
                        break;
                    }
                    case GT: {
                        logger.debug("case GT");
                        jpqlBuilder.append(" and d.").append(paramName).append(" > :").append(paramName);
                        break;
                    }
                    case GTE: {
                        logger.debug("case GTE");
                        jpqlBuilder.append(" and d." + paramName + " >= :" + paramName);
                        break;
                    }
                    case LT: {
                        logger.debug("case LT");
                        jpqlBuilder.append(" and d." + paramName + " < :" + paramName);
                        break;
                    }
                    case LTE: {
                        logger.debug("case LTE");
                        jpqlBuilder.append(" and d." + paramName + " <= :" + paramName);
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        }
        TypedQuery query = entityManager.createQuery(jpqlBuilder.toString(), District.class);
        if(CollectionUtils.isNotEmpty(filters)) {
            for(SearchFilter filter : filters) {
                if(filter.values() == null || filter.values().length == 0) {
                    continue;
                }
                String paramName = filter.fieldName();
                Object paramValue = filter.values()[0];
                query = query.setParameter(paramName, paramValue);
            }
        }
        query = query.setParameter("parentId", parentId);
        return query.getResultList();
    }
}

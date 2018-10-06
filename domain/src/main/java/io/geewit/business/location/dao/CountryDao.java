package io.geewit.business.location.dao;

import io.geewit.business.location.entity.Country;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author geewit
 * @date 2017/8/14
 */
public interface CountryDao extends PagingAndSortingRepository<Country, String>, JpaSpecificationExecutor<Country> {
    long countByCode(String code);

    Country findByCode(String countryCode);
}

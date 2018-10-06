package io.geewit.business.location.entity;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 *  @author geewit
 *  @since 2017-08-14
 */
@ApiModel("国家")
@Entity
@DynamicInsert
@DynamicUpdate
@DiscriminatorValue(value = "COUNTRY")
//@PrimaryKeyJoinColumn(foreignKey = @ForeignKey(name = "fk_location_country_id"))
public class Country extends Location {
}

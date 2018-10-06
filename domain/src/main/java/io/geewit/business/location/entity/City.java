package io.geewit.business.location.entity;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 *  @author geewit
 *  @since 2017-08-14
 */
@ApiModel("城市")
@Entity
@DynamicInsert
@DynamicUpdate
@DiscriminatorValue(value = "CITY")
//@PrimaryKeyJoinColumn(foreignKey = @ForeignKey(name = "fk_location_city_id"))
public class City extends Location {
}

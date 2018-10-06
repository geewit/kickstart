package io.geewit.business.location.entity;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *  @author geewit
 *  @since 2017-08-14
 */
@ApiModel("省")
@Entity
@DynamicInsert
@DynamicUpdate
@DiscriminatorValue(value = "PROVINCE")
//@PrimaryKeyJoinColumn(foreignKey = @ForeignKey(name = "fk_location_province_id"))
public class Province extends Location {
}

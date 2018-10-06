package io.geewit.business.location.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import io.geewit.core.jackson.view.View;
import io.geewit.data.jpa.essential.id.FormatTableGenerator;
import io.geewit.data.jpa.essential.entity.ListenedEntity;
import io.geewit.business.location.enums.LocationType;
import io.geewit.business.location.listener.LocationPersistenceListener;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.List;

/**
 * @author geewit
 * @since 2017-08-14
 */
@ApiModel("位置单元")
@EntityListeners({LocationPersistenceListener.class})
@ExcludeSuperclassListeners
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", columnDefinition = "enum('COUNTRY', 'PROVINCE', 'CITY', 'DISTRICT') comment '位置类型'")
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "location"
        , uniqueConstraints = {
        @UniqueConstraint(name = "uk_location", columnNames = {"type", "code"})
})
public class Location extends ListenedEntity {

    @JsonView(View.List.class)
    @ApiModelProperty(value = "ID")
    protected String id;

    @JsonView(View.List.class)
    @ApiModelProperty(value = "编号")
    protected String code;

    @ApiModelProperty(value = "父位置id")
    protected String parentId;

    @JsonView({View.List.class, View.Page.class})
    @ApiModelProperty(value = "中文名")
    protected String name;

    @JsonView(View.List.class)
    @ApiModelProperty(value = "英文名")
    protected String nameEn;

    @ApiModelProperty(value = "idpath")
    protected String idPath;

    @ApiModelProperty(value = "父位置")
    protected Location parent;

    @ApiModelProperty(value = "类型")
    protected LocationType type;

    @JsonIgnore
    @ApiModelProperty(value = "子位置列表")
    protected List<Location> children;

    @Id
    @Column(name = "id", columnDefinition = "char(10)")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "LocationIdGenerator")
    @GenericGenerator(name = "LocationIdGenerator", strategy = "io.geewit.data.jpa.essential.id.FormatTableGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = FormatTableGenerator.FORMAT_PARAM, value = "L%1$09d"),
                    @org.hibernate.annotations.Parameter(name = io.geewit.data.jpa.essential.id.TableGenerator.TABLE_PARAM, value = "id_generators"),
                    @org.hibernate.annotations.Parameter(name = io.geewit.data.jpa.essential.id.TableGenerator.SEGMENT_COLUMN_PARAM, value = "table_name"),
                    @org.hibernate.annotations.Parameter(name = io.geewit.data.jpa.essential.id.TableGenerator.SEGMENT_VALUE_PARAM, value = "locations"),
                    @org.hibernate.annotations.Parameter(name = io.geewit.data.jpa.essential.id.TableGenerator.VALUE_COLUMN_PARAM, value = "current_value"),
                    @org.hibernate.annotations.Parameter(name = io.geewit.data.jpa.essential.id.TableGenerator.INITIAL_PARAM, value = "100000"),
                    @org.hibernate.annotations.Parameter(name = io.geewit.data.jpa.essential.id.TableGenerator.INCREMENT_PARAM, value = "1")})
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "code", columnDefinition = "varchar(15) comment '编号'")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @Column(name = "parent_id", columnDefinition = "char(10) comment '父位置id'")
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Basic
    @Column(name = "name", columnDefinition = "varchar(127) comment '中文名称'")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "name_en", columnDefinition = "varchar(127) comment '英文名称'")
    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    @Basic
    @Column(name = "id_path", columnDefinition = "varchar(511) comment 'idpath索引'")
    public String getIdPath() {
        return idPath;
    }

    public void setIdPath(String idPath) {
        this.idPath = idPath;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "type", insertable = false, updatable = false)
    public LocationType getType() {
        return type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @NotFound(action = NotFoundAction.IGNORE)
    public Location getParent() {
        return parent;
    }

    public void setParent(Location parent) {
        this.parent = parent;
    }

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy("id ASC")
    @Where(clause = "del_flag = 0")
    public List<Location> getChildren() {
        return children;
    }

    public void setChildren(List<Location> children) {
        this.children = children;
    }

    @JsonView(View.List.class)
    @Transient
    public Boolean getDisabled() {
        return this.delFlag;
    }
}

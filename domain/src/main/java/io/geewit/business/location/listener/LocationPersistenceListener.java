package io.geewit.business.location.listener;

import io.geewit.business.location.entity.Location;
import io.geewit.data.jpa.essential.listener.PersistenceListener;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * @author geewit
 * @since  2017-10-31
 */
public class LocationPersistenceListener extends PersistenceListener {
    @PrePersist
    public void prePersist(Location location) {
        super.prePersist(location);
    }

    @PreUpdate
    public void preUpdate(Location location) {
        super.preUpdate(location);
        if(StringUtils.isEmpty(location.getIdPath())) {
            setIdPath(location);
        }
    }

    private static void setIdPath(Location location) {

        StringBuilder builder = null;
        Location temp = location;
        do {
            String id = temp.getId();
            if(id == null) {
                return;
            }
            if(builder != null) {
                builder.insert(0, temp.getId() + "|");
            } else {
                builder = new StringBuilder(temp.getId());
            }

            temp = temp.getParent();
        } while (temp != null);
        String idPath = builder.toString();

        location.setIdPath(idPath);
    }
}

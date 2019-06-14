package com.vivolvle.redis_cache.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * @Author: weilz
 * @Date: 2019/6/14 17:46
 */
@Entity
@Table(name = "map_theme", schema = "map")
public class MapTheme {
    private long id;
    private String name;
    private long resourceId;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "resource_id")
    public long getResourceId() {
        return resourceId;
    }

    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapTheme mapTheme = (MapTheme) o;
        return id == mapTheme.id &&
                resourceId == mapTheme.resourceId &&
                Objects.equals(name, mapTheme.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, resourceId);
    }
}

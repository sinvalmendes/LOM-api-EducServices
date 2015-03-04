package com.nanuvem.lom.api.accountability;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class EntityType implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer version;
    private String name;
    private String namespace;
    private List<PropertyType> propertyTypes = new LinkedList<PropertyType>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {

        this.namespace = (namespace == null) ? "" : namespace;
    }

    public String getFullName() {
        String fullName = "";
        if (this.namespace != null && !this.namespace.isEmpty()) {
            fullName = this.namespace + ".";
        }
        return fullName + this.getName();
    }

    public List<PropertyType> getAttributes() {
        return propertyTypes;
    }

    public void setAttributes(List<PropertyType> propertyTypes) {
        this.propertyTypes = propertyTypes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EntityType other = (EntityType) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (namespace == null) {
            if (other.namespace != null)
                return false;
        } else if (!namespace.equals(other.namespace))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Entity [namespace=" + namespace + ", name=" + name + ", id=" + id + ", version=" + version + "]";
    }

}

package com.douglasdb.camel.feat.core.domain.menu;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 
 */
@Data
@RequiredArgsConstructor
public class MenuItem {

    private int id;
    private int cost;

    private String name;
    private String description;

    /**
     * @args item
     */
    public MenuItem(@NotNull MenuItem item) {
        this.id = item.id;
        this.cost = item.cost;
        this.name = item.name;
        this.description = item.description;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        MenuItem menuItem = (MenuItem) o;

        if (id != menuItem.id)
            return false;
        if (cost != menuItem.cost)
            return false;
        if (name != null ? !name.equals(menuItem.name) : menuItem.name != null)
            return false;
        return description != null ? description.equals(menuItem.description) : menuItem.description == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + cost;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MenuItem{" + "id=" + id + ", name='" +
        		name + '\'' + ", cost=" + 
        		cost + ", description='" + description
                + '\'' + '}';
    }

}
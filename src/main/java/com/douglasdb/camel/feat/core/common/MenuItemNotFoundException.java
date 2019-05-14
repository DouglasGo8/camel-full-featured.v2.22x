package com.douglasdb.camel.feat.core.common;

import lombok.Getter;

/**
 * 
 */
@Getter
public class MenuItemNotFoundException extends Exception {

    private static final long serialVersionUID = -1L;

    private int id;

    /**
     * 
     * @param id
     */
    public MenuItemNotFoundException(int id) {
        super("Menu Item id " + id + " not found");
        this.id = id;
    }

}
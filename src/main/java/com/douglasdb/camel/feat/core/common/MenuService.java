package com.douglasdb.camel.feat.core.common;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.douglasdb.camel.feat.core.domain.menu.MenuItem;

import lombok.Data;

/**
 * 
 */
@Data
public class MenuService {

    private AtomicInteger ids = new AtomicInteger();
    private final Map<Integer, MenuItem> menuItems = new TreeMap<>();

    public MenuService() throws Exception {

        MenuItem item = new MenuItem();
        item.setName("Coffee");
        item.setCost(1);
        item.setDescription("Kona Zoom Zoom");
        //
        createMenuItem(item);

        item = new MenuItem();
        item.setName("Bagel");
        item.setCost(3);
        item.setDescription("Bagel with Cream Cheese");
        //
        createMenuItem(item);
    }

    /**
     * 
     * @return
     */
    public Collection<MenuItem> getMenuItems() {
        return Collections.unmodifiableCollection(menuItems.values());
    }

    /**
     * 
     */
    public MenuItem getMenuItem(int itemId) throws MenuItemNotFoundException {

        MenuItem item = menuItems.get(itemId);

        if (item == null) {
        	//
            throw new MenuItemNotFoundException(itemId);
        }
        
        return item;
    }

    public void removeMenuItem(int itemId) {
        menuItems.remove(itemId);
    }

    /**
     * 
     */
    public void updateMenuItem(int itemId, MenuItem item) throws MenuItemInvalidException {
        if (!menuItems.containsKey(itemId)) {
            createMenuItem(itemId, item);
        }
        menuItems.put(item.getId(), item);
    }

    /**
     * 
     * @param item
     * @return
     * @throws MenuItemInvalidException
     */
    public int createMenuItem(MenuItem item) throws MenuItemInvalidException {
        return createMenuItem(ids.incrementAndGet(), item);
    }

    /**
     * 
     * @param item
     */
    private int createMenuItem(int itemId, MenuItem item) throws MenuItemInvalidException {

        if (menuItems.containsKey(item.getId())) {
            throw new MenuItemInvalidException("itemID " + item.getId() + " already exists");
        }

        if (item.getCost() <= 0) {
            throw new MenuItemInvalidException("Cost must be greater than 0");
        }

        MenuItem itemCopy = new MenuItem(item);
        itemCopy.setId(itemId);
        menuItems.put(itemId, itemCopy);
        return itemId;
    }

}
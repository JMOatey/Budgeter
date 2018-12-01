/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmokvfbudgeter;

import javafx.beans.property.StringProperty;

/**
 *
 * @author John Oatey
 */
public interface Categorizable {
    public String getCategory();
    public StringProperty categoryProperty();
    public void setCategory(String category);
}

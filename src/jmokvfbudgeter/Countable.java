/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmokvfbudgeter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Callback;

/**
 *
 * @author John Oatey
 */
public abstract class Countable implements Categorizable, Serializable {
    protected DoubleProperty amount;
    protected DoubleProperty difference;
    protected StringProperty description;
    protected StringProperty category;
    
    public Countable(Double amount, String description) {
        setAmount(amount);
        setDescription(description);
        setCategory("No category");
    }
    
    public Countable(Double amount, String description, String category) {
        this(amount, description);
        setCategory(category);
    }
    
    public static Callback<Countable, Observable[]> extractor() {
        return (Countable c) -> new Observable[]{
            c.amountProperty(),
            c.descriptionProperty(),
            c.categoryProperty()
        };
    }
    
    public abstract Double getDifference();
    
    public DoubleProperty amountProperty() {
        if (amount == null) {
            amount = new SimpleDoubleProperty();
        }
        return amount; 
    }
    
    public Double getAmount() {
        return amountProperty().get();
    }
    
    public void setAmount(Double amount) {
        amountProperty().set(amount);
    }
    
    public DoubleProperty differenceProperty() {
        if (difference == null) {
            difference = new SimpleDoubleProperty();
        }
        return difference; 
    }

    public void setDifference(Double difference) {
        differenceProperty().set(difference);
    }
    
    public StringProperty descriptionProperty() {
        if (description == null) {
            description = new SimpleStringProperty();
        }
        return description; 
    }
    
    public String getDescription() {
        return descriptionProperty().get();
    }

    public void setDescription(String description) {
        descriptionProperty().set(description);
    }
    
    @Override
    public StringProperty categoryProperty() {
        if (category == null) {
            category = new SimpleStringProperty();
        }
        return category; 
    }

    @Override
    public String getCategory() {
        return categoryProperty().get();
    }

    @Override
    public void setCategory(String category) {
        categoryProperty().set(category);
    }
    
    private void writeObject(ObjectOutputStream stream)
            throws IOException {
        stream.writeObject(getAmount());
        stream.writeObject(getDifference());
        stream.writeObject(getDescription());
        stream.writeObject(getCategory());
    }

    private void readObject(ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        setAmount((Double) stream.readObject());
        setDifference((Double) stream.readObject());
        setDescription((String) stream.readObject());
        setCategory((String) stream.readObject());
    }    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmokvfbudgeter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author John Oatey
 */
public class Account implements Categorizable, Serializable{
    private transient StringProperty name;
    private transient DoubleProperty amountLeft;
    private transient ObservableList<Countable> revenue = FXCollections.observableArrayList(Countable.extractor());
    private transient StringProperty category;
    private transient ObservableList<Countable> incomes = FXCollections.observableArrayList(Countable.extractor());
    private transient ObservableList<Countable> expenses = FXCollections.observableArrayList(Countable.extractor());
    
    public Account(String name) {
        setName(name);
        setAmountLeft(0.0);
        setCategory("No category");
    }
    
    public Account(String name, String category) {
        this(name);
        setCategory(category);
    }
    
    public static Callback<Account, Observable[]> extractor() {
        return (Account a) -> new Observable[]{
            a.categoryProperty(),
            a.nameProperty()
        };
    }
    
    public StringProperty nameProperty() {
        if(name == null) {
            name = new SimpleStringProperty();
        }
        return name;
    }
    
    public final String getName() {
        return nameProperty().get();
    }
    
    public final void setName(String name) {
        nameProperty().set(name);
    }
    
    public ObservableList<Countable> getRevenue() {
        return this.revenue;
    }
    
    public void setRevenue(ArrayList<Countable> revenue){
        this.revenue = FXCollections.observableArrayList(Countable.extractor());
        for(Countable money : revenue){
            this.revenue.add(money);
        }
    }
    
    public DoubleProperty amountLeftProperty() {
        if(amountLeft == null) {
            amountLeft = new SimpleDoubleProperty();
        }
        return amountLeft;
    }
    
    public final void setAmountLeft(Double amount) {
        amountLeftProperty().set(amount);
    }
    
    public Double getAmountLeft() {
        return amountLeftProperty().get();
    }
    

    public void addAmount(double amount, String name, String category) throws NumberFormatException{
        Countable money = null;
        if(amount > 0) {
            money = (category == "") ? new Income(amount, name) : new Income(amount, name, category);
        }
        else {
            money = (category == "") ? new Expense(amount * -1, name) : new Expense(amount * -1, name, category);
        }
        revenue.add(money);

        setAmountLeft(getAmountLeft() + money.getDifference());
    }
    
    public ObservableList<Countable> getIncomes() {
        setIncomes();
        return this.incomes;
    }
    
    private void setIncomes(){
        incomes = FXCollections.observableArrayList(Countable.extractor());
        for(Countable money : revenue){
            if(money instanceof Income){
                incomes.add(money);
            }
        }
    }
    
    private void setIncomes(ArrayList<Income> incomes){
        this.incomes = FXCollections.observableArrayList(Countable.extractor());
        for(Countable money : incomes){
            this.incomes.add(money);
        }
    }
    
    public ObservableList<Countable> getExpenses() {
        setExpenses();
        return this.expenses;
    }
    
    private void setExpenses(){
        expenses = FXCollections.observableArrayList(Countable.extractor());
        for(Countable money : revenue){
            if(money instanceof Expense){
                expenses.add(money);
            }
        }
    }
    
    private void setExpenses(ArrayList<Expense> expenses){
        this.expenses = FXCollections.observableArrayList(Countable.extractor());
        for(Countable money : expenses){
            this.expenses.add(money);
        }
    }
    
    @Override
    public final String getCategory() {
        return categoryProperty().get();
    }

    @Override
    public StringProperty categoryProperty() {
        if (category == null) {
            category = new SimpleStringProperty();
        }
        return category;
    }

    @Override
    public final void setCategory(String category) {
        categoryProperty().set(category);
    }
    
    private void writeObject(ObjectOutputStream stream)
            throws IOException {
        stream.writeObject(getName());
        stream.writeObject(getAmountLeft());
        stream.writeObject(new ArrayList<Countable>(revenue));
        stream.writeObject(getCategory());
        stream.writeObject(new ArrayList<Countable>(incomes));
        stream.writeObject(new ArrayList<Countable>(expenses));
    }

    private void readObject(ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        setName((String) stream.readObject());
        setAmountLeft((Double) stream.readObject());
        setRevenue((ArrayList) stream.readObject());
        setCategory((String) stream.readObject());
        setIncomes((ArrayList) stream.readObject());
        setExpenses((ArrayList) stream.readObject());
    }
}

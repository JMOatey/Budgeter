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
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author John Oatey
 */
public class AccountGroup implements Serializable{
    private transient StringProperty name;
    private transient ObservableList<Account> accounts = FXCollections.observableArrayList(Account.extractor());
   
    public AccountGroup(String name){
        setName(name);
    }
    
    public AccountGroup(String name, List<Account> accounts){
        this(name);
        this.accounts = FXCollections.observableArrayList(accounts);
    }
    
     public StringProperty nameProperty() {
        if (name == null) {
            name = new SimpleStringProperty();
        }
        return name;
    }
        
    public final String getName() {
        return nameProperty().get();
    }
     
    public final void setName(String name){
        nameProperty().set(name);
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }
    
    public void removeAccount(Account account) {
        accounts.remove(account);
    }
    
    public ObservableList<Account> getAccounts() {
        return this.accounts;
    }
    
    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = FXCollections.observableArrayList(accounts);
    }
    
    private void writeObject(ObjectOutputStream stream)
            throws IOException {
        stream.writeObject(getName());
        ArrayList<Account> accounts = new ArrayList<Account>(this.accounts);
        stream.writeObject(accounts);
    }
    
    private void readObject(ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        this.setName((String) stream.readObject());
        this.setAccounts((ArrayList<Account>) stream.readObject());
    }
}

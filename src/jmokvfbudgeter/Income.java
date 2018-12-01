/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmokvfbudgeter;

/**
 *
 * @author John Oatey
 */
public class Income extends Countable {
    
    public Income(Double amount, String description){
        super(amount, description);
    }
    
    public Income(Double amount, String description, String category){
        super(amount, description, category);
    }
 
    @Override
    public void setCategory(String category) {
        categoryProperty().set(category + " - Income");
    }
    
    @Override
    public Double getDifference() {
        return this.amount.get();
    }  
}

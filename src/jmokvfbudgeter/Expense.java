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
public class Expense extends Countable {
    
    public Expense(Double amount, String description) {
        super(amount, description);
    }
    
    public Expense(Double amount, String description, String category) {
       super(amount, description, category);
    }
   
    @Override
    public final void setCategory(String category) {
        categoryProperty().set(category + " - Expense");
    }
    
    @Override
    public Double getDifference() {
        return this.amount.get() * -1;
    }
}

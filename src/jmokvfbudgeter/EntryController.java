/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmokvfbudgeter;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

/**
 *
 * @author John Oatey
 */
public class EntryController extends Switchable implements Initializable {
    
    @FXML
    TextArea info;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        info.setText(
                "Thank you for trying my app. It's primary use case is for families to manage all of their "
                        + "accounts (Credit/Debit). This can let parents know what's going on with all of their children "
                        + "as well as their own. Click the Budget button to start now! I hope it helps make life easier\n\n"
                        + "Now a little bit about me. My name is John Michael. I am a Junior in Computer Science. "
                        + "I have a huge passion for cs and want to use it to create the best applications I can "
                        + "in my career. I want to try working in the vido game industry to possibly make a game "
                        + "that can influence people in the same way they have me."
        );
    }  
    
    @FXML
    public void switchToBudget(ActionEvent event) {
        Switchable.switchTo("Budget");
    }
    
}

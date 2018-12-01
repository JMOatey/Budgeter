/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmokvfbudgeter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author John Oatey
 */
public class BudgetController extends Switchable implements Initializable {

    AccountGroup accountGroup;
    Account selectedAccount;
    Countable selectedTransaction;
    AccountState state = AccountState.None;
    File file;
    Boolean loaded = false;
    DecimalFormat df;
    
    @FXML
    private TableView<Countable> transactionsList;
    
    @FXML
    private TableColumn<Countable, String> nameColumn;
    
    @FXML
    private TableColumn<Countable, String> categoryColumn;
    
    @FXML
    private TableColumn<Countable, Double> amountColumn;
    
    @FXML
    private TextField totalAmount;
    
    @FXML
    private ListView accountsList;
    
    @FXML
    private VBox entryBox;
    
    @FXML
    private VBox accountVBox;
    
    @FXML
    private Label entryLabel;
    
    @FXML
    private TextField nameText;
    
    @FXML
    private TextField amountText;
    
    @FXML
    private TextField categoryText;
    
    @FXML
    private ToggleGroup accountToggle;
    
    @FXML
    private Label errorLabel;
    
    @FXML
    private Label accountGroupLabel;
    
    @FXML
    private Button addAccountButton;
    
    @FXML
    private Button addTransactionButton;
    
    @FXML
    private Button submitButton;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        df = new DecimalFormat("0.00");
    }
    
    @FXML
    public void submitEntryBox(ActionEvent action){
        RadioButton button = null;
        switch(state){
            case EditAccount:
                button = (RadioButton) accountToggle.getSelectedToggle();
                selectedAccount.setCategory(button.getText());
                selectedAccount.setName(nameText.getText());
                break;
            case AddAccount:
                button = (RadioButton) accountToggle.getSelectedToggle();
                accountGroup.addAccount(new Account(nameText.getText(), button.getText()));
                break;
            case AddAmount:
                try{
                    Double amount = Double.parseDouble(amountText.getText());                   
                    errorLabel.setText("");
                    selectedAccount.addAmount(amount, nameText.getText(), categoryText.getText());
                    transactionsList.getItems().setAll(selectedAccount.getRevenue()); 
                }
                catch(NumberFormatException e){
                    errorLabel.setText("Invalid amount");
                }
                break;
            case EditAmount:
                selectedTransaction.setAmount(Double.parseDouble(amountText.getText()));
                selectedTransaction.setCategory(categoryText.getText());
                selectedTransaction.setDescription(nameText.getText());
                break;
            case CreateAccount:
                transactionsList.getItems().setAll();
                accountGroup = new AccountGroup(nameText.getText());
                accountsList.setItems(accountGroup.getAccounts());
                accountGroupLabel.setText(accountGroup.getName());
                selectedAccount = null;
                selectedTransaction = null;
                addTransactionButton.setDisable(true);
                break;
        }
    }
    
    @FXML
    public void save(ActionEvent action){
        if(this.file == null){
            FileChooser fileChooser = new FileChooser();

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(null);
            saveFile(accountGroup, file);
            this.file = file;
        }
        else{
            saveFile(accountGroup, this.file);
        }
    }
    
    @FXML
    public void saveAs(ActionEvent action){
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(null);
        saveFile(accountGroup, file);
        this.file = file;
    }
    
    private void saveFile(AccountGroup toSave, File file){
        try {
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream o = new ObjectOutputStream(f);

            o.writeObject(toSave);

            o.close();
            f.close();
            errorLabel.setText("");
        } catch (FileNotFoundException e) {
                System.out.println("File not found");
                e.printStackTrace();
        }catch (IOException e) {
                errorLabel.setText("Invalid file choice");
                e.printStackTrace();
        }
    }
    
    @FXML
    public void load(ActionEvent action){
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(null);
        AccountGroup accountGroup = null;
        if(file != this.file){
            accountGroup = readFile(file);
        }
        if(accountGroup != null){
            this.accountGroup = accountGroup;
            accountGroupLabel.setText(accountGroup.getName());
            addAccountButton.setDisable(false);
            addTransactionButton.setDisable(true);
            selectedAccount = null;
            selectedTransaction = null;
            transactionsList.getItems().setAll();
            accountsList.getSelectionModel().clearSelection();
            setListeners();
        }
        if(!loaded){
            loaded = true;
        }
    }
    
    private AccountGroup readFile(File file){
        AccountGroup accountGroup = null;
         try {
            FileInputStream fi = new FileInputStream(file);
            ObjectInputStream oi = new ObjectInputStream(fi);

            accountGroup = (AccountGroup) oi.readObject();

            oi.close();
            fi.close();
            errorLabel.setText("");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }catch (IOException e) {
            errorLabel.setText("Invalid file choice");
            System.out.println("Error initializing stream");
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
         return accountGroup;
    }
    
    @FXML
    public void switchToEntry(ActionEvent action) {
        Switchable.switchTo("Entry");
    }
    
    @FXML
    public void addAccount(ActionEvent action) {
        clearTexts();
        accountVBox.setVisible(true);
        entryBox.setVisible(true);
        state = AccountState.AddAccount;
        entryLabel.setText("Enter Account Info");
        amountText.setDisable(true);
        categoryText.setDisable(true);
        submitButton.setText("Add");
    }
    
    @FXML
    public void addTransaction(ActionEvent action) {
        clearTexts();
        state = AccountState.AddAmount;
        accountVBox.setVisible(false);
        entryLabel.setText("Enter Transaction");
        amountText.setDisable(false);
        categoryText.setDisable(false);
        submitButton.setText("Add");
    }
    
    @FXML
    public void createGroup(ActionEvent action) {
        clearTexts();
        entryBox.setVisible(true);
        state = AccountState.CreateAccount;
        accountVBox.setVisible(false);
        entryLabel.setText("Create Group");
        amountText.setDisable(true);
        categoryText.setDisable(true);
        submitButton.setText("Create");
    }
    
    private void changeAccount(Account account) {
        this.selectedAccount = account;
        
        this.selectedAccount.amountLeftProperty().addListener(new ChangeListener(){ 
            @Override 
            public void changed(ObservableValue o,Object oldVal, 
                 Object newVal){
                if(account != null){
                    totalAmount.setText(df.format((Double) newVal));
                }
            }
        });
        transactionsList.getItems().setAll(account.getRevenue());
    }
    
    private void clearTexts() {
        amountText.clear();
        nameText.clear();
        categoryText.clear();
    }
    
     private void setListeners() {

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        
        amountColumn.setCellFactory(column -> new TableCell<Countable, Double>(){
            @Override 
            public void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(df.format(amount));
                }
            }
        });
      
        accountsList.setCellFactory(new Callback<ListView<Account>, ListCell<Account>>() {
            @Override
            public ListCell<Account> call(ListView<Account> param) {
              final ListCell<Account> cell = new ListCell<Account>() {
                @Override
                public void updateItem(Account account, boolean empty) {
                  super.updateItem(account, empty);
                  if (account != null) {
                    setText(account.getName() + " - " + account.getCategory());
                  }
                }
              };
              return cell;
            }
        });
        
        accountsList.setItems(accountGroup.getAccounts());
        
        transactionsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Countable>(){
                @Override
                public void changed(ObservableValue<? extends Countable> observable, Countable oldValue, Countable newValue) {
                    clearTexts();
                    entryLabel.setText("Edit field");
                    
                    accountVBox.setVisible(false);
                    amountText.setDisable(false);
                    categoryText.setDisable(false);
                    
                    entryBox.setVisible(true);
                    selectedTransaction = newValue;
                    
                    nameText.setText(newValue.getDescription());
                    amountText.setText(df.format(newValue.getAmount()));
                    String category = newValue.getCategory().split(" - ")[0];
                    categoryText.setText(category);
                    
                    state = AccountState.EditAmount;
                    submitButton.setText("Submit");
                }
            });
        
        accountsList.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<Account>(){
                @Override
                public void changed(ObservableValue<? extends Account> observable, Account oldValue, Account newValue) {
                    if(newValue != null){
                        clearTexts();
                        changeAccount(newValue);                    
                        entryLabel.setText("Edit Account Info");
                        addTransactionButton.setDisable(false);

                        categoryText.setDisable(true);
                        amountText.setDisable(true);

                        entryBox.setVisible(true); 
                        accountVBox.setVisible(true);

                        totalAmount.setText(df.format(newValue.getAmountLeft()));
                        state = AccountState.EditAccount;
                        submitButton.setText("Submit");  
                    }
                }
            });
    }
}

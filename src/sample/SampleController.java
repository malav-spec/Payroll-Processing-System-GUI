package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import model.Date;

import java.time.LocalDate;


public class SampleController {

    @FXML
    public Button addButton;

    @FXML
    private TextArea messageArea;

    @FXML
    private TextField nameField;

    @FXML
    private RadioButton parttimeButton, fulltimeButton, managementButton;

    @FXML
    private TextField salaryTextField, hoursTextField, payrateTextField;

    @FXML
    private RadioButton managerButton, deptheadButton, directorButton;

    @FXML
    private DatePicker dateField;

    @FXML
    private RadioButton eceButton, csButton, itButton;

    @FXML
    private ToggleGroup group, employeeGroup, roles;

    @FXML
    public void display(ActionEvent actionEvent) { //Just for reference
        RadioButton rb = (RadioButton) group.getSelectedToggle();
        messageArea.setText(rb.getText());
        LocalDate d = dateField.getValue();
        //messageArea.appendText(String.valueOf(d.getDayOfYear()));
        messageArea.appendText(" " + d.getDayOfMonth()); //Gives the day
        messageArea.appendText(" " + d.getYear()); //Gives year
        messageArea.appendText(" " + d.getMonthValue()); //Gives month
        messageArea.appendText(" " + nameField.getText());
    }

    @FXML
    public void disableOptions(ActionEvent event){
        RadioButton temp = (RadioButton) employeeGroup.getSelectedToggle();
        String employeeType = temp.getText();

        switch (employeeType) {
            case "Part-time" -> {
                salaryTextField.setDisable(true);
                managerButton.setDisable(true);
                directorButton.setDisable(true);
                deptheadButton.setDisable(true);
                hoursTextField.setDisable(false);
                payrateTextField.setDisable(false);
            }
            case "Full-time" -> {
                hoursTextField.setDisable(true);
                payrateTextField.setDisable(true);
                managerButton.setDisable(true);
                directorButton.setDisable(true);
                deptheadButton.setDisable(true);
                salaryTextField.setDisable(false);
            }
            case "Management" -> {
                hoursTextField.setDisable(true);
                payrateTextField.setDisable(true);
                managerButton.setDisable(false);
                directorButton.setDisable(false);
                deptheadButton.setDisable(false);
                salaryTextField.setDisable(false);
            }
        }
    }

    @FXML
    public void resetFields(){

        salaryTextField.clear();
        salaryTextField.setDisable(false);

        hoursTextField.clear();
        hoursTextField.setDisable(false);

        payrateTextField.clear();
        payrateTextField.setDisable(false);

        nameField.clear();

        dateField.getEditor().clear();

        group.getSelectedToggle().setSelected(false);
        employeeGroup.getSelectedToggle().setSelected(false);

        if(roles.getSelectedToggle() != null) {
            roles.getSelectedToggle().setSelected(false);
        }

        managerButton.setDisable(false);
        deptheadButton.setDisable(false);
        directorButton.setDisable(false);
    }

    @FXML
    public void addEmployee(ActionEvent event){
        if(checkValues(null)){
            resetFields();
            messageArea.appendText("Employee added.\n");
        }
    }
    @FXML
    public boolean checkValues(ActionEvent event){

        if(nameField.getText().equals("")){
            messageArea.appendText("Enter a name.\n");
            return false;
        }
        if(group.getSelectedToggle() == null){
            messageArea.appendText("Select a department.\n");
            return false;
        }

        if(employeeGroup.selectedToggleProperty() == null){
            messageArea.appendText("Select employee type.\n");
            return false;
        }

        if(!checkDate()){
            messageArea.appendText("Select a valid hiring date.\n");
            return false;
        }

        RadioButton temp = (RadioButton) employeeGroup.getSelectedToggle();

        if(temp == null){
            messageArea.appendText("Employee type missing.\n");
            return false;
        }

        String employeeType = temp.getText();

         if(employeeType.equals("Part-time")){

             if(payrateTextField.getText().equals("")){
                 messageArea.appendText("Enter pay rate.\n");
                 return false;
             }

            if(!hoursTextField.getText().equals("") && Integer.parseInt(hoursTextField.getText()) < 0){
                messageArea.appendText("Hours worked cannot be negative.\n");
                return false;
            }
            else if(!hoursTextField.getText().equals("") && Integer.parseInt(hoursTextField.getText()) > 100){
                messageArea.appendText("Hours worked cannot be greater than 100.\n");
                return false;
            }

            if(Integer.parseInt(payrateTextField.getText()) < 0){
                messageArea.appendText("Pay rate cannot be negative\n");
                return false;
            }
        }

         if(employeeType.equals("Full-time")){

             if(salaryTextField.getText().equals("")){
                 messageArea.appendText("Enter salary.\n");
                 return false;
             }

            if(Integer.parseInt(salaryTextField.getText()) < 0){
                messageArea.appendText("Salary cannot be negative.\n");
                return false;
            }
        }

        else if(employeeType.equals("Management")){

             if(salaryTextField.getText().equals("")){
                 messageArea.appendText("Enter salary.\n");
                 return false;
             }

            if(Integer.parseInt(salaryTextField.getText()) < 0){
                messageArea.appendText("Salary cannot be negative.\n");
                return false;
            }
            else if(roles.getSelectedToggle() == null){
                messageArea.appendText("Select a management role.\n");
                return false;
            }
        }

        return true;
    }

    @FXML
    public boolean checkDate(){
        LocalDate d = dateField.getValue();

        if(d == null){
            return false;
        }

        String tempDate = d.getMonthValue() + "/" + d.getDayOfMonth() + "/" + d.getYear();

        Date date = new Date(tempDate);

        return date.isValid();
    }


}

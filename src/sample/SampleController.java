package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import model.*;

import java.time.LocalDate;


public class SampleController {

    @FXML
    public Button addButton;

    @FXML
    public ComboBox printBox;

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

    Company company = new Company();

    @FXML
    public void display(ActionEvent actionEvent) { //Just for reference
        RadioButton rb = (RadioButton) group.getSelectedToggle();
        messageArea.setText(rb.getText());
        LocalDate d = dateField.getValue();
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

            Profile profile = new Profile(nameField.getText(), getDepartment(), getDate());
            RadioButton temp = (RadioButton) employeeGroup.getSelectedToggle();
            String employeeType = temp.getText();

            if(employeeType.equals("Part-time")){

                Parttime parttime = new Parttime(profile, getPayRate());

                if(company.add(parttime)){
                    messageArea.appendText("Employee added.\n");
                }
                else{
                    messageArea.appendText("Employee is already in the list.\n");
                }
            }

            else if(employeeType.equals("Full-time")){

                Fulltime fulltime = new Fulltime(profile, getSalary());
                if(company.add(fulltime)){
                    messageArea.appendText("Employee added.\n");
                }
                else{
                    messageArea.appendText("Employee is already in the list.\n");
                }
            }

            else if(employeeType.equals("Management")){

                Fulltime management = new Management(profile, getSalary(), getRole());

                if(company.add(management)){
                    System.out.println("Employee added.");
                }
                else{
                    System.out.println("Employee is already in the list.");
                }
            }
            resetFields();
        }
    }

    @FXML
    private void printByDepartment(){
        if(company.getNumEmployee() == 0){
            System.out.println("Employee database empty!");
            return;
        }

        company.printByDepartment();
    }

    @FXML
    private void printByDateHired(){
        if(company.getNumEmployee() == 0){
            System.out.println("Employee database empty!");
            return;
        }

        company.printByDate();
    }

    @FXML
    private void printEmployees(){
        if(company.getNumEmployee() == 0){
            System.out.println("Employee database empty!");
            return;
        }

        company.print();
    }

    @FXML
    public void printFunctions(ActionEvent event){
        if(printBox.getEditor().getText().equals("Print by Date")){
            printByDateHired();
        }
        else if(printBox.getEditor().getText().equals("Print by Department")){
            printByDepartment();
        }
        else{
            printEmployees();
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

            if(Double.parseDouble(payrateTextField.getText()) < 0){
                messageArea.appendText("Pay rate cannot be negative\n");
                return false;
            }
        }

         if(employeeType.equals("Full-time")){

             if(salaryTextField.getText().equals("")){
                 messageArea.appendText("Enter salary.\n");
                 return false;
             }

            if(Double.parseDouble(salaryTextField.getText()) < 0){
                messageArea.appendText("Salary cannot be negative.\n");
                return false;
            }
        }

        else if(employeeType.equals("Management")){

             if(salaryTextField.getText().equals("")){
                 messageArea.appendText("Enter salary.\n");
                 return false;
             }

            if(Double.parseDouble(salaryTextField.getText()) < 0){
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

    @FXML
    private String getDepartment(){
        RadioButton rb = (RadioButton) group.getSelectedToggle();
        return rb.getText();
    }

    @FXML
    private Date getDate(){
        LocalDate d = dateField.getValue();
        String tempDate = d.getMonthValue() + "/" + d.getDayOfMonth() + "/" + d.getYear();
        Date date = new Date(tempDate);
        return date;
    }

    @FXML
    private double getSalary(){
        return Double.parseDouble(salaryTextField.getText());
    }

    @FXML
    private int getHours(){
        return Integer.parseInt(hoursTextField.getText());
    }

    @FXML
    private double getPayRate(){
        return Double.parseDouble(payrateTextField.getText());
    }

    @FXML
    private int getRole(){
        RadioButton rb = (RadioButton) group.getSelectedToggle();
        String management = rb.getText();

        if(management.equals("Director")){
            return 3;
        }
        else if(management.equals("Department Head")){
            return 2;
        }
        else{
            return 1;
        }
    }


}

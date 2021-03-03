package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.time.LocalDate;


public class SampleController {

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
        messageArea.appendText(String.valueOf(" " + d.getDayOfMonth())); //Gives the day
        messageArea.appendText(String.valueOf(" " + d.getYear())); //Gives year
        messageArea.appendText(String.valueOf(" "+ d.getMonthValue())); //Gives month
        messageArea.appendText(String.valueOf(" " + nameField.getText()));
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


}

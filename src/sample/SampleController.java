package sample;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.StringTokenizer;
import model.*;


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
                    messageArea.appendText("Employee added.");
                }
                else{
                    messageArea.appendText("Employee is already in the list.");
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

            try {
                if (!hoursTextField.getText().equals("") && Integer.parseInt(hoursTextField.getText()) < 0) {
                    messageArea.appendText("Hours worked cannot be negative.\n");
                    return false;
                } else if (!hoursTextField.getText().equals("") && Integer.parseInt(hoursTextField.getText()) > 100) {
                    messageArea.appendText("Hours worked cannot be greater than 100.\n");
                    return false;
                }

                if(Double.parseDouble(payrateTextField.getText()) < 0){
                    messageArea.appendText("Pay rate cannot be negative\n");
                    return false;
                }
            }
            catch (NumberFormatException e){
                messageArea.appendText("Enter numbers.\n");
                return false;
            }

        }

        if(employeeType.equals("Full-time")){

            if(salaryTextField.getText().equals("")){
                messageArea.appendText("Enter salary.\n");
                return false;
            }

            try {
                if (Double.parseDouble(salaryTextField.getText()) < 0) {
                    messageArea.appendText("Salary cannot be negative.\n");
                    return false;
                }
            }
            catch (NumberFormatException e){
                messageArea.appendText("Enter numbers.\n");
                return false;
            }
        }

        else if(employeeType.equals("Management")){

            if(salaryTextField.getText().equals("")){
                messageArea.appendText("Enter salary.\n");
                return false;
            }

            try {
                if (Double.parseDouble(salaryTextField.getText()) < 0) {
                    messageArea.appendText("Salary cannot be negative.\n");
                    return false;
                }
                else if(roles.getSelectedToggle() == null){
                    messageArea.appendText("Select a management role.\n");
                    return false;
                }
            }
            catch (NumberFormatException e) {
                messageArea.appendText("Enter numbers.\n");
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
    @FXML
    public void remove(){
        RadioButton rb = (RadioButton) group.getSelectedToggle();
        String dept = rb.getText();
        LocalDate d = dateField.getValue();
        String name = nameField.getText();
        Date date = new Date(""+d.getMonthValue()+"/"+d.getDayOfMonth()+"/"+d.getYear());
        Profile profile = new Profile(name, dept, date);

        Management management = new Management(profile, 0, 0);
        Parttime parttime = new Parttime(profile, 0);

        if(!company.remove(management)){
            if(company.remove(parttime)){
                messageArea.appendText("Employee removed.");

            }
            else {
                messageArea.appendText("Employee does not exist.");
            }
        }
        else {
            messageArea.appendText("Employee removed.");
        }


    }

    @FXML
    public void file(){
        String path = getPath();
        File myObj = new File(path);
        try{
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String fileData = myReader.nextLine();
                StringTokenizer st = new StringTokenizer(fileData,",",false);
                while (st.hasMoreTokens()){
                    String employeeType = st.nextToken();
                    if(employeeType.equals("P")){
                        partTime(st);
                    }
                    else if(employeeType.equals("F")){
                        fullTime(st);
                    }

                    else if(employeeType.equals("M")){
                        management(st);
                    }
                }
            }
            company.print();
            myReader.close();
        } catch (FileNotFoundException e) {
        }

    }

    private String getPath() // Add exception if the user does not select any file
    {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = chooser.showOpenDialog(new Stage());
        String path = file.getAbsolutePath();
        return path;
    }

    private void partTime(StringTokenizer st){
        while (st.hasMoreTokens()){
            String name = st.nextToken();
            String dept = st.nextToken();
            String dateIn = st.nextToken();
            Date date = new Date(dateIn);
            Double hourlyPayRate =  Double.parseDouble(st.nextToken());
            Profile profile = new Profile(name, dept, date);
            Parttime parttime = new Parttime(profile, hourlyPayRate);
            company.add(parttime);
        }
    }

    private void fullTime(StringTokenizer st){
        while (st.hasMoreTokens()){
            String name = st.nextToken();
            String dept = st.nextToken();
            String dateIn = st.nextToken();
            Date date = new Date(dateIn);
            Double salary =  Double.parseDouble(st.nextToken());
            Profile profile = new Profile(name, dept, date);
            Fulltime fulltime = new Fulltime(profile, salary);
            company.add(fulltime);
        }
    }


    private void management(StringTokenizer st) {
        while (st.hasMoreTokens()) {
            String name = st.nextToken();
            String dept = st.nextToken();
            String dateIn = st.nextToken();
            Date date = new Date(dateIn);
            Double salary = Double.parseDouble(st.nextToken());
            int role = Integer.parseInt(st.nextToken());
            Profile profile = new Profile(name, dept, date);
            Fulltime management = new Management(profile, salary, role);
            company.add(management);
        }
    }
}



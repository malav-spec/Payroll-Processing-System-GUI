package sample;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Company;
import model.Fulltime;
import model.Parttime;
import model.Profile;
import model.Employee;
import model.Management;
import model.Date;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.StringTokenizer;


public class SampleController {

    /**
     *
     */
    @FXML
    public Button addButton;

    /**
     *
     */
    @FXML
    private ComboBox printBox;

    /**
     *
     */
    @FXML
    private ComboBox fileBox;

    /**
     *
     */
    @FXML
    private TextArea messageArea;

    /**
     *
     */
    @FXML
    private TextField nameField;

    /**
     *
     */
    @FXML
    private RadioButton parttimeButton, fulltimeButton, managementButton;

    /**
     *
     */
    @FXML
    private TextField salaryTextField, hoursTextField, payrateTextField;

    /**
     *
     */
    @FXML
    private RadioButton managerButton, deptheadButton, directorButton;

    /**
     *
     */
    @FXML
    private DatePicker dateField;

    /**
     *
     */
    @FXML
    private ToggleGroup group, employeeGroup, roles;

    /**
     *
     */
    Company company = new Company();

// need to remove method redundant
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

    /**
     * Disables fields according to selection of employee type
     * @param event Not used
     */
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

    /**
     * Resets all the fields to be abled
     */
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

    /**
     * Add the employee to the database
     * @param event Not used
     */
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

    /**
     * Prints the database sorted by department
     */
    @FXML
    private void printByDepartment(){
        if(company.getNumEmployee() == 0){
            messageArea.appendText("Employee database empty!\n");
            return;
        }

        Employee[] temp = company.printByDepartment();

        messageArea.appendText("--Printing earning statements by department--\n");

        for(int i = 0; i < company.getNumEmployee(); i++){
            messageArea.appendText(temp[i].toString() + "\n");
        }
    }

    /**
     * Prints the database sorted by date hired
     */
    @FXML
    private void printByDateHired(){
        if(company.getNumEmployee() == 0){
            messageArea.appendText("Employee database empty!\n");
            return;
        }

        Employee[] temp = company.printByDate();

        messageArea.appendText("--Printing earning statements by date hired--\n");
        for(int i = 0; i < company.getNumEmployee(); i++){
            messageArea.appendText(temp[i].toString() + "\n");
        }
    }

    /**
     * Prints the employee database
     */
    @FXML
    private void printEmployees(){
        if(company.getNumEmployee() == 0){
            messageArea.appendText("Employee database empty!\n");
            return;
        }

        Employee[] temp = company.print();

        messageArea.appendText("--Printing Earning statements for all employees--\n");

        for(int i = 0; i < company.getNumEmployee(); i++){
            messageArea.appendText(temp[i].toString() + "\n");
        }
    }

    /**
     * Function associated with the ComboBox for Print functions
     * @param event
     */
    @FXML
    public void printFunctions(ActionEvent event){

        if(printBox.getValue().equals("Print By Date Hired")){
            printByDateHired();
        }
        else if(printBox.getValue().equals("Print By Department")){
            printByDepartment();
        }
        else{
            printEmployees();
        }
    }

    /**
     * Checks if the input fields are valid
     * @param event Not used
     * @return False if there are any invalid inputs. True otherwise
     */
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

    /**
     * Checks if the date field is valid
     * @return True if date is valid, False otherwise
     */
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

    /**
     * Get the department of employee
     * @return Employee department
     */
    @FXML
    private String getDepartment(){
        RadioButton rb = (RadioButton) group.getSelectedToggle();
        return rb.getText();
    }

    /**
     * Get the date hired of employee
     * @return date hired
     */
    @FXML
    private Date getDate(){
        LocalDate d = dateField.getValue();
        String tempDate = d.getMonthValue() + "/" + d.getDayOfMonth() + "/" + d.getYear();
        Date date = new Date(tempDate);
        return date;
    }

    /**
     * Get employee's salary
     * @return Salary of employee
     */
    @FXML
    private double getSalary(){
        return Double.parseDouble(salaryTextField.getText());
    }

    /**
     * Get the hours worked of part time employee
     * @return Hours worked
     */
    @FXML
    private int getHours(){
        return Integer.parseInt(hoursTextField.getText());
    }

    /**
     * Get pay rate of part time employee
     * @return pay rate of employee
     */
    @FXML
    private double getPayRate(){
        return Double.parseDouble(payrateTextField.getText());
    }

    /**
     * Get the management role of employees
     * @return Management role
     */
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

    /**
     * Set the hours of part time employee
     */
    @FXML
    public void setHours(){

        if(!checkValues(null)){
            return;
        }

        Profile profile = new Profile(nameField.getText(), getDepartment(), getDate());

        Parttime part_emp = new Parttime();

        //part_emp.setProfile(profile); //Set profile
        part_emp.setHours(getHours()); // Set hours

        if(company.setHours(part_emp)){
            messageArea.appendText("Working hours set.\n");
        }
        else{
            messageArea.appendText("Employee not found.\n");
        }
    }

    /**
     * Calculates the payments of all the employees
     */
    @FXML
    public void calculate(){

        if(company.getNumEmployee() == 0){
            messageArea.appendText("Employee database empty.\n");
            return;
        }
        company.processPayments();
        messageArea.appendText("Calculation of employee payments done.\n");

    }

    /**
     * Removes the employee from the database
     */
    @FXML
    public void remove(){

        if(!checkValues(null)){
            return;
        }

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
                messageArea.appendText("Employee removed.\n");

            }
            else {
                messageArea.appendText("Employee does not exist.\n");
            }
        }
        else {
            messageArea.appendText("Employee removed.\n");
        }


    }

    /**
     * Used to check user response whether a user wants to import or export the file
     */
    @FXML
    public void file(){
        if(fileBox.getValue().equals("Export")){
            fileExport();
        }
        else if(fileBox.getValue().equals("Import")){
            fileImport();
        }

    }

    /**
     * Exports the Employee data to a file. Uses file chooser to select or make the file.
     */
    private void fileExport() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save File");
        File file = chooser.showSaveDialog(new Stage());
        if(file == null){
            return ;
        }
        String path = file.getAbsolutePath();
        company.exportDatabase(path);
    }


    /**
     *Imports data from a file and stores it to the Employee array
     */
    private void fileImport(){
        String path = getPath();

        if(path == null){
            return;
        }

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
        }
        catch (FileNotFoundException e) {
        }

    }

    /**
     * Implements file chooser to select file and return its path
     * @return Path of the file which we want to import
     */
    private String getPath() // Add exception if the user does not select any file
    {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = chooser.showOpenDialog(new Stage());

        if(file == null){
            return null;
        }
        String path = file.getAbsolutePath();
        return path;
    }

    /**
     * Adds part time employee to the Employee array
     * @param st String Tokenizer which is used to separate the stored string value using particular delim
     */
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

    /**
     * Adds full time employee to the Employee array
     * @param st String Tokenizer which is used to separate the stored string value using particular delim
     */
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


    /**
     * Adds Management level employee to the Employee array
     * @param st String Tokenizer which is used to separate the stored string value using particular delim
     */
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
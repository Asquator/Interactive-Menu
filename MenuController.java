
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;

import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MenuController {

    @FXML
    private VBox entries;

    private final ArrayList<MenuEntry> menu = new ArrayList<>();

    private final ArrayList<String> allowedCategories = new ArrayList<>();

    private final LinkedList<MenuEntry> orderList = new LinkedList<>();

    private static final String MENU_FILE = "menu.txt";

    static final ObservableList<Integer> comboList = FXCollections.observableArrayList();

    /**
     * On order button clicked, confirm the order
     * @param event
     */
    @FXML
    void orderClicked(ActionEvent event) {
        if(orderList.isEmpty())
            return;

        StringBuilder strb = new StringBuilder();
        for (MenuEntry entry : orderList) //accumulating order details
            strb.append(entry.getDescription()).append(" (x").append(entry.getQuantity()).append(")\n");

        String orderDetails = strb.toString();

        //displaying confirmation dialog
        ConfirmationDialog message = new ConfirmationDialog(orderDetails);
        Optional<ButtonType> answer = message.showAndWait();
        if(answer.isEmpty())
            return;

        if(answer.get().equals(ConfirmationDialog.CONFIRM)) {
            saveOrder(orderDetails);
            resetMenu();
        }

        else if(answer.get().equals(ConfirmationDialog.CANCEL))
            resetMenu();
    }


    //reset the menu between orders
    private void resetMenu(){
        orderList.clear();
        for (MenuEntry entry :
                menu) {
            entry.resetDisplay();
        }
    }


    //save order details to file
    private void saveOrder(String orderDetails){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setContentText("Enter your name and ID"); //ask name and ID, then save to file
        var answer = dialog.showAndWait();
        if(answer.isEmpty())
            return;

        try(FileWriter file = new FileWriter(answer.get() + ".txt")){
            file.write(orderDetails);
        }

        catch (IOException ex){
            System.err.println(ex.getMessage());
        }
    }

    //load the menu from file
    private void loadMenu(){
        //reading menu from the specified file
        try(Scanner scan = new Scanner(new File(MENU_FILE))){
            while(scan.hasNext())
                readAddNextEntry(scan);
        }

        catch(FileNotFoundException ex){
            System.err.println("Menu file does not exist");
            System.exit(1);
        }
    }

    /**
     * Initialize the menu application
     */
    public void initialize() {
        allowedCategories.add("Appetizers");
        allowedCategories.add("Main Courses");
        allowedCategories.add("Secondaries");
        allowedCategories.add("Beverages");

        //filling the ComboBox options with numbers in range [1...29]
        for (int i = 1; i < 30; i++) {
            comboList.add(i);
        }

        loadMenu();
        displayMenu();
    }

    //read next entry from the given scanner stream and add it to the meu
    private void readAddNextEntry(Scanner scan){
        try {
            String description = "";
            while(description.equals("")) //skip blank lines
                description = scan.nextLine();

            String category = scan.nextLine();
            if (!allowedCategories.contains(category)) //check if the category is allowed
                throw new IOException("Invalid category " + category);

            double price = scan.nextDouble();
            menu.add(new MenuEntry(description, category, price)); //adding new entry to the list
        }

        catch(IOException | NoSuchElementException | IllegalStateException ex){
            System.err.println("Error parsing menu\n" + ex.getMessage());
            System.exit(1);
        }
    }

    //display the graphical menu
    private void displayMenu(){
        if(menu.isEmpty())
            return;

        String lastCategory = "";
        String currentCategory;

        for(MenuEntry entry : menu) {
            currentCategory = entry.getCategory();
            if (!currentCategory.equals(lastCategory)) { //display and update category if new is encountered
                displayCategory(currentCategory);
                lastCategory = currentCategory;
            }

            //display the remaining entry
            entries.getChildren().add(entry.getHbox()); //adding the node to layout

            //binding checkbox selection to order list
            entry.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                    if(newValue)    //add if selected
                        orderList.add(entry);

                    else    //remove if deselected
                        orderList.remove(entry);
                }
            });
        }
    }


    //displays a category label
    private void displayCategory(String category){
        Label label = new Label(category);
        label.setFont(new Font(14));
        entries.getChildren().add(label);
    }

}

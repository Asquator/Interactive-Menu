package com.example.mmn13q2;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import static com.example.mmn13q2.MenuController.comboList; //static options list for the combo-box

/**
 * Represents a single menu entry
 */
class MenuEntry {

    public static String CURRENCY_SIGN = "€";
    private final String description;
    private final String category;
    private final double price;

    //graphical elements
    private final HBox hbox;
    private final ComboBox<Integer> combo = new ComboBox<>(comboList);
    private final CheckBox check = new CheckBox();
    private int quantity;

    /**
     * Constructs and initialize a menu entry
     * @param description item description
     * @param category item category
     * @param price price for one meal
     */
    public MenuEntry(String description, String category, double price){
        this.description = description;
        this.category = category;
        this.price = price;

        //initializing graphical elements
        Label descLabel = new Label(description);
        Label priceLabel = new Label(String.valueOf(price) + CURRENCY_SIGN);

        //binding combo-box to quantity
        combo.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer oldValue, Integer newValue) {
                quantity = newValue;
            }
        });

        //enable combo-box iff checkbox is ticked
        check.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                combo.setDisable(!observableValue.getValue());
            }
        });

        descLabel.setFont(new Font(18));
        descLabel.setWrapText(true);

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        combo.setMinSize(70,30);

        hbox = new HBox(8, descLabel, spacer, priceLabel, check, combo);

        resetDisplay();
    }

    /**
     * Resets the graphical representation to default
     */
    public void resetDisplay(){
        check.setSelected(false);
        combo.setDisable(true);
        combo.getSelectionModel().selectFirst();
        quantity = 1;
    }

    /**
     * Get checkbox selection property
     * @return the selected property of the check-box
     */
    public BooleanProperty selectedProperty(){
        return check.selectedProperty();
    }

    public HBox getHbox() {
        return hbox;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

}

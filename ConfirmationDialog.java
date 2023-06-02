
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Custom order confirmation dialog
 */
class ConfirmationDialog extends Alert {

    public static ButtonType CONFIRM = new ButtonType("Confirm");
    public static ButtonType UPDATE = new ButtonType("Update order");

    public static ButtonType CANCEL = new ButtonType("Cancel order");


    public ConfirmationDialog(String orderDetails) {
        super(AlertType.CONFIRMATION, orderDetails, CONFIRM, UPDATE, CANCEL);
        setTitle("Order confirmation");
        setHeaderText("Check your order again");
    }

}

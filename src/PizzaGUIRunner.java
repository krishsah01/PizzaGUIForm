import javax.swing.*;

public class PizzaGUIRunner extends PizzaGUIFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(PizzaGUIFrame::new);
    }
}

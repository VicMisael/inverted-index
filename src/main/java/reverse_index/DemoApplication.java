package reverse_index;

import reverse_index.gui.CSVSearchFrame;

import javax.swing.*;

public class DemoApplication {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CSVSearchFrame frame = new CSVSearchFrame();
                frame.setVisible(true);
            }
        });
    }

}

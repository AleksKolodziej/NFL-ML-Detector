package com.example;

import javax.swing.SwingUtilities;

public class App {

    public static void main(String[] args) {
        // Invoke the GUI 
        SwingUtilities.invokeLater(() -> {
            Gui frame = new Gui();
            frame.setVisible(true);
            
        });
    }
}

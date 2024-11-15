package com.example;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Gui extends JFrame{

    //Constructor for GUI setting bounds and window title
    public Gui(){                      //initialize()

        setTitle("ML Detector");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 650);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        addGui();
    }

     //Create the method for the search image setting bounds and location in folder path
    public void addGui(){

        JTextField searchText = new JTextField();
        searchText.setBounds(15,15, 350, 45);
        searchText.setFont(new Font("Dialog", Font.PLAIN, 24 ));
        add(searchText);
        JButton searchButton = new JButton(loadImage("demo/src/pictures/search.png"));

        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(371,14, 47, 45);
        add(searchButton);

    }

    //ImageIcon in order to return the new Search image
    public ImageIcon loadImage(String resourcePath) {
       try{

        BufferedImage searchImage = ImageIO.read(new File(resourcePath));
        return new ImageIcon(searchImage);

       }catch(IOException e){
            e.printStackTrace();
       }

       System.out.println("Couldn't find correct resource");
       return null;
}
}
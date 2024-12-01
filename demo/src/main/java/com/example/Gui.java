package com.example;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Gui extends JFrame {

    private FetchML fetchML;
    private TeamMap teamMap;

    public Gui() {
        fetchML = new FetchML();
        teamMap = new TeamMap();  
        addGui();
    }

    public void addGui() {
        // GUI setup code
        getContentPane().setBackground(Color.BLACK);
        setTitle("ML Detector");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        
        
    
        // Search text box boundaries
        JTextField searchText = new JTextField();
        searchText.setBounds(40, 15, 350, 35);
        searchText.setFont(new Font("Dialog", Font.PLAIN, 24));
        add(searchText);

        // Implements the search button and retrieves the png from the resource path
        JButton searchButton = new JButton(loadImage("demo/src/pictures/search.png"));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(400, 14, 47, 40);
        add(searchButton);

        // Set the bounds for the result text box
        JTextArea mlResult = new JTextArea();
        mlResult.setBounds(50, 200, 350, 150);
        mlResult.setFont(new Font("Dialog", Font.PLAIN, 18));
        mlResult.setEditable(false);
        mlResult.setLineWrap(true);
        mlResult.setWrapStyleWord(true);
        add(mlResult);

        // Set bounds for NFL home logo
        JLabel homePic = new JLabel();
        homePic.setBounds(20,20,2000,200);
        add(homePic);

        // Set bounds for NFL away logo
        JLabel awayPic = new JLabel();
        awayPic.setBounds(300,20,2000,200);
        add(awayPic);

        //For the at text in the middle of the gui
        JLabel atLabel = new JLabel("at");
        atLabel.setForeground(Color.WHITE);
        atLabel.setFont(new Font("Dialog", Font.PLAIN, 30));
        atLabel.setBounds(220, 90, 50, 50);  
        add(atLabel);
        
        // Action listener for search button
        searchButton.addActionListener(e -> {
            String input = searchText.getText().trim().toLowerCase(); // Normalize input

            if (input.isEmpty()) {
                mlResult.setText("Please enter a valid NFL team name.");
                return;
            }

            String teamNormalizedId = teamMap.getTeamId(input); // Use normalized input for lookup
            if (teamNormalizedId == null) {
                mlResult.setText("No matching team found for the entered name.");
                return;
            }

            // Fetch the moneyline data
            Moneylines moneyline = fetchML.fetchMoneyLine(teamNormalizedId);
            if (moneyline != null) {
                String result = "               Best Away Moneyline: " + moneyline.getAwayMoneyline() +
                                "\n               Best Home Moneyline: " + moneyline.getHomeMoneyline() +
                                "\n               Best Away Affiliate: " + moneyline.getAwayAffiliate() +
                                "\n               Best Home Affiliate: " + moneyline.getHomeAffiliate();
                mlResult.setText(result);


                // Normalize and fetch the home and away teams
                String homeTeam = moneyline.getHomeTeam();
                String awayTeam = moneyline.getAwayTeam();

                // Normalize both team names 
                String normalizedHomeTeam = homeTeam != null ? homeTeam.toLowerCase().trim() : "";
                String normalizedAwayTeam = awayTeam != null ? awayTeam.toLowerCase().trim() : "";

                // Debug the fetched and normalized team names
                System.out.println("Normalized Home Team: " + normalizedHomeTeam);
                System.out.println("Normalized Away Team: " + normalizedAwayTeam);

                // Set the logos for both teams
                loadTeamLogo(homePic, normalizedHomeTeam); // Set home logo
                loadTeamLogo(awayPic, normalizedAwayTeam); // Set away logo
            } else {
                mlResult.setText("No moneyline data found for the entered team.");
            }
        });
    }

    // Loads the search button image
    public ImageIcon loadImage(String resourcePath) {
        try {
            BufferedImage searchImage = ImageIO.read(new File(resourcePath));
            return new ImageIcon(searchImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Couldn't find the correct resource: " + resourcePath);
        return null;
    }

    //Loads the team Logo in the correct spot if there is no logo detected use the basic NFL logo
    private void loadTeamLogo(JLabel teamPicLabel, String teamName) {
        if (teamName == null || teamName.isEmpty()) {
            System.out.println("Error: Team name is null or empty.");
            return;
        }

        // Normalize team name (lowercase and trimmed)
        String normalizedInput = teamName.toLowerCase().trim();

        // Get normalized name for logo files
        String normalizedTeamName = teamMap.getTeamLogoName(normalizedInput);

        if (normalizedTeamName == null) {
            System.out.println("Error: No mapping found for team name: " + teamName);
            return;
        }

        // Construct the file path for the logo
        String folderPath = "demo/src/nflLogos";
        String fileName = normalizedTeamName + ".png";
        String fullPath = folderPath + "/" + fileName;

        // Load logo or default
        File file = new File(fullPath);
        if (file.exists()) {
            ImageIcon teamLogo = new ImageIcon(new ImageIcon(fullPath).getImage()
                    .getScaledInstance(170, 130, Image.SCALE_SMOOTH));
            teamPicLabel.setIcon(teamLogo);
        } else {
            System.out.println("Image not found for team: " + teamName);
            ImageIcon defaultLogo = new ImageIcon(new ImageIcon("demo/src/nflLogos/default_logo.png").getImage()
                    .getScaledInstance(100, 100, Image.SCALE_SMOOTH));
            teamPicLabel.setIcon(defaultLogo);
        }
    }
}

package com.example;

import com.example.Gui;
import java.net.URI;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import com.google.gson.Gson;
import java.net.http.HttpRequest;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;


public class App{

    public static void main(String[] args)  {

        // The apiKey held in a string
        final String apiKey = "1AQ7jY07u8QvXd3cnhuDbYlRuVYNahKB";

        //Call the gui
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run(){
                Gui frame = new Gui();
                frame.setVisible(true);
                
            }
        });
        

        //Transcript object in order to get corresponding "sport_id"
         Transcript transcript = new Transcript();
         transcript.setSport_id("2");
         Gson gson = new Gson();
         String jsonRequest = gson.toJson(transcript);
        System.out.println(jsonRequest);
        
        //The get request to the api to retrieve the data
    try{
       HttpRequest postRequest = HttpRequest.newBuilder()
       .uri(new URI("https://api.apilayer.com/therundown/sports"))
       .header("apikey", apiKey)
       .GET()
       .build();
    
        //HttpClient object to pass thru the HttpRequest
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

       //Sout here to just present the correct information inside of the api
       System.out.println("Response body" + postResponse.body());
       
        
    } catch (Exception e){
       e.printStackTrace();
      

  }
 }
}  


   


//Tmr if i could get it to recognize the sport_name then identify the 30 teams in the nfl that would be cool
// Then send another post and get request for the ML's and then set a simple if statement within it saying
// hello this is the ML for the future game the odds are best on this platform
//import nfl logos in a folder along with sportsbook pngs
//Create a gui displaying everything not bad
// dont have to worry about scores as this will take place in the future 
// Also have to send in a post and get request for the future schedules of the teams and in the input box of the gui
//Make it recognizes noncasesensitive team String answers such as "Jets" or "jets" and "Chargers" or " chaRGErs"
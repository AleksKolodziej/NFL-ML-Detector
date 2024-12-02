
package com.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FetchML {

    private final String apiKey = "XXXXXXXXXXXXXXXXX"; 
    private TeamMap teamMap = new TeamMap();

    public Moneylines fetchMoneyLine(String teamNormalizedId) {
        System.out.println("Team Normalized ID received: " + teamNormalizedId);

        String url = "https://api.apilayer.com/therundown/sports/2/events/2024-12-01";

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("apikey", apiKey)
                    .GET()
                    .build();

            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            System.out.println("Full API Response: " + responseBody); // Log full response for debugging

            // Parse and find moneyline data
            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
            return parseMoneyLine(jsonObject, teamNormalizedId);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Method for parsing thru the API data to get the moneylines
    public Moneylines parseMoneyLine(JsonObject jsonObject, String teamId) {
        JsonArray events = jsonObject.getAsJsonArray("events");
    
        if (events == null || events.size() == 0) {
            System.out.println("No events found in the API response.");
            return null;
        }
    
        for (JsonElement eventElement : events) {
            JsonObject event = eventElement.getAsJsonObject();
            String eventId = event.get("event_id").getAsString();
            System.out.println("Event ID: " + eventId);
    
            JsonArray teams = event.getAsJsonArray("teams");
            boolean isTeamInEvent = false; // Flag to track if the input team is in this event.
    
            String homeTeamName = null;
            String awayTeamName = null;
            boolean isHomeTeam = false;
            boolean isAwayTeam = false;
    
            boolean homeFlagFound = false;
            boolean awayFlagFound = false;
    
            // Loop through the teams in the event
            for (JsonElement teamElement : teams) {
                JsonObject team = teamElement.getAsJsonObject();
                String teamName = team.get("name").getAsString();
                String normalizedId = team.get("team_normalized_id").getAsString();
                boolean isHome = team.get("is_home").getAsBoolean();
                boolean isAway = team.get("is_away").getAsBoolean();
    
                System.out.println("Team found: " + teamName + " (Normalized ID: " + normalizedId + 
                                   ", is_home: " + isHome + ", is_away: " + isAway + ")");
    
                // Explicitly check and assign home and away teams
                if (isHome) {
                    homeTeamName = teamName;
                    if (normalizedId.equals(teamId)) {
                        isHomeTeam = true;
                    }
                    homeFlagFound = true;
                }
    
                if (isAway) {
                    awayTeamName = teamName;
                    if (normalizedId.equals(teamId)) {
                        isAwayTeam = true;
                    }
                    awayFlagFound = true;
                }
    
                // If the input team matches any of the teams, mark it as part of the event
                if (normalizedId.equals(teamId)) {
                    isTeamInEvent = true;
                }
            }
    
            // Log home and away flag status
            System.out.println("Home Flag Found: " + homeFlagFound);
            System.out.println("Away Flag Found: " + awayFlagFound);
    
            // Handling edge case  If one team is flagged as home, the other should automatically be away.
            if (!homeFlagFound && awayFlagFound) {
                System.out.println("One team is marked as away, assuming the other is home.");
                homeTeamName = awayTeamName; // Swap teams since we have only the away flag
                isHomeTeam = true;
            } else if (!awayFlagFound && homeFlagFound) {
                System.out.println("One team is marked as home, assuming the other is away.");
                awayTeamName = homeTeamName; // Swap teams since we have only the home flag
                isAwayTeam = true;
            }
    
            // Log results after adjustments
            System.out.println("Home Team after adjustment: " + homeTeamName);
            System.out.println("Away Team after adjustment: " + awayTeamName);
    
            // If both teams are found, proceed to set moneyline data
            if (isTeamInEvent && homeTeamName != null && awayTeamName != null) {
                JsonObject lines = event.getAsJsonObject("lines");
                if (lines == null) {
                    System.out.println("No lines found for event: " + eventId);
                    continue;
                }
    
                // Call getBestMoneyLine to process moneylines
                Moneylines bestMoneyline = getBestMoneyLine(lines, homeTeamName, awayTeamName, isHomeTeam, isAwayTeam);
    
                // Return the final Moneylines object if it's not null
                if (bestMoneyline != null) {
                    return bestMoneyline;
                }
            }
        }
    
        System.out.println("No matching event found for the team with ID: " + teamId);
        return null;
    }
    
    


    // Method to get the best moneyline data from the JSON lines
    private Moneylines getBestMoneyLine(JsonObject lines, String homeTeamName, String awayTeamName, boolean isHomeTeam, boolean isAwayTeam) {
        System.out.println("Starting to process lines data...");
    
        String bestHomeMoneyline = null;
        String bestAwayMoneyline = null;
        String bestHomeAffiliate = null;
        String bestAwayAffiliate = null;
    
        Double bestHomeMoneylineValue = null; // For comparing negative home moneylines
        Double bestAwayMoneylineValue = null; // Initially null to track if we find valid data
    
        for (Map.Entry<String, JsonElement> entry : lines.entrySet()) {
            JsonObject line = entry.getValue().getAsJsonObject();
    
            JsonObject affiliate = line.getAsJsonObject("affiliate");
            String affiliateName = (affiliate != null && affiliate.has("affiliate_name")) ? affiliate.get("affiliate_name").getAsString() : "Unknown Affiliate";
    
            if (line.has("moneyline")) {
                JsonObject moneyline = line.getAsJsonObject("moneyline");
    
                if (moneyline.has("moneyline_away") && moneyline.has("moneyline_home")) {
                    String awayMoneyLine = moneyline.get("moneyline_away").isJsonNull() ? null : moneyline.get("moneyline_away").getAsString();
                    String homeMoneyLine = moneyline.get("moneyline_home").isJsonNull() ? null : moneyline.get("moneyline_home").getAsString();
    
                    if (awayMoneyLine != null && homeMoneyLine != null) {
                        try {
                            double awayMoneyLineValue = Double.parseDouble(awayMoneyLine);
                            double homeMoneyLineValue = Double.parseDouble(homeMoneyLine);
    
                            // Skip invalid moneylines (e.g., 0.0001)
                           if (awayMoneyLineValue == 0.0001 || homeMoneyLineValue == 0.0001 || awayMoneyLineValue == 0 || homeMoneyLineValue == 0) {
                             System.out.println("Skipping invalid moneylines: Away = " + awayMoneyLine + ", Home = " + homeMoneyLine);
                               continue;
                            }
    
                            System.out.println("Processing: Away = " + awayMoneyLine + ", Home = " + homeMoneyLine + ", Affiliate = " + affiliateName);
    
                            // Update best away moneyline (higher is better for away moneylines)
                            if (bestAwayMoneylineValue == null || awayMoneyLineValue > bestAwayMoneylineValue) {
                                bestAwayMoneylineValue = awayMoneyLineValue;
                                bestAwayMoneyline = awayMoneyLine;
                                bestAwayAffiliate = affiliateName;
                                System.out.println("Updated Best Away Moneyline: " + bestAwayMoneyline + " (Affiliate: " + bestAwayAffiliate + ")");
                            }
    
                            // Update best home moneyline (less negative is better for home moneylines)
                            if (bestHomeMoneylineValue == null || homeMoneyLineValue > bestHomeMoneylineValue) {
                                bestHomeMoneylineValue = homeMoneyLineValue;
                                bestHomeMoneyline = homeMoneyLine;
                                bestHomeAffiliate = affiliateName;
                                System.out.println("Updated Best Home Moneyline: " + bestHomeMoneyline + " (Affiliate: " + bestHomeAffiliate + ")");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Error parsing moneyline: " + awayMoneyLine + " or " + homeMoneyLine);
                        }
                    } else {
                        System.out.println("Incomplete moneyline data for affiliate: " + affiliateName);
                    }
                } else {
                    System.out.println("Moneyline data missing for affiliate: " + affiliateName);
                }
            } else {
                System.out.println("No moneyline data in line for affiliate: " + affiliateName);
            }
        }

        System.out.println("Home Team Name: " + homeTeamName);
        System.out.println("Away Team Name: " + awayTeamName);
    
        
        if (bestHomeMoneyline != null && bestAwayMoneyline != null) {
            System.out.println("Final Best Moneyline Found: Away - " + bestAwayMoneyline + " (Affiliate: " + bestAwayAffiliate + 
                               "), Home - " + bestHomeMoneyline + " (Affiliate: " + bestHomeAffiliate + ")");
    
        

            // Create the Moneylines object with the appropriate home and away teams and affiliates
            return new Moneylines(bestAwayMoneyline, bestHomeMoneyline, bestAwayAffiliate, bestHomeAffiliate, homeTeamName, awayTeamName, true ,false);
        } else if (bestAwayMoneyline == null) {
            System.out.println("No valid away moneyline data found.");
        } else if (bestHomeMoneyline == null) {
            System.out.println("No valid home moneyline data found.");
        }
    
        System.out.println("No valid moneyline data found.");
        return null;
    }
    
}    
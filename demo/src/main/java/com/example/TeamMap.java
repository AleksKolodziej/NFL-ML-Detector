package com.example;

import java.util.HashMap;

public class TeamMap {

    private HashMap<String, String> teamMap;

    public TeamMap() {
        teamMap = new HashMap<>();
        initializeTeamMap();
    }

    private void initializeTeamMap() {
        // Initialize teams to their normalized values in order to search across api
        teamMap.put("arizona cardinals", "89");
        teamMap.put("buffalo bills", "61");
        teamMap.put("atlanta falcons", "85");
        teamMap.put("baltimore ravens", "65");
        teamMap.put("carolina panthers", "86");
        teamMap.put("chicago bears", "81");
        teamMap.put("cincinnati bengals", "66");
        teamMap.put("cleveland browns", "67");
        teamMap.put("dallas cowboys", "77");
        teamMap.put("denver broncos", "73");
        teamMap.put("detroit lions", "82");
        teamMap.put("green bay packers", "83");
        teamMap.put("houston texans", "69");
        teamMap.put("indianapolis colts", "70");
        teamMap.put("las vegas raiders", "75");
        teamMap.put("kansas city chiefs", "74");
        teamMap.put("los angeles chargers", "76");
        teamMap.put("los angeles rams", "90");
        teamMap.put("miami dolphins", "62");
        teamMap.put("minnesota vikings", "84");
        teamMap.put("new england patriots", "63");
        teamMap.put("new orleans saints", "87");
        teamMap.put("new york jets", "64");
        teamMap.put("philadelphia eagles", "79");
        teamMap.put("pittsburgh steelers", "68");
        teamMap.put("san francisco 49ers", "91");
        teamMap.put("seattle seahawks", "92");
        teamMap.put("tampa bay buccaneers", "88");
        teamMap.put("tennessee titans", "72");
        teamMap.put("washington commanders", "80");
        teamMap.put("new york giants", "78");
        teamMap.put("jacksonvile jaguars", "71");
    }

    // Getter for teamMap
    public HashMap<String, String> getTeamMap() {
        return teamMap;
    }

    // Setter for teamMap
    public void setTeamMap(HashMap<String, String> teamMap) {
        this.teamMap = teamMap;
    }

    // Normalize and handle abbreviations for team names
    private String normalizeTeamName(String teamName) {
        if (teamName == null || teamName.isEmpty()) {
            return null;
        }

        // Normalize team name (lowercase, trimmed)
        String normalizedTeamName = teamName.trim().toLowerCase();

        // swtich for handling the abbrievations in the api
        switch (normalizedTeamName) {
            case "ny jets" -> normalizedTeamName = "new york jets";
            case "ny giants" -> normalizedTeamName = "new york giants";
            case "sea seahawks" -> normalizedTeamName = "seattle seahawks";
            case "sf 49ers" -> normalizedTeamName = "san francisco 49ers";
            case "jax jaguars" -> normalizedTeamName = "jacksonville jaguars";
            case "la chargers" -> normalizedTeamName = "los angeles chargers";
            case "ari cardinals" -> normalizedTeamName = "arizona cardinals";
            case "buf bills" -> normalizedTeamName = "buffalo bills";
            case "atl falcons" -> normalizedTeamName = "atlanta falcons";
            case "bal ravens" -> normalizedTeamName = "baltimore ravens";
            case "phi eagles" -> normalizedTeamName = "philadelphia eagles";
            case "car panthers" -> normalizedTeamName = "carolina panthers";
            case "chi bears" -> normalizedTeamName = "chicago bears";
            case "cin bengals" -> normalizedTeamName = "cincinatti bengals";
            case "cle browns" -> normalizedTeamName = "cleveland bears";
            case "dal cowboys" -> normalizedTeamName = "dallas cowboys";
            case "den broncos" -> normalizedTeamName = "denver broncos";
            case "det lions" -> normalizedTeamName = "detriot lions";
            case "gb packers" -> normalizedTeamName = "green bay packers";
            case "ind colts" -> normalizedTeamName = "indianapolis colts";
            case "lv raiders" -> normalizedTeamName = "las vegas raiders";
            case "kc chiefs" -> normalizedTeamName = "kansas city chiefs";
            case "la rams" -> normalizedTeamName = "los angeles rams";
            case "mia dolphins" -> normalizedTeamName = "miami dolphins";
            case "min vikings" -> normalizedTeamName = "minnesota vikings";
            case "ne patriots" -> normalizedTeamName = "new england patriots";
            case "no saints" -> normalizedTeamName = "new orleans saints";
            case "pit steelrs" -> normalizedTeamName = "pittsburgh steelers";
            case "tb buccaneers" -> normalizedTeamName = "tampa bay buccaneers";
            case "ten titants" -> normalizedTeamName = "tennesee titans";
            case "was commanders" -> normalizedTeamName = "washing commanders";
            default -> {
            }
        }


        return normalizedTeamName;
    }

    // Get Team ID
    public String getTeamId(String teamName) {
        String normalizedTeamName = normalizeTeamName(teamName);
        if (normalizedTeamName == null) {
            return null;
        }
    
        System.out.println("Normalized team name for " + teamName + ": " + normalizedTeamName);  // Log normalized name
        
        // Log the entire teamMap for debugging
        teamMap.forEach((key, value) -> System.out.println("Team in map: " + key + " -> " + value));
    
        return teamMap.get(normalizedTeamName);
    }
    
    
    

    // Get the team logo name (used for logo file lookup)
    public String getTeamLogoName(String teamName) {
        String normalizedTeamName = normalizeTeamName(teamName);
        if (normalizedTeamName == null) {
            return null;
        }

        // Return the normalized name, which corresponds to the logo file
        return normalizedTeamName.replaceAll(" ", "_");  // Optional: Replace spaces with underscores
    }
}

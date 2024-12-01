package com.example;

//Class for setters and getters for the moneylines and recognizing home and away teams
public class Moneylines {
    private String awayMoneyline;
    private String homeMoneyline;
    private String awayAffiliate;
    private String homeAffiliate;
    private String homeTeam;
    private String awayTeam;
    private boolean isAway;
    private boolean isHome;

    // Constructor for moneylines 
    public Moneylines(String awayMoneyline, String homeMoneyline, String awayAffiliate, String homeAffiliate, String homeTeam, String awayTeam, boolean isHome, boolean isAway) {
        this.awayMoneyline = awayMoneyline;
        this.homeMoneyline = homeMoneyline;
        this.awayAffiliate = awayAffiliate;
        this.homeAffiliate = homeAffiliate;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.isHome = isHome;  
        this.isAway = isAway;  
    }

    //Getter for Away ML
    public String getAwayMoneyline() {
        return awayMoneyline;
    }
    //Getter for Home ML
    public String getHomeMoneyline() {
        return homeMoneyline;
    }
    //Getter for Away Affilliate
    public String getAwayAffiliate() {
        return awayAffiliate;
    }
    //Getter for Home Affilliate
    public String getHomeAffiliate() {
        return homeAffiliate;
    }
    //Getter for home Team data
    public String getHomeTeam() {
        return homeTeam;
    }
    //Setter for Away team
    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }
    //Setter for Away team
    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }
    //Getter for Away Team
    public String getAwayTeam() {
        return awayTeam;
    }
    //Boolean statement to determine whether team is home or away
    public boolean isHome() {
        return isHome;
    }
    //Setter for ^^
    public void setIsHome(boolean isHome) {
        this.isHome = isHome;
    }
    //Boolean for if the team is away
    public boolean isAway() {
        return isAway;
    }
    //Setter for if the team is away
    public void setIsAway(boolean isAway) {
        this.isAway = isAway;
    }
    //print statements
    @Override
    public String toString() {
        return "Moneylines{" +
                "homeTeam='" + homeTeam + '\'' +
                ", awayTeam='" + awayTeam + '\'' +
                ", isHome=" + isHome +
                ", isAway=" + isAway +
                ", homeMoneyline='" + homeMoneyline + '\'' +
                ", awayMoneyline='" + awayMoneyline + '\'' +
                '}';
    }
}

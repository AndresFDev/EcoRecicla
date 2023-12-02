package com.example.ecorecicla.models;

import java.util.List;
import java.util.Map;

public class Entry {
    private Map<String, List<PlasticItem>> plastic;
    private List<Category> paperList;
    private List<Category> electronicList;
    private List<Category> glassList;
    private List<Category> cardboardList;
    private Map<String, List<SteelItem>> steel;
    private List<Category> textilesList;
    private Map<String, List<BatteryItem>> battery;

    public Entry(Map<String, List<PlasticItem>> plastic, List<Category> paperList, List<Category> electronicList, List<Category> glassList, List<Category> cardboardList, Map<String, List<SteelItem>> steel, List<Category> textilesList, Map<String, List<BatteryItem>> battery) {
        this.plastic = plastic;
        this.paperList = paperList;
        this.electronicList = electronicList;
        this.glassList = glassList;
        this.cardboardList = cardboardList;
        this.steel = steel;
        this.textilesList = textilesList;
        this.battery = battery;
    }

    public Map<String, List<PlasticItem>> getPlastic() {
        return plastic;
    }

    public void setPlastic(Map<String, List<PlasticItem>> plastic) {
        this.plastic = plastic;
    }

    public List<Category> getPaperList() {
        return paperList;
    }

    public void setPaperList(List<Category> paperList) {
        this.paperList = paperList;
    }

    public List<Category> getElectronicList() {
        return electronicList;
    }

    public void setElectronicList(List<Category> electronicList) {
        this.electronicList = electronicList;
    }

    public List<Category> getGlassList() {
        return glassList;
    }

    public void setGlassList(List<Category> glassList) {
        this.glassList = glassList;
    }

    public List<Category> getCardboardList() {
        return cardboardList;
    }

    public void setCardboardList(List<Category> cardboardList) {
        this.cardboardList = cardboardList;
    }

    public Map<String, List<SteelItem>> getSteel() {
        return steel;
    }

    public void setSteel(Map<String, List<SteelItem>> steel) {
        this.steel = steel;
    }

    public List<Category> getTextilesList() {
        return textilesList;
    }

    public void setTextilesList(List<Category> textilesList) {
        this.textilesList = textilesList;
    }

    public Map<String, List<BatteryItem>> getBattery() {
        return battery;
    }

    public void setBattery(Map<String, List<BatteryItem>> battery) {
        this.battery = battery;
    }
}

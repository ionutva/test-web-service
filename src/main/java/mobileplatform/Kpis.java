package mobileplatform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

//Kpis Singleton Class stores data for /kpis endpoint
public class Kpis {

    
    private int proccessedJSONFiles;
    private int totalNoRows;
    private int totalNoCalls;
    private int totalNoMessages;
    private int totalOriginCountries;
    private int totalDestinationCountries;
    private HashMap<String,Long> jsonProcessTime;

    @JsonIgnore
    HashSet<String> filesProcessed;
    @JsonIgnore
    HashSet<Long> totalOriginCountriesSet;
    @JsonIgnore
    HashSet<Long> totalDestinationCountriesSet;
    
    private Kpis(int proccessedJSONFiles, int totalNoRows, int totalNoCalls, int totalNoMessages, int totalOriginCountries, int totalDestinationCountries, 
            HashMap<String, Long> jsonProcessTime, HashSet<String> filesProcessed, HashSet<Long> totalOriginCountriesSet, HashSet<Long> totalDestinationCountriesSet) {
        this.proccessedJSONFiles = proccessedJSONFiles;
        this.totalNoRows = totalNoRows;
        this.totalNoCalls = totalNoCalls;
        this.totalNoMessages = totalNoMessages;
        this.totalOriginCountries = totalOriginCountries;
        this.totalDestinationCountries = totalDestinationCountries;
        this.jsonProcessTime = jsonProcessTime;
        this.filesProcessed = filesProcessed;
        this.totalOriginCountriesSet = totalOriginCountriesSet;
        this.totalDestinationCountriesSet = totalDestinationCountriesSet;
    }
    
    private static Kpis kpis = null;
    
    public static Kpis getKpis(){
        synchronized(Kpis.class){
            if(kpis == null){
                kpis = new Kpis(0, 0, 0, 0, 0, 0, new HashMap<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
            }
            return kpis;
        }
    }

    public int getProccessedJSONFiles() {
        return proccessedJSONFiles;
    }

    public void setProccessedJSONFiles(int proccessedJSONFiles) {
        this.proccessedJSONFiles = proccessedJSONFiles;
    }

    public int getTotalNoRows() {
        return totalNoRows;
    }

    public void setTotalNoRows(int totalNoRows) {
        this.totalNoRows = totalNoRows;
    }

    public int getTotalNoCalls() {
        return totalNoCalls;
    }

    public void setTotalNoCalls(int totalNoCalls) {
        this.totalNoCalls = totalNoCalls;
    }

    public int getTotalNoMessages() {
        return totalNoMessages;
    }

    public void setTotalNoMessages(int totalNoMessages) {
        this.totalNoMessages = totalNoMessages;
    }

    public int getTotalOriginCountries() {
        return totalOriginCountries;
    }

    public void setTotalOriginCountries(int totalOriginCountries) {
        this.totalOriginCountries = totalOriginCountries;
    }

    public int getTotalDestinationCountries() {
        return totalDestinationCountries;
    }

    public void setTotalDestinationCountries(int totalDestinationCountries) {
        this.totalDestinationCountries = totalDestinationCountries;
    }

    public HashMap<String, Long> getJsonProcessTime() {
        return jsonProcessTime;
    }

    public void setJsonProcessTime(HashMap<String, Long> jsonProcessTime) {
        this.jsonProcessTime = jsonProcessTime;
    }

    public HashSet<String> getFilesProcessed() {
        return filesProcessed;
    }

    public void setFilesProcessed(HashSet<String> filesProcessed) {
        this.filesProcessed = filesProcessed;
    }

    public HashSet<Long> getTotalOriginCountriesSet() {
        return totalOriginCountriesSet;
    }

    public void setTotalOriginCountriesSet(HashSet<Long> totalOriginCountriesSet) {
        this.totalOriginCountriesSet = totalOriginCountriesSet;
    }

    public HashSet<Long> getTotalDestinationCountriesSet() {
        return totalDestinationCountriesSet;
    }

    public void setTotalDestinationCountriesSet(HashSet<Long> totalDestinationCountriesSet) {
        this.totalDestinationCountriesSet = totalDestinationCountriesSet;
    }

   

    
    
}

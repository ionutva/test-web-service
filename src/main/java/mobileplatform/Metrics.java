package mobileplatform;

//Metrics Class stores data for /{date}/metrics endpoint

import java.util.HashMap;

public class Metrics {

    private int rowsMissingField;
    private int messagesBlankContent;
    private int rowsFieldsError;
    private HashMap<Long, Long> originDestinationByCountry;
    private String  okKoRelation;
    private HashMap<Long, Long> averageCallDurationByCountry;
    private HashMap<String, Long> wordOccurenceRanking;

    public Metrics(){
        this.rowsMissingField = 0;
        this.messagesBlankContent = 0;
        this.rowsFieldsError = 0;
        this.originDestinationByCountry = new HashMap<>();
        this.okKoRelation = "0/0";
        this.averageCallDurationByCountry = new HashMap<>();
        this.wordOccurenceRanking = new HashMap<>();
    }

    public int getRowsMissingField() {
        return rowsMissingField;
    }

    public void setRowsMissingField(int rowsMissingField) {
        this.rowsMissingField = rowsMissingField;
    }

    public int getMessagesBlankContent() {
        return messagesBlankContent;
    }

    public void setMessagesBlankContent(int messagesBlankContent) {
        this.messagesBlankContent = messagesBlankContent;
    }

    public int getRowsFieldsError() {
        return rowsFieldsError;
    }

    public void setRowsFieldsError(int rowsFieldsError) {
        this.rowsFieldsError = rowsFieldsError;
    }

    public HashMap getOriginDestinationByCountry() {
        return originDestinationByCountry;
    }

    public void setOriginDestinationByCountry(HashMap originDestinationByCountry) {
        this.originDestinationByCountry = originDestinationByCountry;
    }

    public String getOkKoRelation() {
        return okKoRelation;
    }

    public void setOkKoRelation(String okKoRelation) {
        this.okKoRelation = okKoRelation;
    }

    public HashMap getAverageCallDurationByCountry() {
        return averageCallDurationByCountry;
    }

    public void setAverageCallDurationByCountry(HashMap averageCallDurationByCountry) {
        this.averageCallDurationByCountry = averageCallDurationByCountry;
    }

    public HashMap<String, Long> getWordOccurenceRanking() {
        return wordOccurenceRanking;
    }

    public void setWordOccurenceRanking(HashMap<String, Long> wordOccurenceRanking) {
        this.wordOccurenceRanking = wordOccurenceRanking;
    }

   


}

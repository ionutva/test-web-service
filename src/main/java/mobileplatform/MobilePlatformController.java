package mobileplatform;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.OptionalDouble;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam; 
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobilePlatformController {

    String[] rowFieldsCallArray = {"message_type", "timestamp", "origin", "destination", "duration", "status_code", "status_description"};
    String[] rowFieldsMsgArray =  {"message_type", "timestamp", "origin", "destination", "message_content", "message_status"};


    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return "Hello, " + name + "!";
        
    }
   
    @RequestMapping("/{date}")
    public String data(@PathVariable String date) {
        StringBuilder content = new StringBuilder();
        try{
            File file = new ClassPathResource(date + ".json").getFile();
            BufferedReader br = new BufferedReader(new FileReader(file)); 
                String st;
                while ((st = br.readLine()) != null) 
                    content.append(st); 
        }catch(IOException e){
           e.printStackTrace();
        }
        
        return content.toString();
    }

    
    @RequestMapping("/{date}/metrics")
    public Metrics metrics(@RequestParam(value="words", defaultValue="") String words, @PathVariable String date) throws JSONException {
        long startTime = System.currentTimeMillis();
        long okCalls = 0;
        long koCalls = 0;
        String[] searchWords = words.replaceAll(" ", "").split(",");
        
        HashMap<Long, ArrayList<Long>> durations = new HashMap<>();
        Metrics metrics = new Metrics();
        StringBuilder content = new StringBuilder();
        try{
            File file = new ClassPathResource(date + ".json").getFile();
            BufferedReader br = new BufferedReader(new FileReader(file)); 
                String st;
                while ((st = br.readLine()) != null) 
                    content.append(st); 
        }catch(IOException e){
           e.printStackTrace();
        }
        JSONObject jsonObj = new JSONObject(content.toString());
        
        //CALL section
        JSONArray callsArray = jsonObj.getJSONArray("CALL");
        
        for(int i = 0; i < callsArray.length(); i++){
            Kpis.getKpis().setTotalNoRows(Kpis.getKpis().getTotalNoRows() + 1);
            Kpis.getKpis().setTotalNoCalls(Kpis.getKpis().getTotalNoCalls() + 1);
            JSONObject row = (JSONObject)callsArray.get(i);
            boolean containsMissingFields = false;
            boolean containsFieldsErrors = false;
            boolean wrongOrder = false;
            int arrayIndex = 0;
            for(String key: rowFieldsCallArray){
                if(!row.has(key)){
                    containsMissingFields = true;
                    continue;
                }
                
                try{
                    if(!key.equals(row.names().get(arrayIndex++))){
                         wrongOrder = true;
                    }
                }
                catch(Exception e){
                    wrongOrder = true;
                }

                //check if these fields are numbers
                if(key.equals("timestamp") || key.equals("origin") || key.equals("destination") || key.equals("duration")){
                    try{
                        row.getLong(key);
                    }catch(Exception e){
                        containsFieldsErrors = true;
                    }
                    //}
                }
                
                
            }
            
            if(containsFieldsErrors || wrongOrder){
                
                //increment number of rows with fields errors
                metrics.setRowsFieldsError(metrics.getRowsFieldsError() + 1);
                continue;
            }
            
            if(containsMissingFields){
                
                //increment number of rows with missing field
                metrics.setRowsMissingField(metrics.getRowsMissingField() + 1);
            }
            
            
            if(row.has("origin")){
                
                //increment number of calls with a certain origin
                Long originCountry = row.getLong("origin");
                metrics.getOriginDestinationByCountry().put(originCountry, (metrics.getOriginDestinationByCountry().get(originCountry) == null)?1:(Integer)metrics.getOriginDestinationByCountry().get(originCountry) + 1);
                
                //update Kpis country code size   
                Kpis.getKpis().getTotalOriginCountriesSet().add(originCountry);
                Kpis.getKpis().setTotalOriginCountries(Kpis.getKpis().getTotalOriginCountriesSet().size());
            }
            
            if(row.has("destination")){
                
                //increment number of calls with a certain destination
                Long destinationCountry = row.getLong("destination");
                metrics.getOriginDestinationByCountry().put(destinationCountry, (metrics.getOriginDestinationByCountry().get(destinationCountry) == null)?1:(Integer)metrics.getOriginDestinationByCountry().get(destinationCountry) + 1);
                
                //update Kpis country code size
                Kpis.getKpis().getTotalDestinationCountriesSet().add(destinationCountry);
                Kpis.getKpis().setTotalDestinationCountries(Kpis.getKpis().getTotalDestinationCountriesSet().size());

            }
            
            //calculate raport between OK and KO
            if(row.has("status_code")){
                if(row.getString("status_code").equals("OK")){
                    metrics.setOkKoRelation((++okCalls) + "/" + koCalls);
                }else if(row.getString("status_code").equals("KO")){
                    metrics.setOkKoRelation(okCalls + "/" + (++koCalls));
                }
            }
            
            if(row.has("duration")){
                if(durations.get(row.getLong("origin")) == null){
                    durations.put(row.getLong("origin"), new ArrayList<Long>());
                }
                durations.get(row.getLong("origin")).add(row.getLong("duration"));
                OptionalDouble average = durations.get(row.getLong("origin")).stream().mapToDouble(a -> a).average();
                metrics.getAverageCallDurationByCountry().put(row.getLong("origin"), average.isPresent() ? average.getAsDouble() : 0);
            }
            
        }
        
        //MSG section
        JSONArray msgArray = jsonObj.getJSONArray("MSG");
        for(int i = 0; i < msgArray.length(); i++){
            Kpis.getKpis().setTotalNoRows(Kpis.getKpis().getTotalNoRows() + 1);
            Kpis.getKpis().setTotalNoMessages(Kpis.getKpis().getTotalNoMessages() + 1);
            JSONObject row = (JSONObject)msgArray.get(i);
            boolean containsMissingFields = false;
            boolean containsFieldsErrors = false;
            boolean wrongOrder = false;
            int arrayIndex = 0;
            for(String key: rowFieldsMsgArray){
                if(!row.has(key)){
                    containsMissingFields = true;
                    continue;
                }

                try{
                    if(!key.equals(row.names().get(arrayIndex++))){
                         wrongOrder = true;
                    }
                }
                catch(Exception e){
                    wrongOrder = true;
                }
                
                
                if(key.equals("timestamp") || key.equals("origin") || key.equals("destination")){
                    try{
                        row.getLong(key);
                    }catch(Exception e){
                        containsFieldsErrors = true;
                    }
                }
                
            }
            
            if(containsFieldsErrors || wrongOrder){
                //increment number of rows with fields errors
                metrics.setRowsFieldsError(metrics.getRowsFieldsError() + 1);
                continue;
            }
            if(containsMissingFields){
                //increment number of rows with missing field
                metrics.setRowsMissingField(metrics.getRowsMissingField() + 1);
            }
            
            if(row.getString("message_content").equals("")){
                //increement number of messages with blank content
                metrics.setMessagesBlankContent(metrics.getMessagesBlankContent() + 1);
            }
            
            if(row.has("message_content")){
                String currentMessage = row.getString("message_content").replaceAll("\\?", "").replaceAll("\\.", "").replaceAll("!", "");
                String[] messageWords = currentMessage.split(" ");
                for(String search:searchWords){
                    if(metrics.getWordOccurenceRanking().get(search) == null){
                        metrics.getWordOccurenceRanking().put(search, 0L);
                    }
                    for(String message: messageWords){
                        if(search.toLowerCase().equals(message.toLowerCase())){
                            metrics.getWordOccurenceRanking().put(search, metrics.getWordOccurenceRanking().get(search) + 1);
                        }
                    }
                }
            }
            
            if(row.has("origin")){
               //update Kpis country code size   
                Kpis.getKpis().getTotalOriginCountriesSet().add(row.getLong("origin"));
                Kpis.getKpis().setTotalOriginCountries(Kpis.getKpis().getTotalOriginCountriesSet().size());   
            }
            
            if(row.has("destination")){
               //update Kpis country code size   
                Kpis.getKpis().getTotalDestinationCountriesSet().add(row.getLong("destination"));
                Kpis.getKpis().setTotalDestinationCountries(Kpis.getKpis().getTotalDestinationCountriesSet().size());   
            }

            
        }
        
        Kpis.getKpis().getFilesProcessed().add(date);
        Kpis.getKpis().setProccessedJSONFiles(Kpis.getKpis().getFilesProcessed().size());
        
        long endTime = System.currentTimeMillis();
        Kpis.getKpis().getJsonProcessTime().put(date, endTime - startTime);
        
        return metrics;
    }

    
    @RequestMapping("/kpis")
    public Kpis kpis() {
        return Kpis.getKpis();
    }
    
}

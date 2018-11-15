This project is developed in Netbeans.

You will find in src/java/resources 3 json files for test.

Endpoints: 

/greeting
For testing

/{date}
Get the json for that date

/{date}/metrics
Calculates the metrics for the json with that date in name
Ex: http://localhost:8080/2018-11-12/metrics, http://localhost:8080/2018-11-13/metrics, http://localhost:8080/2018-11-14/metrics

/kpis
Returns the kpis

For testing word occurence call:
http://localhost:8080/2018-11-12/metrics?words=ARE,YOU,FINE,HELLO,NOT

Fields with invalid data are not taking in coount.

Average call duration by country is calculated for the same origin country.

Time for JSON proces is in miliseconds and is calculated just for the first access for an applicatio session.


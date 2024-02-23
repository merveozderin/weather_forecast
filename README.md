# Weather Forecast 

The project provides maximum, feels like temperatures, humidity information for the next 48 hours with the data received 
from OpenWeather APIs free (https://openweathermap.org/api) according to given city parameter.

#Getting Started

In any Java IDE, you can develop and run this code. It is a Java 17, Spring Boot 3.2.3 and Maven project.

#Dependencies

Spring Boot DevTools
Spring Boot Web
Spring WebFlux
SpringFox Boot
Springfox Swagger2
SpringFox Swagger UI

All dependecies have the latest version.

You can also find more information in the pom.xml file.

#Setup

- git clone https://github.com/merveozderin/weather_forecast.git
- set the dependencies
- mvn clean
- mvn install
- run project

To test the code:
- run test app

#Utilization

After the project run, you can access the link, http://localhost:8080/forecast?cityId=524901. cityId is a parameter in order to access OpenWeather APIs.
In the website, you can also find more information about different cityId parameters.

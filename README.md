## Weather-Hub
An Android Application which reads weather information of user given city around the world.
#### We took the API key from https://openweathermap.org/
#### API Call sample: api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}
* In the field {city name} you have to write down the city id. You will get the city ids from http://bulk.openweathermap.org/sample/
* Download the file - city.list.json.gz
* In the field {API key} you have to write down your own API key which will be given automatically by creating an account in "Open Weather Map" website (link is given before).
- - - -
#### Internet-permission in AndroidManifest.xml -
* uses-permission android:name="android.permission.INTERNET"<br><br>
#### Design library dependencies -
* implementation 'com.google.android.material:material:1.2.1'<br><br>
#### Retrofit 2 library dependencies -
* implementation 'com.squareup.retrofit2:retrofit:2.9.0'
* implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
* implementation 'com.google.code.gson:gson:2.8.6'<br><br>

#### Outputs:

* Weather data of Dhaka city - 
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;    ![temp](https://user-images.githubusercontent.com/37416018/148210316-d0e0be4e-70b9-4058-9cf1-968cc13879ce.jpg)
- - - -
<br>

* Weather forecast of 5 days 3 hours interval - 

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;    ![forecast](https://user-images.githubusercontent.com/37416018/148210323-3875f11e-73c8-48da-8dc7-1dada13d168f.jpg)
- - - -
<br>

* Built With - 
    * Android Studio
    * Java
    * XML
    * REST API
    * JSON
- - - -

* License -
    * This project is licensed under the MIT License.

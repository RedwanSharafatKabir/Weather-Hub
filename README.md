## Weather-App_using_REST-API_and_Retrofit2
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

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;    ![1](https://user-images.githubusercontent.com/37416018/105637984-7f617500-5e9a-11eb-8749-eae2b7f00f14.JPG)
- - - -
<br>

* Weather data of Dhaka and London city - 

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;    ![2](https://user-images.githubusercontent.com/37416018/105637987-81c3cf00-5e9a-11eb-9e1b-951c9fa30d34.JPG)
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;    ![3](https://user-images.githubusercontent.com/37416018/105637990-838d9280-5e9a-11eb-9673-b40b40e7161a.JPG)
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

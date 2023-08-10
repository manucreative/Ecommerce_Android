# E-COMMERCE ANDROID PROJECT

#purpose for the app
   - This project is a fully running-Android application created mainly for local food industries, for example, hotels and cafes, etc.
   - it is mainly used to make a food order for earlier processing.
   - Sweatble for advanced food order-based hotels and takeaway services.
    
# Project Features and Overview
- It is a JAVA-based system and can only run on Android devices like mobile smartphones, tablets, etc.
- Build with Android studio
- Connected to MySql database for backend with PHP APIs.
- Can be controlled by admins through the admin panel
- build for Android version 9 and above
- The testing process is being done with the actual life device.

# App installations and documentations

## Configuring ON local server
  - **XAMPP** or **WAMPP** can be used in this development face.
  - Configure your local server to accept and access the global IP connection.
  - Use the USB tethering or Hot-sport for your connection and obtain the relevant connected IP.
 
# BACKEND AND ANDROID APP PROCESS CONNECTION
   - Use Retrofit and RxJava in Android studio to initiate the connection by implementing the following in **build.gradle** file.
     
                implementation 'com.squareup.retrofit2:retrofit:2.9.0'
                implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
                implementation 'io.reactivex.rxjava2:rxjava:2.2.21'
                implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
     
   - Create the Retrofit instance and pass your back-end URL with the relevant connected IP.
     ### You can implement the retrofit as follows

         Retrofit retrofit = new Retrofit.Builder()
          .baseUrl(BASE_URL) // Your API base URL
          .addConverterFactory(GsonConverterFactory.create())
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .build();
    ApiService apiService = retrofit.create(ApiService.class);

   - Add Necessary internet permission to manifest.xml file like bellow
           uses-permission android:name="android.permission.INTERNET"

     

    

# DEMO OAUTH JAVA

An example of using OAuth2 authentication protocol with Java and Android. This application uses 
Retrofit library, which is a REST client for Android and Java.

## Before starting

It is recommended to read:

- [An introduction to OAuth2](https://www.digitalocean.com/community/tutorials/an-introduction-to-oauth-2)
- [Retrofit tutorial](https://futurestud.io/tutorials/retrofit-getting-started-and-android-client), 
  specially Retrofit, Requests and Authentication sections.

## Requirements to develop and/or running the app

- Android Studio
- Retrofit 2.1.0
- An Android device with Android 4.1 (API 16) or higher
- Also, you have to register a private application at FIB API. You can do that
 [here](http://api.fib.upc.edu/v2/o/applications/register_private/). IMPORTANT! The value of
  Redirect URIs must follow the schema `apifib://`, for example `apifib://raco`.

Once you have satisfied this requirements, open the Android project and go to
`/app/src/main/java/com.inlab.racodemoapi/Constants/OAuthParamsTemplate.java` and copy its content to
a new file called `OAuthParams` at the same package. Go to this new file and replace the `clientID` and
`clientSecret` variables with your own corresponding application's values. 
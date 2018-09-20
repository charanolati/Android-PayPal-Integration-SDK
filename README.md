# Android-PayPal-Integration-SDK
PayPal Payment Gateway Android SDK Integration 


Merchant Integration Guide
Handling Custom Chrome Tab (Android) for
PayPal Express Checkout (REST API)
Version – 2.0

### Step 1

Add Chrome Custom Tab Dependencies
In your android app open build.gradle and in the dependencies section add the chrome custom tab dependencies compile 
```
    'com.android.support:customtabs:25.2.0'
```
Recompile/ Re-run the project in order for android studio to download the dependencies

***Note: The version of the custom tab should satisfy your build version.***

- Send Line item details to Merchant Server to initiate the create payment API call. Line item object sent from mobile app to server, server should transform the product object into a Payload object, which is used to make create payment API call.

- From server initiate your create payment API call with your product object as described in [PayPal create Payment API Document](https://developer.paypal.com/docs/api/payments/#payment)

- PayPal will return with a response with Pay ID and other information needed for the subsequent calls.

- Create Payment API call if successful will contain PAY ID and Links array object, this array will contain an object with method key as REDIRECT e.g.
```
    {
    "href": "https://api.sandbox.paypal.com/v1/payments//cgi-bin/webscr?cmd=_express-checkout&token=EC-60385559L1062554J",
    "rel": "approval_url",
    "method": "REDIRECT"
    }
```
### Step 2

Once the dependencies are downloaded then use the following snippet to open any external link to open in Chrome Custom Tab
```
    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(); 
    CustomTabsIntent customTabsIntent = builder.build(); 
    customTabsIntent.intent.setData(Uri.parse(url)); 
    startActivityForResult(customTabsIntent.intent, CHROME_CUSTOM_TAB_REQUEST_CODE);
```
Replace ** URL ** with the ** Actual Url ** to be opened in ** CCT **

### Step 3

* Sending back response from server to Android activity *

1. Decide which activity should be notified for success response and error response
2. In Android Manifest declare the success / failure activity as 
```
    <category android:name="android.intent.category.DEFAULT" /> 
    <category android:name="android.intent.category.BROWSABLE" /> 
    <data android:scheme="com.example.paypalcustomtabdemo"/>
```    
3. Declare your package name in android scheme
4. In your server side PHP code you can directly call your android intent by doing an server side PHP 302 redirect.
```
    $url = 'Location:com.gurucharan.paypalcustomtabdemo:success/'.body.id.'/'.body.payer.payer_info.payer_id;
    header('$url', true, 302);
    exit;
```
This will call your android intent declared in android manifest to handle success or error notification.

## Steps to run the demo:

1. Clone the android app repository to your local machine
2. Open the project from Android Studio.
3. Connect your device via usb, Run – Run App will deploy the app to your android device
4. Click Proceed by entering amount  
5. You will be redirected to payment page using CCT 
6. Proceed with Transaction 
7. Once Payment is Successfull you will be Automatically redirected to Final Activity


# Android-RaspberryPi--LightSwitch
Android app that connect through USB to raspberry pi pico and turn on the on board LED light by a click of a button

Intro
During this assignment I developed a native android app written in Kotlin and a C++ for the Raspberry pi pico,
The application needs to send data through USB to control the raspberry pi pico and turn its built-in light ON/OFF. 
In this application the android device is using host mode and the Raspberry is the accessory 
 

I chose to create a native application because it is more compatible with the mobile operating system that it's developed for, it offers a faster and more reliable responsive experience to the user than hybrid applications, and it can better utilize the built-in capabilities of the smartphone.
Native application for android is written in Java or Kotlin,
I chose to write my application in Kotlin because it has several benefits over java, one of them is that it is more concise which result in in easier to read and maintainable code.







Application Flow Diagram


![image](https://user-images.githubusercontent.com/62388861/210338128-c7c171a8-0142-4914-ad52-4d9ebc009e60.png)





















Problems I encountered and their solutions:
 
 1.	Problem: Raspberry pi pico wouldn’t go in bootsel mode
    
    Steps to solution: 
  
  •	I tried checking if it is working by connecting a LED diod to the 3.3v and GND legs on the pico.
  
  •	Forced boot by shorting RUN and GND legs.
  
  Solution:
  
  •	Replaced the USB cable.



  2.	Problem: App ran in android studio but crashed on tablet.
  
  Steps to solution: 
  
  •	Debugging- while I was debugging, I noticed a couple of things:
  
  o	Found out that the line that caused it was when I created a new Pending Intent.
  
  o	I got an error on the code that belongs to the language itself that the bit code doesn’t match the origin code (I got this when I debugged the language code and not mine).
  
  o	I got a suggestion (not an error or a warning just to clarify) from android studio that I'm using API level 21 and there's a level 31 API available.
  Solution:
  
  •	Updated to a level 31 API and it solved the problem



  3.	Problem: when using the pendingIntent.getBroadcast the application crashed
  
  Steps to solution: 
  
  •	Debugging.
  
  •	Searched different online sources for what can cause this.
  
  Solution:
  
  •	Found out through my search that in API level 31 when calling the pendingIntent.getBroadcast it is required to pass one of these flags to it- FLAG-MUTABLE / FLAG-IMUTABLE.



  4.	Problem: when checking if extra permissions  were granted it always returned null
  
  Steps to solution: 
  
  •	Debugging.
  
  •	Searched different online sources for what can cause this.
  
  Solution:
  
  •	Found out that there is a problem with FLAG-IMUTABLE, and I should use FLAG-MUTABLE.
  
  5.	Problem: Can't send data through USB
  
  Steps to solution: 
  
  •	Debugging.
  
  •	Searched different online sources for what can cause this.
  
  •	Made a python script that runs on PC to check that the problem hadn’t originated in the Raspberry.
  
  •	Tried using external libraries (3) for sending data to serial.

  Final Findings:
    •	The USB connector is disturbing the serial communication, there for this can't be done with the equipment that I had on hand.
  I found this out from knowledge I gained while searching the web for a solution and by connecting the Raspberry to the computer with and without the connector and seeing the result on it with the Python script.




Technologies Appendix
 
 •	Android Studio
 
 •	Arduino IDE
 
 •	Thonny
 
 •	PyCharm




References
 
 https://www.youtube.com/watch?v=onBkPkaqDnk
 
 https://www.youtube.com/watch?v=EOfCEhWq8sg
 
 https://developer.android.com/guide/topics/connectivity/usb/host#kotlin
 
 https://www.geeksforgeeks.org/12-best-practices-for-android-development/
 
 https://www.youtube.com/watch?v=DG7OYSTxPUA&t=630s
 
 https://chat.openai.com/chat


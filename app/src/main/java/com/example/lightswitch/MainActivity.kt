package com.example.lightswitch

import android.content.IntentFilter
import android.hardware.usb.*
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.hoho.android.usbserial.driver.*


class MainActivity : AppCompatActivity() {

    private val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"
    lateinit var usbComm: UsbCommunication

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //set the main view

        val onOffSwitch = findViewById<Button>(R.id.onOffSwitch) // Get button object
        val text = findViewById<TextView>(R.id.textView) // Get text object
        var deviceLightState = 0

        var filter = IntentFilter() //filter to set which broadcast we want to receive
        filter.addAction(ACTION_USB_PERMISSION)
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED)
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        usbComm = UsbCommunication(this)
        registerReceiver(
            usbComm,
            filter
        ) // register the UsbCommunication instance to receive the broadcast we selected

        onOffSwitch.setOnClickListener { //set button functionality
            if (usbComm.m_device != null) {
                deviceLightState = 1 - deviceLightState // if was on change to off and vise versa
                usbComm.SendDataAsyncTask().execute(deviceLightState.toString())
                text.text = String.format(usbComm.m_device?.productName.toString())
            } else {
                text.text = String.format("First connect a device")
            }
        }
    }
}
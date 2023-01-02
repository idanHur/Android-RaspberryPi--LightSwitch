package com.example.lightswitch

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.*
import android.os.AsyncTask
import android.util.Log


class UsbCommunication(context: Context) : BroadcastReceiver() {
    private val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"
    var m_device: UsbDevice? = null
    var manager: UsbManager
    var m_connection: UsbDeviceConnection? = null
    lateinit var intf: UsbInterface
    lateinit var endpoint: UsbEndpoint
    var context: Context
    ///////////////////////////////////////////////////////////
    /////////// attempt to use UsbSerial library
//    lateinit var driver: UsbSerialDriver
//    lateinit var port: UsbSerialPort

    init {
        this.context = context
        manager = context.getSystemService(Context.USB_SERVICE) as UsbManager
    }

    private fun startUsbConnection() {
        val deviceList: HashMap<String, UsbDevice> = manager.deviceList
        if (deviceList.isNotEmpty()) {
            var keep = false
            deviceList.values.forEach { device -> // check all the devices that are currently connected to the android device usb
                val deviceVendorId: Int? = device.vendorId
                Log.i("serial", "vendorId: $deviceVendorId")
                if (deviceVendorId == 11914) { //Check that the device Vendor Id matches the raspberry
                    m_device = device
                    val intent: PendingIntent = PendingIntent.getBroadcast(
                        context,
                        0,
                        Intent(ACTION_USB_PERMISSION),
                        PendingIntent.FLAG_MUTABLE
                    ) // PendingIntent represents an action to be taken in the future
                    manager.requestPermission(m_device, intent)
                    keep = true
                    Log.i("serial", "Connection successful to vendorId: $deviceVendorId")

                    ///////////////////////////
                    /////////// attempt to use UsbSerial library
                    // Add the raspberry to the Probe Table so the library will support it
//                    val customTable = ProbeTable()
//                    customTable.addProduct(deviceVendorId, device!!.productId, CdcAcmSerialDriver::class.java)
//                    val prober = UsbSerialProber(customTable)
//                    driver = prober.probeDevice(m_device)
                    ///////////////////////////////
                } else {
                    if (!keep) {//we didnt see the our device and clearing the m_device m_connection in case he disconnected
                        m_device = null
                        m_connection = null
                        Log.i("serial", "unable to connect to $deviceVendorId")
                    }
                }
            }
        } else {
            Log.i("serial", "No usb device connected")
        }
    }

    inner class SendDataAsyncTask : AsyncTask<String, Void, Void>() {
        override fun doInBackground(vararg strings: String): Void? {
            val inputAsBytes = strings[0].toByteArray()
            Log.i("serial", "Trying to send: ${strings[0]} byte size: ${inputAsBytes.size}")
            val result =
                m_connection!!.bulkTransfer(endpoint, inputAsBytes, inputAsBytes.size, 1000)
            if (result < 0) {
                Log.i("serial", "An error occurred while sending the data")
            } else {
                Log.i("serial", "The transfer was successful")
            }
            return null
        }
        /////////// attempt to use UsbSerial library
//            port.write(inputAsBytes, 2000);

//        attempt to use controlTransfer supposedly should be better at supporting serial connections
        //            val requestType = 0x40 // Standard request, type: vendor
//            val request = 0x03 // Request: set line encoding
//            val value = 0x0001 // Value: 0
//            val buffer = ByteArray(7) // Buffer for response data
//            buffer[0] = 8 // Data bits: 8
//            buffer[1] = 0 // Stop bits: 1
//            buffer[2] = 0 // Parity: none
//            buffer[3] = 0 // Break: off
//            buffer[4] = 0x25 // MSB of baud rate (9600 = 0x2580)
//            buffer[5] = 0x80.toByte() // LSB of baud rate (9600 = 0x2580)
//            buffer[6] = 0 // Reserved: 0
//            val result = m_connection!!.controlTransfer(requestType, request, value, 0, inputAsBytes, inputAsBytes.size, 600)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        if (ACTION_USB_PERMISSION == action) {
            if (intent.getBooleanExtra(
                    UsbManager.EXTRA_PERMISSION_GRANTED,
                    false
                )
            ) { //returns false if no value with the give name
                // Permission granted, setting connection to the device
                Log.i("serial", "Granted permission to access device")
                m_connection = manager.openDevice(m_device)
                intf = m_device?.getInterface(1)!!
                endpoint = intf.getEndpoint(1)
                if (m_connection!!.claimInterface(intf, true)) {
                    Log.i("serial", "The interface was claimed successfully")
                } else {
                    Log.i("serial", "The interface could not be claimed")
                }
                ///////////////////////////////////////////////////////////////////////////
                /////////// attempt to use UsbSerial library
//                    port = driver.ports[0]
//                    port.open(m_connection)
//                    port.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE)
//                    val end = port.writeEndpoint
//                    val test =
                //////////////////////////////////////////////////////////////////////////
            } else {
                // Permission denied, you cannot access the device
                Log.i("serial", "Denied permission to access device")
            }
        } else if (action == UsbManager.ACTION_USB_DEVICE_ATTACHED) {
            startUsbConnection()
        }
    }
}
package com.example.exo2

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import android.provider.ContactsContract
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
//import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T

import android.app.Activity
import android.content.pm.PackageManager
//import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import java.util.jar.Manifest


class MainActivity : AppCompatActivity() {

    private val channelId = "MyChannel"
    private val channelName = "Shona"
    private val importance = NotificationManager.IMPORTANCE_HIGH
    private val REQUEST_CODE = 99

    val CUSTOM_INTENT = "dz.esi.demobroadcast"
    //private val appPermission = arrayOf(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_MMS)
    var liste_contact = ArrayList<Contact>()


    override fun onPause() {
        super.onPause()
        FileContacts.writeToFile(this,liste_contact)
    }

    override fun onStop() {
        super.onStop()
        FileContacts.writeToFile(this,liste_contact)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val i = Intent()
        i.action = CUSTOM_INTENT
        i.setClass(this, ReceiveSMS::class.java)
        this.sendBroadcast(i)


        liste_contact = FileContacts.readFile(this)!!
        
        var contactadapter = ListAdapter(liste_contact,this)
        allcontacts.layoutManager = LinearLayoutManager(this)
        allcontacts.adapter = contactadapter
        allcontacts.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        btnadd.setOnClickListener{

            var intent = Intent(this, AddContact::class.java)
            startActivity(intent)

        }



        btnCreateNotif.setOnClickListener { v -> CreateNotifClick(v) }
        settings.setOnClickListener { v -> getSettingsActivity(v) }

        btncontacts.setOnClickListener { v->getContactlist(v) }
    }



    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.M)
    fun CreateNotifClick(v: View) {
        // notification principale


        val testIntent = Intent(this, TestActivity::class.java)
        val pTestIntent = PendingIntent.getActivity(this, System.currentTimeMillis().toInt(), testIntent, 0)
        val notifIntent1 = Intent(this, NotifActivity::class.java)
        notifIntent1.putExtra("data", "Zoom")
        val pNotifIntent1 = PendingIntent.getActivity(this, System.currentTimeMillis().toInt(), notifIntent1, 0)


        val icon1 = Icon.createWithResource(this, android.R.drawable.btn_minus)

        val action1 = Notification.Action.Builder(icon1, "Zoom", pNotifIntent1).build()





        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                channelId, channelName, importance)
            notificationManager.createNotificationChannel(mChannel)
        }

        val noti = Notification.Builder(this, channelId)
            .setContentTitle("Nouvelle Notification")
            .setContentText("Je viens de recevioir une notification !")
            .setSmallIcon(android.R.drawable.btn_dialog)
            .setContentIntent(pTestIntent)
            .addAction(action1)
            .setAutoCancel(true)

            .build()

        notificationManager.notify(0, noti)
    }

    fun SendEmail(arg0: View) {
        val to = "gs_reffad@esi.dz"
        val subject = "Test mail from app"
        val message = "This mail is a test ! "


        val email = Intent(Intent.ACTION_SEND)
        email.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>(to))
        email.putExtra(Intent.EXTRA_SUBJECT, subject)
        email.putExtra(Intent.EXTRA_TEXT, message)

        //need this to prompts email client only
        email.type = "message/rfc822"


        try {
            startActivity(Intent.createChooser(email, "Send email using..."))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(this, "No email clients installed.", Toast.LENGTH_SHORT).show()
        }



    }

    fun getSettingsActivity(v : View ) {
        val intent = Intent(this@MainActivity,SettingsActivity::class.java)
        startActivity(intent)
    }

    fun getContactlist(v:View) {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE)
    }


    public override fun onActivityResult(reqCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(reqCode, resultCode, data)
        when (reqCode) {
            REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                val contactData = data!!.data
                val c = contentResolver.query(contactData!!, null, null, null, null)
                if (c!!.moveToFirst()) {
                    val contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID))
                    val hasNumber =
                        c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                    var num = ""
                    if (Integer.valueOf(hasNumber) == 1) {
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        val numbers = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                            null,
                            null
                        )
                        while (numbers!!.moveToNext()) {
                            num =
                                numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            Toast.makeText(this, "Number=", Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }
        }
    }
}

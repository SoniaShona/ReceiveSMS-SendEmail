package com.example.exo2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_contact.*
import java.io.File

class AddContact : AppCompatActivity() {

    var contactList = ArrayList<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        contactList = FileContacts.readFile(this)!!
        addbtn.setOnClickListener{

            var c = Contact(nomcontact.text.toString(),numtel.text.toString(),email.text.toString())
            contactList.add(c)
            FileContacts.writeToFile(this,contactList)
            Toast.makeText(this, "Contact ajouté", Toast.LENGTH_SHORT).show()
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)

        }
    }
}

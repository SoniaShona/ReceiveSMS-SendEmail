package com.example.exo2

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import java.io.*

object FileContacts {

    fun readFile(contect : Context) : ArrayList<Contact>? {

        var interventions: ArrayList<Contact>

            val fis: FileInputStream = contect.openFileInput("contact.txt")
            var iss = ObjectInputStream(fis)
            interventions = iss.readObject() as ArrayList<Contact>
            iss.close()
            fis.close()

        return interventions

    }


    fun writeToFile(contect : Context,liste_contact:ArrayList<Contact>){
        val fos: FileOutputStream = contect.openFileOutput("contact.txt", AppCompatActivity.MODE_PRIVATE)
        val os = ObjectOutputStream(fos)
        os.writeObject(liste_contact)
        os.close()
        fos.close()


    }
}
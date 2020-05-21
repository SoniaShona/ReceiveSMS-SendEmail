package com.example.exo2

import java.io.Serializable

class Contact(
    name : String,
    phoneNumber : String,
    email : String

): Serializable {
    var name : String = name
    var phoneNumber : String = phoneNumber
    var email : String = email
}
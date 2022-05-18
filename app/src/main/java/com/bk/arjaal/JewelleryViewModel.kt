package com.bk.arjaal

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private lateinit var auth : FirebaseAuth

class JewelleryViewModel : ViewModel() {

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    val rootRef = FirebaseDatabase.getInstance().reference

    var jewelleriesList by mutableStateOf(mainList)

    private val tempJewelleryList = mainList

    fun onchangeList(category: String) {
        Log.d("CHECKING", "inside category card click")

        if (category == "ALL") {
            jewelleriesList = mutableListOf()
            jewelleriesList = tempJewelleryList
        } else if (category == "Necklaces") {
            jewelleriesList = mutableListOf()
            jewelleriesList = necklacesList
        } else if (category == "Bangles") {
            jewelleriesList = mutableListOf()
            jewelleriesList = banglesList
        } else if (category == "Ear rings") {
            jewelleriesList = mutableListOf()
            jewelleriesList = earRingsList
        } else if (category == "Rings") {
            jewelleriesList = mutableListOf()
            jewelleriesList = ringsList
        }
    }

}

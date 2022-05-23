package com.bk.arjaal

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.database.*

private lateinit var auth : FirebaseAuth

class JewelleryViewModel : ViewModel() {

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    val rootRef = FirebaseDatabase.getInstance().reference
    val favouriteRef = rootRef.child("users").child(user?.uid.toString()).child("favourite")

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

    fun onAddFavouriteList(jewellery: Jewellery)
    {
if(!favourites.contains(jewellery)) {
   // _favouriteList.add(jewellery)
    favourites.add(jewellery)

    favouriteRef.child(jewellery.Name).setValue(jewellery)
}
    }
    fun onDeleteFavouriteList(jewellery: Jewellery)
    {
           favourites.remove(jewellery)

            favouriteRef.child(jewellery.Name).removeValue()
    }
}

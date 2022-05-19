package com.bk.arjaal


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.bk.arjaal.ui.theme.ARJaalTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch


private var TIME_OUT:Long = 5000

val necklacesList : MutableList<Jewellery> = mutableListOf()
val banglesList : MutableList<Jewellery> = mutableListOf()
val earRingsList : MutableList<Jewellery> = mutableListOf()
val ringsList : MutableList<Jewellery> = mutableListOf()

var mainList : MutableList<Jewellery> = mutableListOf()

private lateinit var auth : FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        val rootRef = FirebaseDatabase.getInstance().reference
        val mainref = rootRef.child("jewelleries")//.child("bangles")

        lifecycleScope.launch {

            mainref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {

                        val path = it.key

                        val subref = mainref.child(path.toString())

                        subref.addValueEventListener(object : ValueEventListener {

                            override fun onDataChange(snapshot: DataSnapshot) {
                                snapshot.children.forEach {

                                    val jewel = it.getValue(Jewellery::class.java)

                                    if (jewel != null) {
                                        mainList.add(jewel)

                                        Log.d("CHECKING", jewel.Name)

                                        if (jewel.Category == "Necklaces") {
                                            necklacesList.add(jewel)
                                        } else if (jewel.Category == "Bangles") {
                                            banglesList.add(jewel)
                                        } else if (jewel.Category == "Rings") {
                                            ringsList.add(jewel)
                                        } else if (jewel.Category == "Ear rings") {
                                            earRingsList.add(jewel)
                                        }
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.d("CHECKING", error.message)
                            }

                        })

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        val viewModel = JewelleryViewModel()
        val jewelleriesList = viewModel.jewelleriesList

        Log.d("UNKNOWN","before load splashscreen")
        loadSplashScreen(user)
        setContent {
            ARJaalTheme {

                Surface(color = MaterialTheme.colors.background) {
                    SplashScreen(jewelleriesList)
                }
            }
        }
    }

    private fun loadSplashScreen(user: FirebaseUser?) {

        Handler().postDelayed({
            if(user != null ) {


                Log.d("UNKNOWN","inside load splashscreen")
                // Launching MainActivity
                val mainintent = Intent(this, MainActivity::class.java)
                this.startActivity(mainintent)
                finish()
            } else{
//TODO change this MainAcitvity to LoginActivity Class to check firebase
                Log.d("UNKNOWN","inside load splashscreen")
                val loginintent = Intent(this, MainActivity::class.java)
                this.startActivity(loginintent)
                finish()
            }
        }, TIME_OUT)
    }
    companion object {

        const val LOCATION_PERMISSION = 100
        const val RC_SIGN_IN = 6

    }
}


package com.bk.arjaal

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bk.arjaal.ui.theme.ARJaalTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.bk.arjaal.SplashActivity.Companion.RC_SIGN_IN
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

lateinit var mGoogleSignInClient: GoogleSignInClient
val Req_Code:Int=123
val firebaseAuth= FirebaseAuth.getInstance()

val rootRef = FirebaseDatabase.getInstance().reference

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure Google Sign In inside onCreate mentod
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()// getting the value of gso inside the GoogleSigninClient
        mGoogleSignInClient=GoogleSignIn.getClient(this,gso)// initialize the firebaseAuth variable firebaseAuth= FirebaseAuth.getInstance()


        setContent {
            ARJaalTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    loginScreen()
                }
            }
        }
    }

    @Composable
    fun loginScreen() {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
                Text(text = "SignIn with Google",color = Color.Black,fontSize = 20.sp)
                Spacer(modifier = Modifier.size(20.dp))
                Button(onClick = {
                    signInGoogle()
                }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center) {
                        Image(
                            painter = painterResource(R.drawable.ic_google),
                            contentDescription = "google",
                            modifier = Modifier.size(40.dp)
                        )
                        Text(text = "SignIn",color = Color.Black,fontSize = 20.sp)//,modifier = Modifier.padding(10.dp))
                    }
                }
        }

    }

    fun signInGoogle(){

    val signInIntent:Intent=mGoogleSignInClient.signInIntent
    startActivityForResult(signInIntent,Req_Code)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==Req_Code) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
     }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
     try {
    val account: GoogleSignInAccount? =completedTask.getResult(ApiException::class.java)
    if (account != null) {
        UpdateUI(account)
    }
    } catch (e:ApiException){
    Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show()
    }
    }

    private fun UpdateUI(account: GoogleSignInAccount){
    val credential= GoogleAuthProvider.getCredential(account.idToken,null)
    firebaseAuth.signInWithCredential(credential).addOnCompleteListener {task->
    if(task.isSuccessful) {

        val userRef = rootRef.child("users").child(firebaseAuth.currentUser?.uid.toString())
        userRef.child("Name").setValue(firebaseAuth.currentUser?.displayName.toString())
        userRef.child("Email").setValue(firebaseAuth.currentUser?.email.toString())

        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
        finish()
    }
    }
    }

}

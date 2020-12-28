package bernardash.creations.crud

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import bernardash.creations.crud.entities.User
import com.google.firebase.database.*
import java.util.*

private lateinit var mdatabase: DatabaseReference
private lateinit var mtextviewName: TextView
private var users = mutableListOf<User>()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mtextviewName = findViewById(R.id.main_textview_name)

    }

    override fun onStart() {
        super.onStart()
        mdatabase = FirebaseDatabase.getInstance().reference
        //create a new user in firebase
        createUser("Erick","Erick@gmail.com","20","90")
        createUser("Bernard","Bernard@gmail.com","19","80")
        //fills the vector with the firebase values ​​for the read
        readUser()
        //updates a user's data in firebase
        updateUser("13ccf51a-976d-4979-8200-1a07d9dd10c0","John","newJohnemail@gmail.com","35","95")
        //removes a user from firebase
        deleteUser("1522651b-256c-8079-1050-2a07d9ee53c0")


        //I put a delay time of 5 seconds to successfully collect the data from the firebase before adding it to the textview

        val handler = Handler()

        handler.postDelayed(Runnable {
                 mtextviewName.setText(users[0].username)
        }, 5000)

    }
}
private fun createUser(name: String, email: String?, age: String?, weight: String?) {
    var uniqueID = UUID.randomUUID().toString()
    val user = User(uniqueID, name, email, age, weight)
    mdatabase.child("users").child(uniqueID).setValue(user)
}
private fun readUser(){

    val query = mdatabase.child("users")
    query.addValueEventListener(object : ValueEventListener { // Procura de valores através de uma query

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            dataSnapshot.children.forEach { x ->
                x.children.let {
                    val name = x.child("username").value.toString()
                    val age = x.child("age").value.toString()
                    val id = x.child("userId").value.toString()
                    val weight = x.child("weight").value.toString()
                    val email = x.child("email").value.toString()
                    val user = User(id, name, email, age, weight)
                    users.add(user)
                }
            }
        }
        override fun onCancelled(databaseError: DatabaseError) {
            Log.e("Debug", "Erro ao obter dados!")
        }
    })
}
private fun updateUser(id: String, name: String, email: String?, age: String?, weight: String?) {
    val user = User(id, name, email, age, weight)
    mdatabase.child("users").child(id).setValue(user)
}
private fun deleteUser(id: String) {
    mdatabase.child("users").child(id).removeValue()
}

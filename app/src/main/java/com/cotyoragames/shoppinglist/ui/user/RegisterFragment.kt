package com.cotyoragames.shoppinglist.ui.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.cotyoragames.shoppinglist.R
import com.cotyoragames.shoppinglist.data.db.entities.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.view.*

class RegisterFragment : Fragment() {

    private val auth: FirebaseAuth = Firebase.auth
    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view= inflater.inflate(R.layout.fragment_register, container, false)
        val loginbutton : Button = view.findViewById(R.id.buttonlogin)
        loginbutton.setOnClickListener {
            val manager = requireActivity().supportFragmentManager
            val transaction = manager.beginTransaction()
                .setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_left_to_right,R.anim.enter_right_to_left,R.anim.exit_right_to_left)
                .replace(R.id.frame, LoginFragment())
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }
        view.registerbutton.setOnClickListener {
            val email= view.emailtxt.text.toString()
            val password=view.registernametxt.text.toString()
            activity?.let { it1 ->
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(it1) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("FireAuth", "createUserWithEmail:success")
                            val user = task.result.user
                            val newUser = Users(user!!.uid,
                                registernametxt.text.toString() ,if(user.email != null) user.email!! else "" ,if(user.photoUrl != null) user.photoUrl.toString() else "")
                            val docData = hashMapOf(
                                "uid" to newUser.useruid,
                                "displayName" to newUser.displayName,
                                "email" to newUser.email,
                                "photoUrl" to newUser.photoUrl,
                                "friends" to listOf<Users>(),
                            )
                            db.collection("users").add(docData).addOnSuccessListener {
                                val manager = requireActivity().supportFragmentManager
                                manager.beginTransaction()
                                    .setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_left_to_right,R.anim.enter_right_to_left,R.anim.exit_right_to_left)
                                    .replace(R.id.frame, LoginFragment())
                                    .disallowAddToBackStack()
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .commit()

                            }.addOnFailureListener {  e -> Log.w("FireStore", "Error writing document", e)  }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("FireAuth", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(context, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()

                        }
                    }
            }
        }

        return view
    }


}
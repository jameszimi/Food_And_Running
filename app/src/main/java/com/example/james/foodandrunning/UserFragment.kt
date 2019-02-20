package com.example.james.foodandrunning


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_user.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class UserFragment : Fragment() {

    val dummyuid = "4JXuStGGvTfxbJe3anMC2glJNEZ2"
    val TAG = "UserFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_user, container, false)


        //btn setting user
        val usersettingbt = v.findViewById<ImageView>(R.id.usersetting)
        usersettingbt.setOnClickListener {
            val clickIntent = Intent(activity,UserEditdataActivity::class.java)
            startActivity(clickIntent)
        }

        //get weight
        val db = FirebaseFirestore.getInstance()

        val weightdata = db.collection("WEIGHT_TABLE").whereEqualTo("member_uid",dummyuid)
            weightdata.get().addOnSuccessListener { doc ->
            for (document in doc) {
                val datahash = document.data
                user_weight.text = (datahash["weight_value"].toString()+" kg")
            }
        }
            .addOnFailureListener {
                Log.d(TAG,"Query Fail")
            }






        return v
    }
    companion object {
        fun newInstance(): UserFragment = UserFragment()
    }

}

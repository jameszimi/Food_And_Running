package com.example.james.foodandrunning


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_user.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class UserFragment : Fragment() {

    val TAG = "UserFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_user, container, false)

        //get UID
        val uid = AppPreferences(v.context).getPreferenceUID()

        //btn setting user
        val usersettingbt = v.findViewById<ImageView>(R.id.usersetting)
        usersettingbt.setOnClickListener {
            val clickIntent = Intent(activity,UserEditdataActivity::class.java)
            startActivity(clickIntent)
        }

        //set FirebaseFirestore
        val db = FirebaseFirestore.getInstance()

        val user_weight = v.findViewById<TextView>(R.id.user_weight)
        var member_weight : String

        //get weight
        val weightdata = db.collection("MEMBER_TABLE").document(uid)
        val callWeightData = db.collection("WEIGHT_TABLE")
            weightdata.get().addOnSuccessListener { doc ->
            if (doc != null) {
                val dataMember = doc.data as HashMap<String,Any>
                //user_weight.text = (dataMember["weight_value"].toString() + "Kg")
                member_weight = dataMember["member_weight"].toString()
                Toast.makeText(v.context,member_weight,Toast.LENGTH_SHORT).show()
                callWeightData.document(member_weight).get().addOnSuccessListener {
                    if (it != null) {
                        val dataWeight = it.data as HashMap<String,Any>
                        user_weight.text = (dataWeight["weight_value"].toString()+" Kg")
                    }
                }.addOnFailureListener {
                    Log.d(TAG,"Get Weight Error")
                }

            }
        }
            .addOnFailureListener {
                Log.d(TAG,"Query Fail")
            }

        //update weight
        v.usUpdateWeight.setOnClickListener {

            Toast.makeText(v.context,"UpDate",Toast.LENGTH_SHORT).show()

            val builder = AlertDialog.Builder(v.context)
            val linearLayout = layoutInflater.inflate(R.layout.dialog_updateweight,null)
            builder.setView(linearLayout)
            builder.setTitle("อัพเดทน้ำหนัก")

            val cancleBtn = linearLayout.findViewById<Button>(R.id.upWeightCancle)
            val summitBtn = linearLayout.findViewById<Button>(R.id.upWeightSumit)
            val dialog : AlertDialog = builder.create()
            dialog.show()

            cancleBtn!!.setOnClickListener {
                Toast.makeText(v.context,"ยกเลิกกก",Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            summitBtn!!.setOnClickListener {
                val weightEditText = linearLayout.findViewById<EditText>(R.id.dialog_weight_text)
                updateWeight(weightEditText.text.toString(),uid)

                dialog.dismiss()
                Toast.makeText(v.context,weightEditText.text,Toast.LENGTH_SHORT).show()
                val ft = fragmentManager!!.beginTransaction()
                ft.detach(this).attach(this).commit()
            }

        }








        return v
    }

    private fun updateWeight(weightInCome: String, uid: String) {
        val db = FirebaseFirestore.getInstance()
        val updateWeightCL = db.collection("WEIGHT_TABLE").document()
        val updateTOUID = db.collection("MEMBER_TABLE").document(uid)
        val dataHash = HashMap<String,Any>()

        //par data
        dataHash["member_uid"] = uid
        dataHash["weight_value"] = weightInCome
        dataHash["weight_update"] = FieldValue.serverTimestamp()

        updateWeightCL.set(dataHash).addOnSuccessListener {
            updateTOUID.update("member_weight",updateWeightCL.id).addOnSuccessListener {
                val ft = fragmentManager!!.beginTransaction()
                ft.detach(this).attach(this).commit()
                Toast.makeText(context,updateWeightCL.id,Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context,"ERROR",Toast.LENGTH_SHORT).show()
            }

        }


    }

    companion object {
        fun newInstance(): UserFragment = UserFragment()
    }

}

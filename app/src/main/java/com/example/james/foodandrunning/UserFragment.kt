package com.example.james.foodandrunning


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.james.foodandrunning.setupdata.AppPreferences
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_user.view.*
import java.util.*


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

        val user_weight = v.findViewById<TextView>(R.id.user_weight)

        //get weight

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

        //set goal
        v.setgoal.setOnClickListener {
            val builder =AlertDialog.Builder(v.context)
            val linearLayout = layoutInflater.inflate(R.layout.dialog_setgoal,null)
            builder.setView(linearLayout)
            builder.setTitle("ตั้งเป้าหมาย")

            val canclegoal = linearLayout.findViewById<Button>(R.id.setgoal_cancle)
            val summitgoal = linearLayout.findViewById<Button>(R.id.setgoal_summit)
            val dialog = builder.create()
            dialog.show()

            canclegoal!!.setOnClickListener {
                dialog.dismiss()
            }

            summitgoal!!.setOnClickListener {
                val goal = linearLayout.findViewById<EditText>(R.id.goal_text).text.toString().toInt()
                val base = AppPreferences(this.context!!).getPreferenceWeight()
                val height = AppPreferences(this.context!!).getPreferenceHeight() / 100.0
                val bMIGoal = goal / (height * height).toDouble()
                println("BMI GOAL : $bMIGoal height:$height")

                if (bMIGoal < 18.5) {
                    Toast.makeText(
                        this.context!!,
                        "น้ำหนักที่คุณระบุต่ำกว่ามาตรฐานมวลกาย\nกรุณาระบุใหม่",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }

                AppPreferences(this.context!!).setPreferenceGoal(goal)
                AppPreferences(this.context!!).setPreferenceBase(base)
                //refresh
                val ft = fragmentManager!!.beginTransaction()
                ft.detach(this).attach(this).commit()
                println(TAG+" goal:$goal")
                dialog.dismiss()
            }
        }

        val appPreferences = AppPreferences(this.context!!)
        val weightBase = appPreferences.getPreferenceBase()
        val weightGoal = appPreferences.getPreferenceGoal().toDouble()
        val status = appPreferences.getPreferenceStatus()
        val progressBar = v.findViewById<ProgressBar>(R.id.progressBarToGoal)
        val weightBase_text = v.findViewById<TextView>(R.id.weight_base)
        val weightGoal_text = v.findViewById<TextView>(R.id.weight_goalText)
        val predictionDay = v.findViewById<TextView>(R.id.prediction_text)
        weightBase_text.text = weightBase.toString()
        weightGoal_text.text = weightGoal.toString()

        val userWeight = appPreferences.getPreferenceWeight()
        user_weight.text = userWeight.toString()

        var progress : Double = (userWeight-weightGoal)*100/(weightBase-weightGoal)
        println(TAG + "progress:$progress")

        if (progress < 100) {
            progressBar.progress = (100.0-progress).toInt()
        } else {
            progress = 100.toDouble()
            progressBar.progress = (100.0-progress).toInt()
        }

        val preday = ((userWeight-weightGoal)/0.907)*7
        predictionDay.text = preday.toInt().toString() + " วัน"

        v.weightchartBtn.setOnClickListener {
            val clickIntent = Intent(activity,WeightchartActivity::class.java)
            startActivity(clickIntent)
        }

        if (status == 2) {
            v.addFoodBtn.visibility = View.VISIBLE
            v.editFoodBtn.visibility = View.VISIBLE
        }

        v.addFoodBtn.setOnClickListener {
            val intent = Intent(activity, AddfoodActivity::class.java)
            startActivity(intent)
        }

        v.editFoodBtn.setOnClickListener {
            val intent = Intent(activity, FoodEditDetailActivity::class.java)
            startActivity(intent)
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
        dataHash["weight_value"] = weightInCome.toFloat()
        dataHash["weight_update"] = FieldValue.serverTimestamp()

        updateWeightCL.set(dataHash).addOnSuccessListener {
            updateTOUID.update("member_weight",updateWeightCL.id).addOnSuccessListener {
                val ft = fragmentManager!!.beginTransaction()
                AppPreferences(context!!).setPreferenceWeight(weightInCome.toFloat())
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

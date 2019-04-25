package com.example.james.foodandrunning


import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.util.Log
import android.view.View
import android.widget.*
import com.example.james.foodandrunning.setupdata.AppPreferences
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_user_editdata.*
import kotlinx.android.synthetic.main.dialog_editheight.*
import org.w3c.dom.Text

class UserEditdataActivity : AppCompatActivity() {

    lateinit var toolbar : ActionBar
    val TAG = "UserEdirdataActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_editdata)

        //set toolbar
        setSupportActionBar(findViewById(R.id.mtoolbar))
        toolbar = supportActionBar!!
        toolbar.title = "แก้ไขข้อมูลสมาชิก"
        toolbar.setDisplayHomeAsUpEnabled(true)

        val uid = AppPreferences(this).getPreferenceUID()

        val diary_routine = AppPreferences(this).getPreferenceRoutine()
        if (diary_routine != 0) {
            val nameofroutine = when (diary_routine) {
                1 -> "นั่งหรือนนอนตลอด"
                2 -> "นั่งโต๊ะทำงานตลอด เคลื่อนไหวเล็กน้อย"
                3 -> "นั่งโต๊ะทำงานตลอด เคลื่อนไหวพอสมควร"
                4 -> "ยืนทำงานตลอด"
                else -> "การทำงานที่มีการเคลื่อนไหวมาก"
            }
            member_diaryroutine.text = nameofroutine
        }


        val appPreferences = AppPreferences(this)
        val username = appPreferences.getPreferenceUsername()
        val height = appPreferences.getPreferenceHeight()
        val email = appPreferences.getPreferenceEmail()
        val birthday = appPreferences.getPreferenceBirthday().toString()

        member_name.text = username
        member_height.text = height.toString()
        member_email.text = email
        validateDate(birthday)


        //set callText
        val editPassText = findViewById<TextView>(R.id.editpass_text)
        val editHeightText = findViewById<TextView>(R.id.editeHeight_text)
        val editRoleText = findViewById<TextView>(R.id.editeRole_text)

        editPassText.setOnClickListener {

            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_editpass)
            dialog.setTitle("เปลี่ยนรหัสผ่าน")

            //call btn
            val cancleBtn = dialog.findViewById<Button>(R.id.cancleBtn)
            val summitBtn = dialog.findViewById<Button>(R.id.sumitBtn)
            val newPassword = dialog.findViewById<EditText>(R.id.newPass_text)
            val confNewPass = dialog.findViewById<EditText>(R.id.newPassCon_text)
            val oldPassword = dialog.findViewById<EditText>(R.id.oldPass_text)
            val dialogTxtErPasUp = dialog.findViewById<TextView>(R.id.dialogTxtErPasUp)



            cancleBtn.setOnClickListener {
                dialog.dismiss()
            }

            summitBtn.setOnClickListener {

                dialogTxtErPasUp.text = ""
                val newPassText = newPassword.text.toString()
                val conPassText = confNewPass.text.toString()
                val oldPassText = appPreferences.getPreferencePassword()
                if (newPassText.isNotEmpty() && conPassText.isNotEmpty()) {
                    Toast.makeText(this, "If 1 $newPassText $conPassText $oldPassText",Toast.LENGTH_SHORT).show()
                    if ((newPassText == conPassText) && (oldPassText == oldPassword.text.toString())) {
                        val user = FirebaseAuth.getInstance().currentUser
                        Log.d(TAG,user.toString())
                        user?.updatePassword(newPassText)?.addOnCompleteListener {
                            if (it.isSuccessful) {
                                upDatePassWord(newPassText,uid)
                                Log.d(TAG,"User password update.")

                            } else {
                                Log.d(TAG,"Complete Fail")
                            }
                        }

                    } else {
                        dialogTxtErPasUp.text = "รหัสเก่าไม่ถูกต้อง"
                    }
                } else {
                    dialogTxtErPasUp.text = "รหัสผ่านใหม่ไม่ตรงกัน"
                }
            }
            dialog.show()
        }

        editHeightText.setOnClickListener {

            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_editheight)
            dialog.setTitle("แก้ไขส่วนสูง")

            val cancBtn = dialog.findViewById<Button>(R.id.btn_heightcancle)
            val summitBtn = dialog.findViewById<Button>(R.id.btn_heightsummit)
            val heightInCome = dialog.findViewById<EditText>(R.id.height_text)
            val text = dialog.findViewById<TextView>(R.id.textView32)
            text.text = "ส่วนสูง"

            cancBtn.setOnClickListener {
                dialog.dismiss()
            }

            summitBtn.setOnClickListener {
                val heighteditT = heightInCome.text.toString().toInt()
                if (heightInCome.text.isNotEmpty()) {
                    upDateHeight(heighteditT, uid)
                } else {
                    upheight.text = "กรุณาระบุส่วนสูง"
                }
            }
            dialog.show()
        }

        editRoleText.setOnClickListener {

            val listOfRoutine = arrayOf("นั่งหรือนนอนตลอด","นั่งโต๊ะทำงานตลอด เคลื่อนไหวเล็กน้อย","นั่งโต๊ะทำงานตลอด เคลื่อนไหวพอสมควร","ยืนทำงานตลอด","ทำงานที่มีการเคลื่อนไหวมาก")
            var routine = 0
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_editrole)
            dialog.setTitle("เลือกกิจวัตรประจำวัน")

            val cancleBtn = dialog.findViewById<Button>(R.id.btn_canclerole)
            val summitBtn = dialog.findViewById<Button>(R.id.btn_summitrole)
            val spinrole = dialog.findViewById<Spinner>(R.id.spinner_role)

            spinrole.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,listOfRoutine)

            spinrole.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val routines = listOfRoutine[position].trim()
                    routine = when (routines) {
                        "นั่งหรือนนอนตลอด" -> 1
                        "นั่งโต๊ะทำงานตลอด เคลื่อนไหวเล็กน้อย" -> 2
                        "นั่งโต๊ะทำงานตลอด เคลื่อนไหวพอสมควร" -> 3
                        "ยืนทำงานตลอด" -> 4
                        else -> 5
                    }
                }
            }

            cancleBtn.setOnClickListener {
                dialog.dismiss()
            }

            summitBtn.setOnClickListener {
                upDateRoutine(routine,uid)
            }

            dialog.show()

        }

        //Logout
        logoutBtn.setOnClickListener {
            signOut()
        }

    }

    private fun validateDate(datein: String) {

        println(TAG+" datein:$datein")
        val datecount = datein.length
        var dd : String
        var mm : String
        var yyyy : String


        if (datecount == 6){
            dd = datein.substring(0,1)
            mm = datein.substring(1,2)
            yyyy = datein.substring(2)
            showbirthday(dd,mm,yyyy)

        }
        if (datecount == 7) {
            if ((datein.substring(0,2).toInt() > 12) && (datein.substring(0,2).toInt() < 32)) {
                dd = datein.substring(0,2)
                mm = datein.substring(2,3)
                yyyy = datein.substring(3)
                showbirthday(dd,mm,yyyy)
            } else {
                dd = datein.substring(0,2)
                mm = datein.substring(2,3)
                yyyy = datein.substring(3)
                showbirthday(dd,mm,yyyy)
            }

        } else {
            dd = datein.substring(0,2)
            mm = datein.substring(2,4)
            yyyy = datein.substring(4)
            showbirthday(dd,mm,yyyy)
        }

    }

    private fun showbirthday(day: String, mounth: String, year: String) {
        val textShow = findViewById<TextView>(R.id.member_birthday)
        val textbirthday:String = when (mounth.toInt()) {
            1 -> "ม.ค."
            2 -> "ก.พ."
            3 -> "มี.ค."
            4 -> "เม.ย."
            5 -> "พ.ค."
            6 -> "มิ.ย."
            7 -> "ก.ค."
            8 -> "ส.ค."
            9 -> "ก.ย."
            10 -> "ต.ค."
            11 -> "พ.ย."
            else -> "ธ.ค."
        }
        textShow.text = "$day $textbirthday $year"
        println(TAG+"dddddddddd"+"$day $textbirthday $year")
    }

    private fun upDateRoutine(routine: Int, uid: String) {

        val db = FirebaseFirestore.getInstance()
        val updateTable = db.collection("MEMBER_TABLE").document(uid)
        updateTable.update("member_diaryroutine", routine).addOnSuccessListener {
            AppPreferences(this).setPreferenceRoutine(routine)
            finish()
            startActivity(intent)
        }.addOnFailureListener {
            Toast.makeText(this,"ไม่สามารถอัพเดทกิจวัตประจำวันได้ กรุณาลองใหม่อีกครั้ง", Toast.LENGTH_SHORT).show()
        }

    }

    private fun upDateHeight(height: Int, uid: String) {
        val db = FirebaseFirestore.getInstance()
        val updatTable = db.collection("MEMBER_TABLE").document(uid)
        updatTable.update("member_height",height).addOnSuccessListener {
            AppPreferences(this).setPreferenceHeight(height)
            finish()
            startActivity(intent)
        }.addOnFailureListener {
            Toast.makeText(this,"ไม่สามารถอัพเดทส่วนสูงได้ กรุณาลองใหม่อีกครั้ง", Toast.LENGTH_SHORT).show()
        }

    }

    private fun signOut() {

        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                AppPreferences(this).setPreferenceUID("")
                startActivity(Intent(this, LoginActivity::class.java))
                this.finish()
            }

    }

    private fun upDatePassWord(newPassText: String, uid: String) {

        FirebaseFirestore.getInstance().collection("MEMBER_TABLE").document(uid).update("member_password",newPassText).addOnSuccessListener {
            signOut()
            AppPreferences(this).setPreferencePassword(newPassText)
        }.addOnFailureListener {
            Toast.makeText(this,"อัพเดทรหัสผ่านไม่ำเร็จ", Toast.LENGTH_SHORT).show()
        }

    }

}

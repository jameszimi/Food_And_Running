package com.example.james.foodandrunning

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

//import java.util.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var mRegisterBtn : Button
    private lateinit var mUsername : EditText
    private lateinit var mPassword : EditText
    private lateinit var mConfPass : EditText
    private lateinit var mEmail : EditText
    private lateinit var mName : EditText
    private lateinit var mDay : Spinner
    lateinit var mMonth : Spinner
    lateinit var mYear: Spinner
    lateinit var mHeight : EditText
    private lateinit var mWeight : EditText
    lateinit var mRoutine : Spinner
    lateinit var mSex : Spinner
    lateinit var mtexterror : TextView
    val TAG = "RegisterActivity"
    //lateinit var mAuth: FirebaseAuthException


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        //mDatabase = FirebaseDatabase.getInstance().getReference("MEMBER_TABLE")

        val listOfDay = ArrayList<Int>(dayList())
        val listOfMonth = ArrayList<Int>(monthList())
        val listOfYear = ArrayList<Int>(yearList())
        val listOfRoutine = arrayOf("นั่งหรือนนอนตลอด","นั่งโต๊ะทำงานตลอด เคลื่อนไหวเล็กน้อย","นั่งโต๊ะทำงานตลอด เคลื่อนไหวพอสมควร","ยืนทำงานตลอด","ทำงานที่มีการเคลื่อนไหวมาก")
        val listOfSex = arrayOf("ชาย","หญิง")
        var userName :String
        var password :String
        var confPass :String
        var email :String
        var name :String
        var day = 0
        var month = 0
        var year = 0
        var height :String
        var weight :String
        var routine = 0
        var sex = 0

        mRegisterBtn=findViewById(R.id.regisButton)
        mUsername=findViewById(R.id.regisUsername)
        mPassword=findViewById(R.id.regisPassword)
        mConfPass=findViewById(R.id.regisConfPass)
        mEmail=findViewById(R.id.regisEmail)
        mName=findViewById(R.id.regisName)
        mDay=findViewById(R.id.spinnerDay)
        mMonth=findViewById(R.id.spinnerMount)
        mYear=findViewById(R.id.spinnerYear)
        mHeight=findViewById(R.id.regisHeight)
        mWeight=findViewById(R.id.regisWeight)
        mRoutine=findViewById(R.id.spinnerRoutine)
        mSex=findViewById(R.id.spinnerSex)
        mtexterror=findViewById(R.id.textView13)

        mDay.adapter = ArrayAdapter<Int>(this,android.R.layout.simple_spinner_dropdown_item,listOfDay)
        mMonth.adapter = ArrayAdapter<Int>(this,android.R.layout.simple_spinner_dropdown_item,listOfMonth)
        mYear.adapter = ArrayAdapter<Int>(this,android.R.layout.simple_spinner_dropdown_item,listOfYear)
        mRoutine.adapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,listOfRoutine)
        mSex.adapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,listOfSex)

        mDay.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Toast.makeText(this@RegisterActivity,"เลือกวันเกิด",Toast.LENGTH_LONG).show()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                day = listOfDay[position]
                // like listOfDay.get(position)
            }
        }

        mMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                month = listOfMonth[position]
            }

        }

        mYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                year = listOfYear[position]
            }

        }

        mRoutine.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
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

        mSex.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val sexs = listOfSex[position].trim()
                sex = when (sexs) {
                    "ชาย" -> 1
                    else -> 2
                }
            }

        }

        mRegisterBtn.setOnClickListener {

            val birthday: String = day.toString()+month.toString()+year.toString()
            val preuser = HashMap<String,Any>()

            userName = mUsername.text.toString()
            password = mPassword.text.toString()
            confPass = mConfPass.text.toString()
            email = mEmail.text.toString()
            name = mName.text.toString()
            height = mHeight.text.toString()
            weight = mWeight.text.toString()

            try {
                //check username
                if (userName.isEmpty()){
                    Toast.makeText(this, "กรุณาใส่ Username",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                preuser["member_username"] = userName

                //check password
                if (password.length < 7) {
                    Toast.makeText(this,"Password ต้องมาความยาวมากกว่าหรือเท่ากับ 8 ตัวอักษร", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                //check conPass
                if (confPass != password) {
                    Toast.makeText(this,"Password ไม่ตรงกัน", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                preuser["member_password"] = password

                //check email
                if (email.isEmpty()) {
                    Toast.makeText(this,"กรุณาใส่ Email", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                preuser["member_email"] = email

                //check name
                if (name.isEmpty()) {
                    Toast.makeText(this,"กรุณาใส่ ชื่อนามสกุล", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                preuser["member_name"] = name

                //check hight
                if (height.isEmpty()) {
                    Toast.makeText(this,"กรุณาระบุส่วนสูง", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                preuser["member_height"] = height

                //check weight
                if (weight.isEmpty()) {
                    Toast.makeText(this,"กรุณาระบบุน้ำหนัก", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                preuser["member_birthday"] = birthday
                preuser["member_sex"] = sex
                preuser["member_diaryroutine"] = routine
                preuser["member_status"] = 1

                println(TAG+preuser)

                //registerFuction(email,password, preuser, weight)

            } catch(e:Exception) {
                Log.d("RegisterActivity", "mRegisterBtn.setOnClickListener")
            }

        }

    }

    //create user
    private fun registerFuction(email: String, password: String, preuser: HashMap<String, Any>, weight: String) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful){
                println("print is success uid:${it.result!!.user.uid}")
                saveWeight(preuser, weight)
            } else{
                Toast.makeText(this,"สมัครสมาชิกไม่สำเร็จ หรือเป็นสมาชิกอยู่แล้ว", Toast.LENGTH_SHORT).show()
                println("println not success:")
                return@addOnCompleteListener
            }
        }.addOnFailureListener {
            println("member add fail:${it.message}")
        }
    }

    //create weight table
    private fun saveWeight(preuser: HashMap<String, Any>, weight: String) {

        val weightHash = HashMap<String,Any>()
        val uid = FirebaseAuth.getInstance().uid ?:""
        val db = FirebaseFirestore.getInstance()

        weightHash["member_uid"] = uid
        weightHash["weight_value"] = weight

        db.collection("WEIGHT_TABLE").add(weightHash).addOnSuccessListener {

            Log.d("Register", "DocumentSnapshot added with ID: " + it.id)

            //call saveUser function and send <userdata>, <id weight>, <uid>
            saveUserToFirebase(preuser, it.id, uid)
        }
        .addOnFailureListener {

            Log.d("Register", "Error adding document")
        }

    }

    private fun saveUserToFirebase(user: HashMap<String, Any>, weightid: String, uid: String) {

        val db = FirebaseFirestore.getInstance()

        user["member_weight"] = weightid

        db.collection("MEMBER_TABLE").document(uid).set(user).addOnSuccessListener {

            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
            Log.d("Register", "DocumentSnapshot added with ID: ")
        }
            .addOnFailureListener {
                Log.d("Register", "Error adding document")
            }

    }

    //Creat List day 1-31
    private fun dayList(): ArrayList<Int> {
        val dayList = ArrayList<Int>()
        for (i in 1..31){
            dayList.add(i)
        }
        return dayList
    }

    private fun monthList(): ArrayList<Int> {
        val monthList = ArrayList<Int>()
        for (i in 1..12){
            monthList.add(i)
        }
        return monthList
    }

    private fun yearList(): ArrayList<Int> {
        val yearList = ArrayList<Int>()
        for (i in 2561 downTo 2461){
            yearList.add(i)
        }
        return yearList
    }

}
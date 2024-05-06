package com.example.capstonedesign.Activity

import Data.SignUpData
import Response.SignUpResponse
import Service.SignUpService
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.NumberPicker
import android.widget.PopupMenu
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.capstonedesign.R
import com.example.capstonedesign.RetrofitClient
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

class SignUpActivity : AppCompatActivity() {
    fun printToast(message : String){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var c = applicationContext
        //EditText 선언 모음
        var edtId = findViewById<EditText>(R.id.edtId)
        var edtPw = findViewById<EditText>(R.id.edtPw)
        var edtPwCheck = findViewById<EditText>(R.id.edtPwCheck)
        var edtNickname = findViewById<EditText>(R.id.edtNickname)
        var edtHeight = findViewById<EditText>(R.id.edtHeight)
        var edtWeight = findViewById<EditText>(R.id.edtWeight)

        // 실제 회원가입 요청에 필요한 데이터
        var id : String = ""
        var pw : String = ""
        var pwCheck : String = ""
        var nickname : String = ""
        var email : String = ""
        var height : Int = -1
        var weight : Int = -1

        //중복 체크 관련
        var btnIdCheck = findViewById<ImageButton>(R.id.btnIdCheck)
        var isIdChecked = false
        btnIdCheck.setOnClickListener {
            if(!edtId.text.toString().equals("")){
                if(edtId.text.toString().equals("kiatae0722")){
                    printToast("입력하신 아이디가 이미 사용중입니다")
                }
                else {
                    printToast("입력하신 아이디가 사용가능합니다.");
                    isIdChecked = true
                    id = edtId.text.toString()
                }
                /*if(edtId.text.toString().equals("taewoo9240")){
                    print("입력하신 아이디가 이미 사용중입니다.")
                }
                else{
                    print("입력하신 아이디가 사용가능합니다.")
                    isIdChecked = true
                    id = edtId.text.toString()
                }*/
                /*val idCheckService = RetrofitClient.retrofitInstance.create(IdCheckService::class.java)
                idCheckService.idCheck(IdCheckData(edtId.text.toString())).enqueue(object : retrofit2.Callback<IdCheckResponse>{
                    override fun onResponse(
                        call: Call<IdCheckResponse>,
                        response: Response<IdCheckResponse>
                    ) {
                        var idCheckResponse = response.body()
                        if(idCheckResponse?.isOkay == true){
                            Toast.makeText(applicationContext, "입력하신 아이디가 사용가능합니다.", Toast.LENGTH_SHORT).show()
                            id = edtId.text.toString()
                            isIdChecked = true
                        }
                        else{
                            Toast.makeText(applicationContext, "입력하신 아이디가 이미 사용중입니다. \n 다른 아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<IdCheckResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message?.toString(), Toast.LENGTH_SHORT).show()
                    }
                })*/
            }
            else{
                printToast("아이디를 입력해주세요")
            }
        }

        //성별 설정 관련
        var gender : String = "" // true면 남자, false면 여자, 그냥 String으로 할까..? 그게 더 직관적일수도
        var rgGender = findViewById<RadioGroup>(R.id.rgGender)
        rgGender.setOnCheckedChangeListener{ group, checkedId ->
            when(checkedId){
                R.id.rbMale -> gender = "MALE"
                R.id.rbFemale -> gender = "FEMALE"
            }
        }

        //출생년도 yearPicker 설정
        var yearPicker : NumberPicker = findViewById(R.id.year_picker)
        yearPicker.minValue = 1900
        yearPicker.maxValue = 2023
        yearPicker.value = 2000
        yearPicker.wrapSelectorWheel = true
        yearPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        val scale = resources.displayMetrics.density
        yearPicker.layoutParams = yearPicker.layoutParams.apply {
            this.height = (90 * scale + 0.5f).toInt() // 예시로 50dp를 사용합니다.
        }

        //활동 정도 관련
        var level : Int = -1 // 1면 하, 2면 중, 3이면 상
        var rgLevel = findViewById<RadioGroup>(R.id.rgActiveLevel)
        rgLevel.setOnCheckedChangeListener{ group, checkedId ->
            when(checkedId){
                R.id.rbLow -> level = 1
                R.id.rbMid -> level = 2
                R.id.rbHigh -> level = 3
            }
        }

        //다이어트 목표 설정 관련
        var dietPlan : Int = -1 // 1 : 빠른 체중 감소 2 : 느린 체중 감소 , 3 : 체중 유지 4 : 느린 체중 증가 5 : 빠른 체중 증가
        var btnDietPlan : Button = findViewById(R.id.btnDietPlan)
        btnDietPlan.setOnClickListener {
            var popupMenu = PopupMenu(applicationContext, it)
            menuInflater?.inflate(R.menu.menu_diet_plan, popupMenu.menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.fast_decrease -> {
                        btnDietPlan.setText("빠른 체중 감소")
                        dietPlan = 1
                        return@setOnMenuItemClickListener true
                    }
                    R.id.slow_decrease -> {
                        btnDietPlan.setText("느린 체중 감소")
                        dietPlan = 2
                        return@setOnMenuItemClickListener true

                    }
                    R.id.maintain -> {
                        btnDietPlan.setText("체중 유지")
                        dietPlan = 3
                        return@setOnMenuItemClickListener true

                    }
                    R.id.slow_increase -> {
                        btnDietPlan.setText("느린 체중 증가")
                        dietPlan = 4
                        return@setOnMenuItemClickListener true

                    }
                    R.id.fast_increase ->{
                        btnDietPlan.setText("빠른 체중 증가")
                        dietPlan = 5
                        return@setOnMenuItemClickListener true
                    }
                    else ->{
                        return@setOnMenuItemClickListener false
                    }
                }
            }
        }



        val btnLogin : Button = findViewById(R.id.btnGoToLogin)
        btnLogin.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        btnLogin.setOnClickListener{
            var intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        //최종적으로 확인을 눌렀을때 ?
        var btnSignUp : ImageButton = findViewById(R.id.btnSignUp)
        btnSignUp.setOnClickListener {
            var pattern = Patterns.EMAIL_ADDRESS
            //아이디 체크
            if(edtId.text.toString().equals("")){
                printToast("아이디를 입력해주세요")
            }
            else if(!pattern.matcher(edtId.text.toString()).matches()){
                printToast("입력하신 아이디가 이메일 형식에 맞지 않습니다.")
            }
            else if((!isIdChecked) || (!edtId.text.toString().equals(id))){
                printToast("아이디 중복체크를 해주세요")
            }
            else{
                //비밀번호 체크
                if(edtPw.text.toString().equals("")){
                    printToast("비밀번호를 입력해주세요")
                }
                else if(edtPwCheck.text.toString().equals("")){
                    printToast("비밀번호를 한 번 더 입력해주세요")
                }
                else if(!edtPw.text.toString().equals(edtPwCheck.text.toString())){
                    printToast("비밀번호가 일치하지 않습니다.")
                }
                else{
                    //그 외
                    if(edtNickname.text.toString().equals("")){
                        printToast("닉네임을 입력해주세요.")
                    }
                    else if(edtHeight.text.toString().equals("")){
                        printToast("키를 입력해주세요.")
                    }
                    else if(edtWeight.text.toString().equals("")){
                        printToast("체중을 입력해주세요.")
                    }
                    else if(gender.equals("")){
                        printToast("성별을 선택해주세요.")
                    }
                    else if(level == -1){
                        printToast("활동 정도를 선택해주세요.")
                    }
                    else if(dietPlan == -1){
                        printToast("다이어트 목표를 선택해주세요.")
                    }
                    else{
                        printToast("회원가입 요청!")
                        id = edtId.text.toString()
                        pw = edtPw.text.toString()
                        nickname = edtNickname.text.toString()
                        var year : Int = yearPicker.value
                        var height : Int = Integer.valueOf(edtHeight.text.toString())
                        var weight : Int = Integer.valueOf(edtWeight.text.toString())
                        val signUpData = SignUpData(id,pw,nickname,height,weight,gender,year,level,dietPlan)
                        val signUpService = RetrofitClient.retrofitInstance.create(SignUpService::class.java)
                        var intent = Intent(this, LoginActivity::class.java)
                        signUpService.signUp(signUpData).enqueue(object : retrofit2.Callback<SignUpResponse>{
                            override fun onResponse(
                                call: Call<SignUpResponse>,
                                response: Response<SignUpResponse>
                            ) {
                                Log.d("test", signUpData.toString())
                                if(response.isSuccessful){
                                    var responseBody = response.body()
                                    if(responseBody != null){
                                        Log.d("test","회원가입 성공")
                                        printToast("회원가입에 성공하셨습니다.\n 로그인을 해주세요.")
                                        startActivity(intent)
                                    }
                                }
                                else{
                                    Log.d("test", response.code().toString())
                                    // HTTP 상태 코드가 200이 아닌 경우 (예: 500)
                                    val errorBody = response.errorBody()?.string()
                                    val gson = Gson()
                                    try {
                                        val errorResponse = gson.fromJson(errorBody, SignUpResponse::class.java)
                                        Log.d("test", errorResponse.message)
                                        printToast(errorResponse.message)
                                    } catch (e: Exception) {
                                        Log.e("LoginError", "응답 처리 중 오류 발생", e)
                                    }
                                }
                            }

                            override fun onFailure(
                                call: Call<SignUpResponse>,
                                t: Throwable
                            ) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                }

            }
        }
    }
}

package com.example.capstonedesign

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.doinglab.foodlens2.sdk.FoodLens
import com.doinglab.foodlens2.sdk.RecognitionResultHandler
import com.doinglab.foodlens2.sdk.config.LanguageConfig
import com.doinglab.foodlens2.sdk.errors.BaseError
import com.doinglab.foodlens2.sdk.model.RecognitionResult

class MenuSelectActivity : ComponentActivity() {
    private lateinit var getContent: ActivityResultLauncher<String>
    //Create Network Service
    val foodLensService by lazy {
        FoodLens.createFoodLensService(this@MenuSelectActivity)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_select)
        //foodLens 설정
        foodLensService.setAutoRotate(true);
        foodLensService.setLanguage(LanguageConfig.KO)

        //image uri -> byteArray 변환 설정
        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            // 사용자가 이미지를 선택하면 이 콜백이 호출됩니다.
            uri?.let {
                val inputStream = contentResolver.openInputStream(uri)
                val byteArray = inputStream?.readBytes()
                // 이제 byteArray에 이미지의 바이트 배열이 들어있습니다.
                // 필요한 작업을 이 배열로 수행하세요.
                foodLensService.predict(byteArray, object : RecognitionResultHandler { //If userId does not exist
                    override fun onSuccess(result: RecognitionResult?) {
                        result?.let {
                            it.foodInfoList.forEach {
                                Log.d("FoodLens", "${it.name}")
                                Toast.makeText(applicationContext, it.name.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    override fun onError(errorReason: BaseError?) {
                        Log.d("FoodLens", errorReason?.getMessage().toString())
                        Toast.makeText(applicationContext, "error: " + errorReason?.getMessage() ,Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}


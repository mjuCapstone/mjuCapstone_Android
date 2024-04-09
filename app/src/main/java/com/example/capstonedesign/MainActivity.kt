package com.example.capstonedesign

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.doinglab.foodlens2.sdk.FoodLens
import com.doinglab.foodlens2.sdk.RecognitionResultHandler
import com.doinglab.foodlens2.sdk.config.LanguageConfig
import com.doinglab.foodlens2.sdk.errors.BaseError
import com.doinglab.foodlens2.sdk.model.RecognitionResult
import com.example.capstonedesign.ui.theme.CapstoneDesignTheme

class MainActivity : ComponentActivity() {
    private lateinit var getContent: ActivityResultLauncher<String>
    //Create Network Service
    val foodLensService by lazy {
        FoodLens.createFoodLensService(this@MainActivity)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        setContent {
            CapstoneDesignTheme {
                // A surface container using the 'background' color from the theme
                Box(modifier = Modifier.fillMaxSize()) {
                    Button(
                        onClick = {
                              getContent.launch("image/*")
                        },
                        modifier = Modifier.wrapContentSize(Alignment.Center)
                    ) {
                        Text("이미지 선택")
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CapstoneDesignTheme {
        Greeting("Android")
    }
}


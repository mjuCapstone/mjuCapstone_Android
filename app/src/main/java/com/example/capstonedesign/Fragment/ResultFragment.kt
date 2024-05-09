package com.example.capstonedesign.Fragment

import Data.AddItemData
import Data.MenuItem
import Response.AddItemResponse
import Service.AddItemService
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.doinglab.foodlens2.sdk.FoodLens
import com.doinglab.foodlens2.sdk.RecognitionResultHandler
import com.doinglab.foodlens2.sdk.config.LanguageConfig
import com.doinglab.foodlens2.sdk.errors.BaseError
import com.doinglab.foodlens2.sdk.model.FoodInfo
import com.doinglab.foodlens2.sdk.model.Nutrition
import com.doinglab.foodlens2.sdk.model.RecognitionResult
import com.example.capstonedesign.R
import com.example.capstonedesign.RetrofitClient
import com.example.capstonedesign.databinding.FragmentResultBinding
import okhttp3.internal.notifyAll
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.time.LocalDateTime

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResultFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentResultBinding
    private lateinit var menuList : ArrayList<FoodInfo>
    private var menuNum : Int = 0
    private var menuIndex = 0
    private var fileName :String = ""
    private lateinit var curMenu : AddItemData
    private lateinit var bitmap : Bitmap
    private lateinit var croppedBitmap : Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentResultBinding.inflate(inflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Create Network Service
        val foodLensService by lazy{
            context?.let { FoodLens.createFoodLensService(it) }
        }
        foodLensService?.setAutoRotate(false)
        foodLensService?.setLanguage(LanguageConfig.KO)
        val photoUri = arguments?.let{
            ResultFragmentArgs.fromBundle(it).photoUri
        }
        val inputStream = context?.contentResolver?.openInputStream(photoUri!!)
        val byteArray = inputStream?.readBytes()!!
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        // 이제 byteArray에 이미지의 바이트 배열이 들어있습니다.
        // 필요한 작업을 이 배열로 수행하세요.
        foodLensService?.predict(byteArray, object : RecognitionResultHandler { //If userId does not exist
            override fun onSuccess(result: RecognitionResult?) {
                result?.let {
                    menuList = it.foodInfoList
                    menuNum = menuList.size
                    if(menuNum == 0){
                        Toast.makeText(context, "메뉴를 찾지 못했습니다.'\n 메뉴를 검색하거나 사진을 다시 선택해주세요.", Toast.LENGTH_SHORT).show()
                        var action = ResultFragmentDirections.actionResultFragmentToInputFragment()
                        findNavController().navigate(action)
                    }
                    else {
                        setView()
                    }
                }
            }

            override fun onError(errorReason: BaseError?) {
                Log.d("FoodLens", errorReason?.getMessage().toString())
                Toast.makeText(context, "error: " + errorReason?.getMessage() ,Toast.LENGTH_SHORT).show()
            }
        })
        binding.btnAdjust.setOnClickListener{
            var dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_result)
            var edtGram : EditText = dialog.findViewById(R.id.edtGram)
            var edtServing : EditText = dialog.findViewById(R.id.edtServing)
            var btnReselect : Button = dialog.findViewById(R.id.btnReselect)
            btnReselect.setOnClickListener {
                findNavController().navigate(R.id.inputFragment)
            }
            var btnConfirm : Button = dialog.findViewById(R.id.btnConfirm)
            btnConfirm.setOnClickListener {
                setInfo(edtGram.text.toString().toInt(), edtServing.text.toString().toInt())
                binding.ivMenuImage.setImageBitmap(requireContext().openFileInput(curMenu.fileName).use{ fis ->
                    Log.d("test", "이미지 불러오는 중..")
                    BitmapFactory.decodeStream(fis)
                })
                dialog.dismiss()
            }
            var btnCancel : Button = dialog.findViewById(R.id.btnCancel)
            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            edtGram.setText(menuList.get(menuIndex).gram.toInt().toString())
            edtServing.setText("1")
            dialog.show()
        }

        binding.btnNext.setOnClickListener {

            //서버에 메뉴 아이템 정보 전송
            val addItemService = RetrofitClient.setRetroFitInstanceWithToken(requireContext()).create(AddItemService::class.java)
            addItemService.addItem(curMenu).enqueue(object : retrofit2.Callback<AddItemResponse>{
                override fun onResponse(
                    call: Call<AddItemResponse>,
                    response: Response<AddItemResponse>
                ) {
                    if(response.isSuccessful) {
                        if (binding.btnNext.text.equals("확인")) {
                            var action =
                                ResultFragmentDirections.actionResultFragmentToMainFragment()
                            findNavController().navigate(action)

                        } else {
                            menuIndex++
                            setView()
                        }
                    }
                    Toast.makeText(requireContext(), "메뉴 업로드에 실패하였습니다.\n 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<AddItemResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "메뉴 업로드에 실패하였습니다.\n 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    fun setInfo(amount : Int, person : Int){
        // 칼로리 및 영양성분 계산
        var menu = menuList.get(menuIndex)
        curMenu.amount = amount
        curMenu.serving = person
        var dif = amount / menu.baseNutrition?.gram!! / person
        curMenu.kcal = (dif * menu.baseNutrition?.energy!!).toInt()
        curMenu.carbohydrate = (dif * menu.baseNutrition?.carbohydrate!!).toInt()
        curMenu.protein = (dif * menu.baseNutrition?.protein!!).toInt()
        curMenu.fat = (dif * menu.baseNutrition?.fat!!).toInt()

        //텍스트뷰 설정
        binding.tvGram.text = amount.toString() + " g"
        binding.tvPerson.text = person.toString() + " 인분"
        binding.tvCalorie.text = curMenu.kcal.toString() + " kcal"
        binding.tvCarbo.text = curMenu.carbohydrate.toString() + " g"
        binding.tvProtein.text = curMenu.protein.toString() + " g"
        binding.tvFat.text = curMenu.fat.toString() + " g"
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun setView(){
        var menu = menuList.get(menuIndex)
        if(menu.name == null){
            Log.d("test", "setView::menuName null")
            menuIndex++
            setView()
        }
        else{
            var x : Int = menu.foodPosition?.xmin!!
            var y : Int = menu.foodPosition?.ymin!!
            var width : Int = menu.foodPosition?.xmax!! - x
            var height : Int = menu.foodPosition?.ymax!! - y
            if(x + width <= bitmap.width && y + height <= bitmap.height) {
                croppedBitmap = Bitmap.createBitmap(bitmap, x, y, width, height)
                binding.ivMenuImage.setImageBitmap(croppedBitmap)

                //생성된 bitmap 파일 로컬 저장소에 저장
                fileName = menu.fullName!!.split("(")[0] + "_" + LocalDateTime.now().toString()
                requireContext().openFileOutput(fileName, Context.MODE_PRIVATE).use{ fos->
                    croppedBitmap.compress(Bitmap.CompressFormat.PNG, 100 , fos)
                }
                // 파일 경로 얻기
                val filePath = File(requireContext().filesDir, fileName).absolutePath
                Log.d("test", "Saved file path: $filePath")
            }
            else{
                Log.d("test", x.toString() + " " + width.toString() + " " + y.toString() + " " + height.toString())
                Log.d("test", bitmap.width.toString() + " " + bitmap.height.toString())
            }
            curMenu = AddItemData(menu.fullName!!.split("(")[0], menu.gram.toInt(), 1, menu.energy.toInt(),
                menu.carbohydrate.toInt(), menu.protein.toInt(), menu.fat.toInt(), fileName)
            binding.tvMenuResultName.text = curMenu.name
            var amount = curMenu.amount.toString() + " g"
            binding.tvGram.text = amount
            binding.tvCalorie.text = curMenu.kcal.toString() + " kcal"
            binding.tvCarbo.text = curMenu.carbohydrate.toString() + " g"
            binding.tvProtein.text = curMenu.protein.toString() + " g"
            binding.tvFat.text = curMenu.fat.toString() + " g"
            val n : Nutrition? = menu.baseNutrition
            Log.d("test", "음식명 : " + menu.name + ", 음식명(재료포함) : " + menu.fullName)
            Log.d("test", "권장량\n양: " + n?.gram!!.toInt().toString() + " 칼로리: " + n?.energy!!.toInt().toString() + " 탄수화물 : " + n?.carbohydrate!!.toInt().toString() + "'\n 단백질 : " + n?.protein!!.toInt().toString() + " 지방 : " + n?.fat!!.toString())
            Log.d("test", "실제")
            Log.d("test", "권장량\n양: " + menu.gram.toInt().toString() + " 칼로리: " + menu.energy.toInt().toString() + " 탄수화물 : " + menu.carbohydrate.toInt().toString() + "'\n 단백질 : " + menu.protein.toInt().toString() + " 지방 : " + menu.fat.toString())
            if(menuIndex == menuNum - 1){
                binding.btnNext.text = "확인"
            }
        }
    }
}
package com.example.capstonedesign.Fragment

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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
import com.example.capstonedesign.databinding.FragmentResultBinding
import okhttp3.internal.notifyAll

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
    private var kcal = 0;
    private var carbo = 0;
    private var protein = 0;
    private var fat = 0;
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
            //sharedPreference에 영양성분 반영
            var activity : AppCompatActivity = context as AppCompatActivity
            var preferences = activity.getSharedPreferences("APP", AppCompatActivity.MODE_PRIVATE)
            var editor = preferences.edit()
            editor.putInt("nowKcal" , preferences.getInt("nowKcal",0) + kcal)
            editor.putInt("nowCarbo" , preferences.getInt("nowCarbo",0) + carbo)
            editor.putInt("nowProtein" , preferences.getInt("nowProtein",0) + protein)
            editor.putInt("nowFat" , preferences.getInt("nowFat",0) + fat)
            editor.apply()
            Log.d("test", preferences.getInt("nowKcal",0).toString() + " " + preferences.getInt("nowCarbo",0).toString() + " " + preferences.getInt("nowProtein",0).toString() + " " + preferences.getInt("nowFat",0))
            if(binding.btnNext.text.equals("확인")){
                var action = ResultFragmentDirections.actionResultFragmentToMainFragment()
                findNavController().navigate(action)
            }
            else{
                menuIndex++
                setView()
            }
        }
    }
    fun setInfo(gram : Int, person : Int){
        var menu = menuList.get(menuIndex)
        binding.tvGram.text = gram.toString() + " g"
        binding.tvPerson.text = person.toString() + " 인분"
        // 칼로리 및 영양성분 계산
        var dif = gram / menu.baseNutrition?.gram!! / person
        kcal = (dif * menu.baseNutrition?.energy!!).toInt()
        carbo = (dif * menu.baseNutrition?.carbohydrate!!).toInt()
        protein = (dif * menu.baseNutrition?.protein!!).toInt()
        fat = (dif * menu.baseNutrition?.fat!!).toInt()
        binding.tvCalorie.text = kcal.toString() + " kcal"
        binding.tvCarbo.text = carbo.toString() + " g"
        binding.tvProtein.text = protein.toString() + " g"
        binding.tvFat.text = fat.toString() + " g"
    }
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
            }
            else{
                Log.d("test", x.toString() + " " + width.toString() + " " + y.toString() + " " + height.toString())
                Log.d("test", bitmap.width.toString() + " " + bitmap.height.toString())
            }
            binding.tvMenuResultName.text = menu.fullName!!.split("(")[0]
            var gram = menu.gram.toInt().toString() + " g"
            kcal = menu.energy.toInt()
            carbo = menu.carbohydrate.toInt()
            protein = menu.protein.toInt()
            fat = menu.fat.toInt()
            binding.tvGram.text = gram
            binding.tvCalorie.text = kcal.toString() + " kcal"
            binding.tvCarbo.text = carbo.toString() + " g"
            binding.tvProtein.text = protein.toString() + " g"
            binding.tvFat.text = fat.toString() + " g"
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
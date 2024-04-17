package com.example.capstonedesign.Fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.doinglab.foodlens2.sdk.FoodLens
import com.doinglab.foodlens2.sdk.RecognitionResultHandler
import com.doinglab.foodlens2.sdk.config.LanguageConfig
import com.doinglab.foodlens2.sdk.errors.BaseError
import com.doinglab.foodlens2.sdk.model.FoodInfo
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
        foodLensService?.setAutoRotate(true)
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
                    Toast.makeText(context, menuNum.toString(), Toast.LENGTH_SHORT).show()
                    setView()
                    //detect 된 음식이 없을때의 예외를 처리해야함.
                }
            }

            override fun onError(errorReason: BaseError?) {
                Log.d("FoodLens", errorReason?.getMessage().toString())
                Toast.makeText(context, "error: " + errorReason?.getMessage() ,Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnNext.setOnClickListener {
            if(menuIndex == menuNum - 1 || binding.btnNext.text.equals("확인")){
                var action = ResultFragmentDirections.actionResultFragmentToMainFragment()
                findNavController().navigate(action)
            }
            else{
                setView()
            }
        }
    }
    fun setView(){
        var menu = menuList.get(menuIndex)
        if(menu.name == null){
            menuIndex++
            setView()
        }
        else{
            var x : Int = menu.foodPosition?.xmin!!
            var y : Int = menu.foodPosition?.ymin!!
            var width : Int = menu.foodPosition?.xmax!! - x
            var height : Int = menu.foodPosition?.ymax!! - y
            croppedBitmap = Bitmap.createBitmap(bitmap, x, y, width, height)
            binding.ivMenuImage.setImageBitmap(croppedBitmap)
            binding.tvMenuResultName.text = menu.name
            var calorie = menu.energy.toString() + "kcal"
            var carbo = menu.carbohydrate.toString() + "g"
            var protein = menu.protein.toString() + "g"
            var fat = menu.fat.toString() + "g"
            binding.tvCalorie.text = calorie
            binding.tvCarbo.text = carbo
            binding.tvProtein.text = protein
            binding.tvFat.text = fat
            if(menuIndex == menuNum - 1){
                binding.btnNext.text = "확인"
            }
            else{
                menuIndex++
            }
        }
    }
}
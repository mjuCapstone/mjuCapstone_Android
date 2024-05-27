package com.example.capstonedesign.Fragment

import Data.AddItemData
import Data.SelectData
import Response.AddItemResponse
import Response.SelectResponse
import Service.AddItemService
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.doinglab.foodlens2.sdk.FoodLens
import com.doinglab.foodlens2.sdk.RecognitionResultHandler
import com.doinglab.foodlens2.sdk.config.LanguageConfig
import com.doinglab.foodlens2.sdk.errors.BaseError
import com.doinglab.foodlens2.sdk.model.FoodInfo
import com.doinglab.foodlens2.sdk.model.RecognitionResult
import com.example.capstonedesign.AddItemAdapter
import com.example.capstonedesign.Dialog.LoadingDialog
import com.example.capstonedesign.Dialog.SelectDialog
import com.example.capstonedesign.Dialog.SelectDialogListener
import com.example.capstonedesign.R
import com.example.capstonedesign.RetrofitClient
import com.example.capstonedesign.databinding.FragmentNewResultBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.time.LocalDateTime

class NewResultFragment : Fragment() , SelectDialogListener {
    private lateinit var binding : FragmentNewResultBinding
    private lateinit var bitmap : Bitmap
    private lateinit var croppedBitmap : Bitmap
    private lateinit var list : ArrayList<AddItemData>
    private lateinit var adapter : AddItemAdapter
    var len : Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewResultBinding.bind(inflater.inflate(R.layout.fragment_new_result, container, false))
        return binding.root
    }

    override fun changeMenu(data: SelectData) {
        var index = binding.newResultViewPager.currentItem
        adapter.changeAll(index, data)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //검색을 통한 메뉴 선택 시
        val menuName : String? = arguments?.getString("menuName")
        menuName?.let{
            val addItemService = RetrofitClient.setRetroFitInstanceWithToken(requireContext()).create(
                AddItemService::class.java)
            addItemService.select(it).enqueue(object : retrofit2.Callback<SelectResponse>{
                override fun onResponse(
                    call: Call<SelectResponse>,
                    response: Response<SelectResponse>
                ) {
                    response.body()?.let {
                        Log.d("test", it.message)
                    }
                    if(response.isSuccessful){
                        var result = response.body()!!.data
                        val fileName = result.name + "_" + LocalDateTime.now().toString()
                        Glide.with(requireContext())
                            .asBitmap()
                            .load(result.imgUrl)
                            .into(object : CustomTarget<Bitmap>(){
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: Transition<in Bitmap>?
                                ) {
                                    //생성된 bitmap 파일 로컬 저장소에 저장
                                    requireContext().openFileOutput(fileName, Context.MODE_PRIVATE).use{ fos->
                                        resource.compress(Bitmap.CompressFormat.PNG, 100 , fos)
                                    }
                                    // 파일 경로 얻기
                                    val filePath = File(requireContext().filesDir, fileName).absolutePath
                                    Log.d("test", "Saved file path: $filePath")

                                    list = ArrayList()
                                    list.add(AddItemData(result.name, result.gram, 1, result.kcal, result.carbohydrate, result.protein,
                                        result.fat, fileName))
                                    Log.d("test", list[0].fileName)
                                    adapter = AddItemAdapter(requireContext())
                                    adapter.menuList = list
                                    binding.newResultViewPager.adapter = adapter
                                    binding.resultIndicator.setViewPager(binding.newResultViewPager)
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {
                                    //추후 필요시 구현
                                }

                            })
                    }
                }

                override fun onFailure(call: Call<SelectResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
        //사진 촬영을 통한 메뉴 선택 시
        val uriString = arguments?.getString("photoUri")
        uriString?.let{ str ->
            val photoUri : Uri = Uri.parse(str)

            //Create Network Service
            val foodLensService by lazy{
                context?.let { FoodLens.createFoodLensService(it) }
            }
            foodLensService?.setAutoRotate(false)
            foodLensService?.setLanguage(LanguageConfig.KO)

            //uri to bitmap
            val inputStream = context?.contentResolver?.openInputStream(photoUri!!)
            val byteArray = inputStream?.readBytes()!!
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

            //푸드렌즈 서버로부터 결과 받아오는 동안 loading
            var loadingDialog = LoadingDialog(requireContext())
            loadingDialog.show()

            //결과 처리
            foodLensService?.predict(byteArray, object : RecognitionResultHandler { //If userId does not exist
                override fun onSuccess(result: RecognitionResult?) {
                    result?.let {
                        list = foodInfoToAddItemList(it.foodInfoList)
                        len = list.size
                        if(len == 0){
                            Toast.makeText(context, "메뉴를 찾지 못했습니다.'\n 메뉴를 검색하거나 사진을 다시 선택해주세요.", Toast.LENGTH_SHORT).show()
                            var action = NewResultFragmentDirections.actionNewResultFragmentToInputFragment()
                            findNavController().navigate(action)
                        }
                        adapter = AddItemAdapter(requireContext())
                        adapter.menuList = list
                        binding.newResultViewPager.adapter = adapter
                        binding.resultIndicator.setViewPager(binding.newResultViewPager)
                        loadingDialog.dismiss()
                    }
                }

                override fun onError(errorReason: BaseError?) {
                    loadingDialog.dismiss()
                    Log.d("FoodLens", errorReason?.getMessage().toString())
                    Toast.makeText(context, "error: " + errorReason?.getMessage() , Toast.LENGTH_SHORT).show()
                }
            })
        }
        binding.btnAdjust.setOnClickListener {
            var dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_result)
            val index = binding.newResultViewPager.currentItem
            Log.d("test", "Viewpager 현재 index : " + index.toString())
            var edtGram : EditText = dialog.findViewById(R.id.edtGram)
            edtGram.setText(adapter.menuList[index].amount.toString())
            var edtServing : EditText = dialog.findViewById(R.id.edtServing)
            edtServing.setText(adapter.menuList[index].serving.toString())
            var btnReselect : Button = dialog.findViewById(R.id.btnReselect)
            btnReselect.setOnClickListener {
                dialog.dismiss()
                val selectDialog = SelectDialog()
                selectDialog.show(childFragmentManager , "MyDialogFragment")
            }
            var btnDelete : Button = dialog.findViewById(R.id.btnDelete)
            btnDelete.setOnClickListener {
                adapter.menuList.removeAt(index)
                adapter.notifyItemRemoved(index)
                binding.resultIndicator.setViewPager(binding.newResultViewPager)
                dialog.dismiss()
            }
            var btnConfirm : Button = dialog.findViewById(R.id.btnConfirm)
            btnConfirm.setOnClickListener {
                adapter.changeInfo(index, edtGram.text.toString().toInt(), edtServing.text.toString().toInt())
                dialog.dismiss()
            }
            var btnCancel : Button = dialog.findViewById(R.id.btnCancel)
            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
        binding.btnConfirm.setOnClickListener {
            val callback = object : retrofit2.Callback<AddItemResponse> {
                override fun onResponse(
                    call: Call<AddItemResponse>,
                    response: Response<AddItemResponse>
                ) {
                    if(!response.isSuccessful){
                        Toast.makeText(requireContext(), "메뉴 업로드에 실패하셨습니다", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AddItemResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "메뉴 업로드에 실패하셨습니다", Toast.LENGTH_SHORT).show()
                }

            }
            val addItemService = RetrofitClient.setRetroFitInstanceWithToken(requireContext()).create(AddItemService::class.java)
            for(item in adapter.menuList){
                addItemService.addItem(item).enqueue(callback)
            }
            val action = NewResultFragmentDirections.actionNewResultFragmentToMainFragment()
            findNavController().navigate(action)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun foodInfoToAddItemList(info : ArrayList<FoodInfo>) : ArrayList<AddItemData>{
        var list = ArrayList<AddItemData>()
        for(item in info){
            if(item.name != null){
                var fileName = ""
                var x : Int = item.foodPosition?.xmin!!
                var y : Int = item.foodPosition?.ymin!!
                var width : Int = item.foodPosition?.xmax!! - x
                var height : Int = item.foodPosition?.ymax!! - y
                if(x + width <= bitmap.width && y + height <= bitmap.height) {
                    croppedBitmap = Bitmap.createBitmap(bitmap, x, y, width, height)
                    //생성된 bitmap 파일 로컬 저장소에 저장
                    fileName = item.fullName!!.split("(")[0] + "_" + LocalDateTime.now().toString()
                    requireContext().openFileOutput(fileName, Context.MODE_PRIVATE).use{ fos->
                        croppedBitmap.compress(Bitmap.CompressFormat.PNG, 100 , fos)
                    }
                    // 파일 경로 얻기
                    val filePath = File(requireContext().filesDir, fileName).absolutePath
                    Log.d("test", "Saved file path: $filePath")
                }
                list.add(AddItemData(item.fullName!!.split("(")[0],item.gram.toInt(),
                    1, item.energy.toInt(), item.carbohydrate.toInt(), item.protein.toInt(), item.fat.toInt() , fileName))
            }
        }
        return list
    }
}
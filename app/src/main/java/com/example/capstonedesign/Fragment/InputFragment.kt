package com.example.capstonedesign.Fragment

import Data.MenuItem
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstonedesign.R
import com.doinglab.foodlens2.sdk.FoodLens
import com.doinglab.foodlens2.sdk.RecognitionResultHandler
import com.doinglab.foodlens2.sdk.config.LanguageConfig
import com.doinglab.foodlens2.sdk.errors.BaseError
import com.doinglab.foodlens2.sdk.model.RecognitionResult
import com.example.capstonedesign.MenuRecyclerAdapter
import com.example.capstonedesign.databinding.FragmentInputBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InputFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InputFragment : Fragment() {
    private lateinit var getContent: ActivityResultLauncher<String>
    //var context = requireContext()
    var menuData = mutableListOf<MenuItem>()
    private lateinit var binding : FragmentInputBinding
    private lateinit var adapter :MenuRecyclerAdapter //adapter객체 먼저 선언해주기!
    private lateinit var imagePickerLauncher : ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInputBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        initMenuRecyclerView()
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if(result.resultCode == Activity.RESULT_OK){
                var uri : Uri = result.data?.data!!
                var action = InputFragmentDirections.actionInputFragmentToResultFragment(uri)
                findNavController().navigate(action)
            }
        }
        binding.btnAddPhoto.setOnClickListener {
            // 갤러리에서 선택하는 Intent 생성
            val galleryIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

            // 카메라로 사진을 찍는 Intent 생성
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            // 사용자에게 제공할 Intent 목록을 만듭니다.
            val chooser = Intent.createChooser(galleryIntent, "사진 선택")
            val intentArray = arrayOf(cameraIntent)

            // 카메라 인텐트를 선택 옵션에 추가합니다.
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)

            // 선택기를 시작합니다.
            imagePickerLauncher.launch(chooser)
        }
    }


    fun initMenuRecyclerView(){
        val adapter= MenuRecyclerAdapter() //어댑터 객체 만듦
        Toast.makeText(context, menuData.size.toString() , Toast.LENGTH_SHORT).show()
        adapter.menuList = menuData //데이터 넣어줌
        binding.menuRecyclerView.adapter = adapter
        binding.menuRecyclerView.layoutManager=LinearLayoutManager(context)
    }


    fun initList(){
        with(menuData){
            add(MenuItem(R.drawable.img_food, "김치볶음밥", true))
            add(MenuItem(R.drawable.img_food, "볶음밥", false))
            add(MenuItem(R.drawable.img_food, "참치볶음밥", false))
            add(MenuItem(R.drawable.img_food, "김치찌개", true))
            add(MenuItem(R.drawable.img_food, "스테이크", true))
            add(MenuItem(R.drawable.img_food, "비빔밥", false))
            add(MenuItem(R.drawable.img_food, "고기국수", true))
            add(MenuItem(R.drawable.img_food, "육회", true))
        }
    }
}
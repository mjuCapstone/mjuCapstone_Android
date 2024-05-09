package com.example.capstonedesign.Fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.capstonedesign.R
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
    private lateinit var binding : FragmentInputBinding
    private lateinit var imagePickerLauncher : ActivityResultLauncher<Intent>
    private lateinit var brandMenuMap : HashMap<Int,java.util.ArrayList<String>>
    private lateinit var menuList : java.util.ArrayList<String>
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
        brandMenuMap = HashMap()
        var lotteia = arrayListOf("불고기버거","치즈버거","새우버거")
        var macdonald = arrayListOf("빅맥","불고기버거","핫크리스피버거","1955버거")
        var burgerking = arrayListOf("통새우와퍼","몬스터와퍼","불고기버거","통모짜와퍼")
        var momstouch = arrayListOf("싸이버거","불싸이버거","불고기버거","아라비아따치즈버거")
        brandMenuMap.put(R.id.lotteria,lotteia)
        brandMenuMap.put(R.id.macdonald,macdonald)
        brandMenuMap.put(R.id.burgerking,burgerking)
        brandMenuMap.put(R.id.momstouch, momstouch)
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if(result.resultCode == Activity.RESULT_OK){
                var uri : Uri = result.data?.data!!
                var action = InputFragmentDirections.actionInputFragmentToResultFragment(uri)
                findNavController().navigate(action)
            }
        }
        binding.btnSelectBrand.setOnClickListener {
            var popupMenu = PopupMenu(requireContext(), it)
            var applicationContext : AppCompatActivity = context as AppCompatActivity
            applicationContext.menuInflater.inflate(R.menu.menu_brand, popupMenu.menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener {
                if(brandMenuMap.containsKey(it.itemId)){
                    menuList = brandMenuMap.get(it.itemId)!!
                    binding.tvBrandEnter.visibility = TextView.GONE
                    binding.menuListView.visibility = ListView.VISIBLE
                    binding.btnSelectBrand.text = it.title
                    var arrayAdapter : ArrayAdapter<String> = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, menuList)
                    binding.menuListView.adapter = arrayAdapter
                    binding.menuListView.deferNotifyDataSetChanged()
                    return@setOnMenuItemClickListener true
                }
                return@setOnMenuItemClickListener false
            }
        }
        binding.btnSearch.setOnClickListener {

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
}
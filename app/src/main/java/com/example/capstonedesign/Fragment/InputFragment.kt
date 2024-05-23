package com.example.capstonedesign.Fragment

import Response.SearchResponse
import Service.AddItemService
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.capstonedesign.R
import com.example.capstonedesign.RetrofitClient
import com.example.capstonedesign.databinding.FragmentInputBinding
import com.github.mikephil.charting.BuildConfig
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    private lateinit var menuList : java.util.ArrayList<String>
    private lateinit var bundle : Bundle
    private lateinit var photoFile: File
    private lateinit var photoURI: Uri
    private val cameraPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            openChooser()

        } else {
            Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_LONG).show()
        }

    }

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
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if(result.resultCode == Activity.RESULT_OK){
                var uri : Uri? = result.data?.data
                uri?.let {
                    bundle = Bundle().apply {
                        putString("photoUri", it.toString())
                    }
                    findNavController().navigate(R.id.action_inputFragment_to_resultFragment, bundle)
                }
            }
        }
        binding.btnSearch.setOnClickListener {
           var text = binding.edtSearch.text.toString()
            if(text.equals("")){
                Toast.makeText(
                    requireContext(), "메뉴명을 입력해주세요", Toast.LENGTH_SHORT
                ).show()
            }
            else{
                //검색 요청
                val addItemService = RetrofitClient.setRetroFitInstanceWithToken(requireContext()).create(
                    AddItemService::class.java)
                addItemService.search(text).enqueue(object : retrofit2.Callback<SearchResponse>{
                    override fun onResponse(
                        call: Call<SearchResponse>,
                        response: Response<SearchResponse>
                    ) {
                        if(response.isSuccessful) {
                            menuList = ArrayList(response.body()!!.data)
                            if(menuList.size != 0) {
                                var arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
                                    requireContext(),
                                    android.R.layout.simple_list_item_1,
                                    menuList
                                )
                                binding.tvBrandEnter.visibility = TextView.GONE
                                binding.menuListView.visibility = ListView.VISIBLE
                                binding.menuListView.adapter = arrayAdapter
                                binding.menuListView.deferNotifyDataSetChanged()
                                binding.menuListView.setOnItemClickListener { adapterView, view, i, l ->
                                    bundle = Bundle().apply {
                                        putString("menuName", menuList.get(i))
                                    }
                                    findNavController().navigate(R.id.action_inputFragment_to_resultFragment, bundle)
                                }
                            }
                            else{
                                Toast.makeText(requireContext(), "검색 결과가 존재하지 않습니다.", Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        else{
                            Log.d("test", response.errorBody().toString())
                            Toast.makeText(requireContext(), "검색에 실패하였습니다." , Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                        Toast.makeText(requireContext(), "검색에 실패하였습니다." , Toast.LENGTH_SHORT).show()
                    }

                })

            }
        }
        binding.btnAddPhoto.setOnClickListener {
            cameraPermissionRequest.launch(Manifest.permission.CAMERA)
        }
    }

    private fun openChooser() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            resolveActivity(requireContext().packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                }
            }
        }
        val chooser = Intent.createChooser(galleryIntent, "사진 선택")
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
        imagePickerLauncher.launch(chooser)
    }
    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return try {
            File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                storageDir
            ).apply {
                photoURI = FileProvider.getUriForFile(requireContext(), "${com.example.capstonedesign.BuildConfig.APPLICATION_ID}.fileprovider", this)
            }
        } catch (ex: IOException) {
            null
        }
    }
}
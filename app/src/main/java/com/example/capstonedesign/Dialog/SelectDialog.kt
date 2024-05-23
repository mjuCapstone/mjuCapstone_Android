package com.example.capstonedesign.Dialog

import Response.SearchResponse
import Response.SelectResponse
import Service.AddItemService
import android.R
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.capstonedesign.Fragment.ResultFragment
import com.example.capstonedesign.RetrofitClient
import com.example.capstonedesign.databinding.FragmentInputBinding
import retrofit2.Call
import retrofit2.Response
import java.lang.ClassCastException

class SelectDialog : DialogFragment() {
    private var listener: SelectDialogListener? = null
    private lateinit var binding : FragmentInputBinding
    private lateinit var menuList : java.util.ArrayList<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // MyFragment의 레이아웃을 재사용합니다
        binding = FragmentInputBinding.inflate(inflater, container, false)
        binding.btnAddPhoto.visibility = ImageButton.GONE
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            Log.d("test", "Parent Fragment: $parentFragment")
            Log.d("test", "Context: $context")
            listener = when{
                parentFragment is SelectDialogListener -> parentFragment as SelectDialogListener
                context is SelectDialogListener -> context as SelectDialogListener
                else -> throw ClassCastException("$context must implement DialogFragmentListener")
            }
        }catch(e : ClassCastException){
            Toast.makeText(requireContext(), "Cast Error", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        binding.btnSearch.setOnClickListener {
            var text = binding.edtSearch.text.toString()
            if(text.equals("")){
                Toast.makeText(requireContext(), "음식 이름을 입력해주세요", Toast.LENGTH_SHORT).show()
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
                                    R.layout.simple_list_item_1,
                                    menuList
                                )
                                binding.tvBrandEnter.visibility = TextView.GONE
                                binding.menuListView.visibility = ListView.VISIBLE
                                binding.menuListView.adapter = arrayAdapter
                                binding.menuListView.deferNotifyDataSetChanged()
                                binding.menuListView.setOnItemClickListener { adapterView, view, i, l ->
                                    var menuName = menuList.get(i)
                                    addItemService.select(menuName).enqueue(object : retrofit2.Callback<SelectResponse>{
                                        override fun onResponse(
                                            call: Call<SelectResponse>,
                                            response: Response<SelectResponse>
                                        ) {
                                            if(response.isSuccessful){
                                                val selectData = response.body()!!.data
                                                listener?.changeMenu(selectData)
                                            }
                                            else{
                                                Toast.makeText(requireContext(), "메뉴 선택에 실패하였습니다", Toast.LENGTH_SHORT).show()
                                            }
                                            dismiss()
                                        }

                                        override fun onFailure(
                                            call: Call<SelectResponse>,
                                            t: Throwable
                                        ) {
                                            Toast.makeText(requireContext(), "메뉴 선택에 실패하였습니다", Toast.LENGTH_SHORT).show()
                                            dismiss()
                                        }

                                    })
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
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        return dialog
    }
}
package com.example.capstonedesign.Fragment

import Service.HistoryService
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.PopupWindow
import com.example.capstonedesign.R
import com.example.capstonedesign.RetrofitClient
import com.example.capstonedesign.databinding.FragmentRecoBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentRecoBinding
    var category : String? = null
    var className : String? = null
    var nutritionRange : String? = null
    var listCategory = listOf("한식","양식","중식")
    var listClass = listOf("밥류","구이류","국 및 탕류","면 및 만두류","볶음류","빵 및 과자류","전 적 및 부침류",
        "죽 및 스프류", "찌개 및 전골류","찜류", "튀김류")
    var listNutritionRagnge = listOf("아침","점심","저녁","사용자 설정")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecoBinding.bind(inflater.inflate(R.layout.fragment_reco,container,false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var popupView = requireActivity().layoutInflater.inflate(R.layout.popupwindow, null)
        var listView = popupView.findViewById<ListView>(R.id.popupWindowListView)
        var popupWindow : PopupWindow
        binding.btnCategory.setOnClickListener {
            popupWindow = PopupWindow(
                popupView,
                binding.btnCategory.width,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
            )
            var categoryAdapter = ArrayAdapter(requireActivity(), R.layout.item_popupwindow, R.id.popupWindowItem, listCategory)
            listView.adapter = categoryAdapter
            listView.setOnItemClickListener{ adapterView, view, position, id ->
                binding.btnCategory.text = categoryAdapter.getItem(position).toString()
                popupWindow.dismiss()
            }
            popupWindow.showAsDropDown(binding.btnCategory)
        }
        binding.btnClass.setOnClickListener {
            popupWindow = PopupWindow(
                popupView,
                binding.btnClass.width,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
            )
            var classAdapter = ArrayAdapter(requireActivity(), R.layout.item_popupwindow, R.id.popupWindowItem, listClass)
            listView.adapter = classAdapter
            listView.setOnItemClickListener{ adapterView, view, position, id ->
                binding.btnClass.text = classAdapter.getItem(position).toString()
                popupWindow.dismiss()
            }
            popupWindow.showAsDropDown(binding.btnClass)

        }
        binding.btnNutritionRange.setOnClickListener {
            popupWindow = PopupWindow(
                popupView,
                binding.btnNutritionRange.width,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
            )
            var nutritionRangeAdapter = ArrayAdapter(requireActivity(), R.layout.item_popupwindow, R.id.popupWindowItem, listNutritionRagnge)
            listView.adapter = nutritionRangeAdapter
            listView.setOnItemClickListener{ adapterView, view, position, id ->
                binding.btnNutritionRange.text = nutritionRangeAdapter.getItem(position).toString()
                if(binding.btnNutritionRange.text.equals("사용자 설정")){
                    //영양성분 범위를 설정하기 위한 dialog
                }
                popupWindow.dismiss()
            }
            popupWindow.showAsDropDown(binding.btnNutritionRange)
        }
    }

    fun setRecommendList(){
        var historyService = RetrofitClient.setRetroFitInstanceWithToken(requireContext()).create(
            HistoryService::class.java
        )
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RecoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
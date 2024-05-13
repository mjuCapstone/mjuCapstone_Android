package com.example.capstonedesign.Fragment

import Data.MenuItem
import Data.PrefItem
import Data.HistoryInfo
import Response.AutoLoginResponse
import Response.HistoryResponse
import Service.AutoLoginService
import Service.HistoryService
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstonedesign.Activity.StartActivity
import com.example.capstonedesign.MenuAdapter
import com.example.capstonedesign.PrefAdapter
import com.example.capstonedesign.R
import com.example.capstonedesign.RetrofitClient
import com.example.capstonedesign.databinding.FragmentHistoryBinding
import com.example.capstonedesign.databinding.FragmentInputBinding
import com.example.capstonedesign.databinding.FragmentPrefBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentHistoryBinding
    lateinit var menuDataList : MutableList<MenuItem>

    private lateinit var adapter : MenuAdapter //adapter객체 먼저 선언해주기!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    fun setnowDate() : String {
        val nowDate : String
        val calendar = Calendar.getInstance() // 현재 날짜와 시간을 얻음
        val dateFormat = SimpleDateFormat("YYYY.MM.dd", Locale.getDefault())
        nowDate = dateFormat.format(calendar.time)
        return nowDate
    }


    fun showDatePicker(){
        val calendar: Calendar = Calendar.getInstance()

        val iYear = calendar.get(Calendar.YEAR)
        val iMonth = calendar.get(Calendar.MONTH)
        val iDay = calendar.get(Calendar.DAY_OF_MONTH)

        // 달력 호출
        val datePicker: DatePickerDialog = DatePickerDialog(requireContext(),
            DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                // 1월은 0부터 시작해서 +1
                val tMonth: Int = month + 1
                val date: String = "$year.$tMonth.$day"
                // 화면에 선택한 날짜 보여주기
                binding.historyToday.text = date
                fetchDataFromServer(date)

            }, iYear, iMonth, iDay
        )
        datePicker.show()
    }

    fun fetchDataFromServer(date: String){
        val historyService = RetrofitClient.setRetroFitInstanceWithToken(requireContext()).create(
            HistoryService::class.java)
        historyService.history(date).enqueue(object : retrofit2.Callback<HistoryResponse> {
            override fun onResponse(
                call: Call<HistoryResponse>,
                response: Response<HistoryResponse>
            ) {
                if (response.isSuccessful) {
                    // 응답으로부터 HistoryResponse 객체를 가져옴
                    val historyResponse = response.body()
                    // HistoryResponse 객체가 null이 아니고, data 필드에 MenuItem 리스트가 있다면
                    historyResponse?.data?.let {
                        menuDataList = it.toMutableList()
                        initMenuRecyclerView()
                    } ?: run {
                        // data가 null인 경우에 여기서 무슨 화면을 보여줄 것인지
                    }
                } else {
                    // 요청이 성공적으로 처리되지 않은 경우의 처리
                }
            }

            override fun onFailure(call: retrofit2.Call<HistoryResponse>, t: Throwable) {
                // 네트워크 요청 실패 처리 (예: 로깅)
            }
        })
    }

    fun initMenuRecyclerView(){
        val adapter= MenuAdapter(requireContext()) //어댑터 객체 만듦
        adapter.menuList = menuDataList //데이터 넣어줌
        binding.hitoryRecyclerView.adapter = adapter
        binding.hitoryRecyclerView.layoutManager= LinearLayoutManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 현재 날짜를 가져와서
        val nowDate: String = setnowDate()
        // history_today에 설정해서 보여줌
        binding.historyToday.text = nowDate

        // 버튼 클릭 이벤트
        binding.historyCalendar.setOnClickListener{
            showDatePicker()
        }
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
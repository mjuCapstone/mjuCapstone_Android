package com.example.capstonedesign.Fragment

import Data.MenuItem
import Data.PrefItem
import Data.HistoryInfo
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstonedesign.MenuAdapter
import com.example.capstonedesign.PrefAdapter
import com.example.capstonedesign.R
import com.example.capstonedesign.RetrofitClient
import com.example.capstonedesign.databinding.FragmentHistoryBinding
import com.example.capstonedesign.databinding.FragmentPrefBinding
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
    lateinit var dateText: TextView



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
                dateText.text = date
            }, iYear, iMonth, iDay)

        datePicker.show()

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*
        // 현재 날짜를 가져와서
        var nowDate : String = setnowDate()
        // history_today에 설정해서 보여줌
        var textViewDate: TextView = view.findViewById(R.id.history_today)
        textViewDate.text = nowDate
         */

        dateText = view.findViewById(R.id.history_today)
        val dateBtn: Button = view.findViewById(R.id.history_calendar)

        // 버튼 클릭 이벤트
        dateBtn.setOnClickListener {
            // 달력 보여주기
            showDatePicker()
        }



        // initList()
        // initHistoryRecyclerView()
    }

    /*
    fun initHistoryRecyclerView(){
        val adapter = MenuAdapter(requireContext()) //어댑터 객체 만듦
        adapter.menuList = menuhistoryData //데이터 넣어줌
        // 서버에 데이터 요청하는 메소드를 써여됨 -> 그래서 메뉴 리스트를 받아와서
        binding.hitory_recyclerView.adapter = adapter
        binding.hitory_recyclerView.layoutManager= LinearLayoutManager(context)

        var historyData = HistoryInfo()
        var loginService = RetrofitClient.retrofitInstance.create(LoginService::class.java)
        var intent = Intent(this, MainActivity::class.java)
        loginService.login(loginData).enqueue(object : retrofit2.Callback<LoginResponse>{
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                Log.d("test", loginData.toString())
                /*TODO("메인화면 진입 후 뒤로가기 버튼을 눌렀을 때 " +
                        "어플리케이션을 종료할지 물어볼 것인지 아니면 로그인 화면으로 돌아갈 것인지" +
                        "정하기")*/
                //토큰 처리 메소드
                when(response.code()){
                    200 -> {
                        response.body()?.let{
                            var accessToken = getCurrentToken()
                            var successData = it.data
                            if(accessToken != successData.accessToken){
                                Log.d("test", "토큰 다름")
                                var editor = preferences.edit()
                                editor.putString("token", successData.accessToken)
                                editor.apply()
                            }
                            else{
                                Log.d("test", "토큰 같음")
                            }
                            startActivity(intent)
                            finish()
                        }
                    }
                    else ->{
                        print("로그인에 실패하셨습니다.")
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("errorMsg", t.message.toString())
                print(t.message.toString())
            }
        })
    }
     */

    /*

    fun initList(){
        with(menuData){
            add(PrefItem(com.example.capstonedesign.R.drawable.img_food, "김치볶음밥", true))
            add(PrefItem(com.example.capstonedesign.R.drawable.img_food, "볶음밥", false))
            add(PrefItem(com.example.capstonedesign.R.drawable.img_food, "참치볶음밥", false))
            add(PrefItem(com.example.capstonedesign.R.drawable.img_food, "김치찌개", true))
            add(PrefItem(com.example.capstonedesign.R.drawable.img_food, "스테이크", true))
            add(PrefItem(com.example.capstonedesign.R.drawable.img_food, "비빔밥", false))
            add(PrefItem(com.example.capstonedesign.R.drawable.img_food, "고기국수", true))
            add(PrefItem(com.example.capstonedesign.R.drawable.img_food, "육회", true))
        }
    }
    */

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
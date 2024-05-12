package com.example.capstonedesign.Fragment

import Data.GoalNutritionInfo
import Data.HistoryInfo
import Response.AutoLoginResponse
import Service.AutoLoginService
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import com.example.capstonedesign.Activity.StartActivity
import com.example.capstonedesign.R
import com.example.capstonedesign.RetrofitClient
import com.example.capstonedesign.databinding.FragmentMainBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import retrofit2.Call
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
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentMainBinding

    lateinit var pieChart : PieChart
    lateinit var barChart : BarChart
    lateinit var info: GoalNutritionInfo
    lateinit var historyList : Map<String,HistoryInfo>
    // 그래프 색상(데이터 순서)
    val colors = listOf(
        Color.parseColor("#E18610"),
        Color.parseColor("#E8E8E8")
    )
    var dietPlanList = listOf(null, "빠른 체중 감소", "느린 체중 감소","체중 유지","느린 체중 증가","빠른 체중 증가")

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("앱을 종료하시겠습니까?")
            .setPositiveButton("예") { dialog, which ->
                // Positive Button이 클릭되었을 때 앱을 종료합니다.
                activity?.moveTaskToBack(true)
                activity?.finishAndRemoveTask()
                android.os.Process.killProcess(android.os.Process.myPid())
            }
            .setNegativeButton("아니오", null)
            .show()
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        // onBackPressedCallback을 생성하고 활성화합니다.
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 사용자에게 종료 여부를 묻는 dialog를 보여주기
                showExitConfirmationDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    // 일주일 날짜 가져오는 함수
    fun getWeekDates(): Array<String?> {
        val weekDate = arrayOfNulls<String>(7) // 7일간의 날짜를 저장할 배열 생성
        val calendar = Calendar.getInstance() // 현재 날짜와 시간을 얻음

        // 어제 날짜로 설정
        calendar.add(Calendar.DATE, -1)

        val dateFormat = SimpleDateFormat("MM/dd", Locale.getDefault())
        // 날짜 형식 지정

        // 어제부터 8일 전까지 날짜를 배열에 저장
        for (i in 0 until 7) {
            weekDate[i] = dateFormat.format(calendar.time) // 현재 설정된 날짜를 배열에 저장
            calendar.add(Calendar.DATE, -1) // 하루를 뺌
        }
        return weekDate
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun setPieChart(goal_calo : Float , intake_calo : Float){
        // PieChart
        pieChart = requireView().findViewById(R.id.today_kcal)

        val entries = ArrayList<PieEntry>()
        var remain = 0f
        if(intake_calo < goal_calo){
            remain = goal_calo - intake_calo
        }
        entries.add(PieEntry(intake_calo, ""))
        entries.add(PieEntry(remain , ""))



        // 데이터, 색상, 글자크기 및 색상 설정
        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors
        dataSet.valueTextSize = 0f
        // Pie 그래프 생성
        val dataMPchart = PieData(dataSet)
        pieChart.data = dataMPchart
        // 중앙 텍스트를 설정하여 섭취량 표시
        pieChart.centerText =
            String.format("%d / %d\n kcal", intake_calo.toInt(), goal_calo.toInt())
        pieChart.setCenterTextSize(15f)
        val typeface: Typeface? = ResourcesCompat.getFont(requireContext(), R.font.font)
        pieChart.setCenterTextTypeface(typeface)
        pieChart.setCenterTextColor(Color.rgb(225,134,16))
        pieChart.holeRadius = 95f
        pieChart.setHoleColor(Color.TRANSPARENT)
        // 범례와 그래프 설명 비활성화
        pieChart.legend.isEnabled = false
        pieChart.description.isEnabled = false
        // 그래프 업데이트
        pieChart.invalidate()
        pieChart.animateY(2000)
    }

    fun setBarChart(index : Int){
        // BarChart
        barChart = requireView().findViewById(R.id.weekly_info)

        // 각 영양소들의 값
        val wentries = ArrayList<BarEntry>()

        // 라벨
        val wlabels = ArrayList<String>()

        // 어제부터 8일전까지의 날짜
        val currentDate: Array<String?> = getWeekDates()

        val tot_nutrition = ArrayList<Float>()

        when(index){
            0 -> {
            // historyList["1"] 부터 historyList["7"] 까지의 tot_kcal
                for (i in 1..7) {
                    // historyList에서 해당 키의 객체를 가져와서 tot_kcal 값을 test_tot_kcal에 추가
                    // tot_kcal이 null이 아닌 경우에만 toFloat()을 호출하여 ArrayList에 추가
                    historyList[i.toString()]?.tot_kcal?.toFloat()?.let {
                        tot_nutrition.add(it)
                    }
                }

                for (i in 0 until 7) {
                    // i를 사용하여 test_tot_kcal에서 필요한 값을 가져온 후,
                    // BarEntry의 x 위치를 결정하기 위해 (test_tot_kcal.size - 1 - i).toFloat()를 사용합니다.
                    wentries.add(BarEntry((6-i).toFloat(), tot_nutrition[i]))
                }
            }

            1 -> {
            // historyList["1"] 부터 historyList["7"] 까지의 tot_kcal
                for (i in 1..7) {
                // historyList에서 해당 키의 객체를 가져와서 tot_kcal 값을 test_tot_kcal에 추가
                // tot_kcal이 null이 아닌 경우에만 toFloat()을 호출하여 ArrayList에 추가
                    historyList[i.toString()]?.tot_carbohydrate?.toFloat()?.let {
                        tot_nutrition.add(it)
                    }
                }

                for (i in 0 until 7) {
                    // i를 사용하여 test_tot_kcal에서 필요한 값을 가져온 후,
                    // BarEntry의 x 위치를 결정하기 위해 (test_tot_kcal.size - 1 - i).toFloat()를 사용합니다.
                    wentries.add(BarEntry((6-i).toFloat(), tot_nutrition[i]))
                }
            }

            2 -> {
                // historyList["1"] 부터 historyList["7"] 까지의 tot_kcal
                for (i in 1..7) {
                    // historyList에서 해당 키의 객체를 가져와서 tot_kcal 값을 test_tot_kcal에 추가
                    // tot_kcal이 null이 아닌 경우에만 toFloat()을 호출하여 ArrayList에 추가
                    historyList[i.toString()]?.tot_protein?.toFloat()?.let {
                        tot_nutrition.add(it)
                    }
                }

                for (i in 0 until 7) {
                    // i를 사용하여 test_tot_kcal에서 필요한 값을 가져온 후,
                    // BarEntry의 x 위치를 결정하기 위해 (test_tot_kcal.size - 1 - i).toFloat()를 사용합니다.
                    wentries.add(BarEntry((6 - i).toFloat(), tot_nutrition[i]))
                }
            }

            3 -> {// historyList["1"] 부터 historyList["7"] 까지의 tot_kcal
                for (i in 1..7) {
                    // historyList에서 해당 키의 객체를 가져와서 tot_kcal 값을 test_tot_kcal에 추가
                    // tot_kcal이 null이 아닌 경우에만 toFloat()을 호출하여 ArrayList에 추가
                    historyList[i.toString()]?.tot_fat?.toFloat()?.let {
                        tot_nutrition.add(it)
                    }
                }

                for (i in 0 until 7) {
                    // i를 사용하여 test_tot_kcal에서 필요한 값을 가져온 후,
                    // BarEntry의 x 위치를 결정하기 위해 (test_tot_kcal.size - 1 - i).toFloat()를 사용합니다.
                    wentries.add(BarEntry((6 - i).toFloat(), tot_nutrition[i]))
                }
            }
        }

        for (i in 6 downTo 0) {
            wlabels.add(currentDate[i]!!) // 현재 설정된 날짜를 배열에 저장
        }

        val wdataSet = BarDataSet(wentries, "Bar Chart")

        wdataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)

        val wdata = BarData(wdataSet)
        wdata.barWidth = 0.3f
        wdata.setValueTextSize(6f)

        barChart.data = wdata

        barChart.axisLeft.axisMinimum = 0f

        val wxAxis = barChart.xAxis
        wxAxis.position = XAxis.XAxisPosition.BOTTOM

        wxAxis.setDrawGridLines(false) // hides vertical grid line
        wxAxis.labelCount = wlabels.size // ensures all labels are visible
        wxAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()

                if (index >= 0 && index < wlabels.size) {
                    return wlabels[index]
                }
                return ""

            }
        }

        barChart.animateY(500) // this animates the graph

        barChart.xAxis.setDrawGridLines(false)
        barChart.xAxis.setDrawAxisLine(false)

        barChart.axisLeft.setDrawTopYLabelEntry(false)
        barChart.axisLeft.setDrawLimitLinesBehindData(false)
        barChart.axisLeft.setDrawAxisLine(false)
        barChart.axisLeft.setDrawZeroLine(false)

        // 차트 축 비활성화 및 범례와 설명 비활성화
        barChart.axisLeft.isEnabled = false
        barChart.axisRight.isEnabled = false
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false

        barChart.enableScroll()
    }

    fun setNutriButton(){
        var nutriInfo : Int = -1 // 1 : KCAL 2 : 탄수화물 , 3 : 단백질 4 : 지방
        var btnnutriInfo : Button = requireView().findViewById(R.id.btnNutrition)
        btnnutriInfo.setOnClickListener {
            var popupMenu = PopupMenu(requireContext(), it)
            requireActivity().menuInflater?.inflate(R.menu.main_nutrition, popupMenu.menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.main_kcal -> {
                        btnnutriInfo.setText("KCAL")
                        setBarChart(0)
                        return@setOnMenuItemClickListener true
                    }
                    R.id.main_carbo -> {
                        btnnutriInfo.setText("탄수화물")
                        setBarChart(1)
                        return@setOnMenuItemClickListener true

                    }
                    R.id.main_protein -> {
                        btnnutriInfo.setText("단백질")
                        setBarChart(2)
                        return@setOnMenuItemClickListener true

                    }
                    R.id.main_fat -> {
                        btnnutriInfo.setText("지방")
                        setBarChart(3)
                        return@setOnMenuItemClickListener true

                    }
                    else ->{
                        return@setOnMenuItemClickListener false
                    }
                }
            }
        }
    }





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //main API 호출
        var intent : Intent = Intent(requireActivity(), StartActivity::class.java)
        val autoLoginService = RetrofitClient.setRetroFitInstanceWithToken(requireActivity().applicationContext).create(
            AutoLoginService::class.java)
        autoLoginService.autoLogin().enqueue(object : retrofit2.Callback<AutoLoginResponse>{
            override fun onResponse(
                call: Call<AutoLoginResponse>,
                response: Response<AutoLoginResponse>
            ) {
                if(response.isSuccessful) {
                    response.body()?.let {
                        Log.d("test" , it.status.toString() + " " + it.message)
                        Log.d("test", it.data.toString())
                        Log.d("test", it.data.historyInfoList.toString())
                        info = it.data.goalNutritionInfo
                        historyList = it.data.historyInfoList
                        binding.nickName.text = info.userName
                        binding.sucWeight.text = dietPlanList[info.dietPlan]
                        Log.d("text", historyList["0"].toString())
                        historyList["0"].let {
                            setPieChart(info.kcal.toFloat(), it!!.tot_kcal.toFloat())
                            binding.pbCarbo.setProgressBar(it!!.tot_carbohydrate, info.carbohydrate)
                            binding.pbProtein.setProgressBar(it!!.tot_protein, info.protein)
                            binding.pbFat.setProgressBar(it!!.tot_fat , info.fat)
                        }
                        setBarChart(0)
                        setNutriButton()
                    }
                }
                else{
                    Toast.makeText(requireActivity().applicationContext, "로그인 세션이 만료되었습니다. 다시 로그인 하세요", Toast.LENGTH_SHORT).show()
                    requireActivity().startActivity(intent)
                    requireActivity().finish()
                }
            }

            override fun onFailure(call: Call<AutoLoginResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
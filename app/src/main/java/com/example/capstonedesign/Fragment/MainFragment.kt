package com.example.capstonedesign.Fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import com.example.capstonedesign.R
import com.example.capstonedesign.databinding.FragmentInputBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

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

    private lateinit var binding: FragmentInputBinding

    lateinit var pieChart : PieChart
    lateinit var barChart : BarChart
    // lateinit var horizontalBarChart: HorizontalBarChart

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
        return inflater.inflate(R.layout.fragment_main, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // PieChart

        // 데이터 넣어야 하는 부분
        val goal_calo = 200f
        val intake_calo = 130f
        pieChart = view.findViewById(R.id.today_kcal)

        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(intake_calo, ""))
        entries.add(PieEntry(goal_calo - intake_calo, ""))

        // 그래프 색상(데이터 순서)
        val colors = listOf(
            Color.parseColor("#E18610"),
            Color.parseColor("#E8E8E8")
        )

        // 데이터, 색상, 글자크기 및 색상 설정
        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors
        dataSet.valueTextSize = 10F
        dataSet.valueTextColor = R.color.black
        // Pie 그래프 생성
        val dataMPchart = PieData(dataSet)
        pieChart.data = dataMPchart
        // 중앙 텍스트를 설정하여 섭취량 표시
        pieChart.centerText =
            String.format("칼로리 \n%.1f kcal / %.1f kacl", intake_calo, goal_calo)
        pieChart.setCenterTextSize(8f)
        // pieChart.radius = 200f
        // 범례와 그래프 설명 비활성화
        pieChart.legend.isEnabled = false
        pieChart.description.isEnabled = false
        // 그래프 업데이트
        pieChart.invalidate()
        pieChart.animateY(2000)



        // 주간 영양소 BarChart

        barChart = view.findViewById(R.id.weekly_info)

        val wentries = ArrayList<BarEntry>()
        val wlabels = ArrayList<String>()

        wentries.add(BarEntry(0f, 6f))
        wentries.add(BarEntry(1f, 2f))
        wentries.add(BarEntry(2f, 3.5f))
        wentries.add(BarEntry(3f, 3f))
        wentries.add(BarEntry(4f, 4f))
        wentries.add(BarEntry(5f, 5f))
        wentries.add(BarEntry(6f, 5f))

        wlabels.add("Mon")
        wlabels.add("Tue")
        wlabels.add("Wed")
        wlabels.add("Thu")
        wlabels.add("Fri")
        wlabels.add("Sat")
        wlabels.add("Sun")

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

        barChart.axisLeft.isEnabled = false
        barChart.axisRight.isEnabled = false
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false

        barChart.enableScroll()



        // 상단 오른쪽 BarChart

        barChart = view.findViewById(R.id.daily_info)
        val hentries = ArrayList<BarEntry>()
        val hlabels = ArrayList<String>()
        val hcolors = ArrayList<Int>()

        hentries.add(BarEntry(0f, 6f))
        hentries.add(BarEntry(1f, 2f))
        hentries.add(BarEntry(2f, 3.5f))

        hlabels.add("Carbo")
        hlabels.add("Protein")
        hlabels.add("Fat")

        hcolors.add(Color.parseColor("#896687"))
        hcolors.add(Color.parseColor("#897987"))
        hcolors.add(Color.parseColor("#822987"))

        val hdataSet = BarDataSet(hentries, "Bar Chart")
        hdataSet.colors = colors

        val hdata = BarData(hdataSet)
        hdata.barWidth = 0.3f
        hdata.setValueTextSize(6f)

        barChart.data = hdata
        barChart.axisLeft.axisMinimum = 0f

        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.labelCount = hlabels.size

        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()

                if (index >= 0 && index < hlabels.size) {
                    return hlabels[index]
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

        barChart.axisLeft.isEnabled = false
        barChart.axisRight.isEnabled = false
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false

        barChart.enableScroll()

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
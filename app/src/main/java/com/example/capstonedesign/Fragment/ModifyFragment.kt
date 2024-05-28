package com.example.capstonedesign.Fragment

import Data.UserData
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.example.capstonedesign.R
import com.example.capstonedesign.databinding.FragmentModifyBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ModifyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ModifyFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentModifyBinding

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
        binding = FragmentModifyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //유저 정보를 불러와 뷰 업데이트
        val data = UserData("kiatae0722@naver.com", "ptwmju2199@", "박태우", 170,69, "male", 2000, 1 , 3)
        setView(data)
    }
    fun setView(data : UserData){
        val dietPlan = listOf("빠른 체중 감소", "느린 체중 감소", "체중 유지", "느린 체중 증가", "빠른 체중 감소")
        val activeLevel = listOf(R.id.rbHigh, R.id.rbMid, R.id.rbLow)
        binding.tvNickName.setText(data.nickname)
        binding.edtPassword.setText(data.password)
        binding.edtPasswordConfirm.setText(data.password)
        binding.edtHeight.setText(data.height.toString())
        binding.edtWeight.setText(data.weight.toString())
        if(data.gender.equals("male")){
            binding.rgGender.check(R.id.rbMale)
        }
        else{
            binding.rgGender.check(R.id.rbFemale)
        }
        binding.rgActiveLevel.check(activeLevel[data.level])
        //출생년도 yearPicker 설정
        binding.yearPicker.minValue = 1900
        binding.yearPicker.maxValue = 2023
        binding.yearPicker.value = data.birth
        binding.yearPicker.wrapSelectorWheel = true
        binding.yearPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        val scale = resources.displayMetrics.density
        binding.yearPicker.layoutParams = binding.yearPicker.layoutParams.apply {
            this.height = (90 * scale + 0.5f).toInt() // 예시로 50dp를 사용합니다.
        }
        binding.btnDietPlan.setText(dietPlan[data.dietPlan])
    }
}
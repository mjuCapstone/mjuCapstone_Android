package com.example.capstonedesign.Fragment

import Data.UserData
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.capstonedesign.Activity.StartActivity
import com.example.capstonedesign.R
import com.example.capstonedesign.databinding.FragmentSettingBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentSettingBinding
    var isPasswordVisible = false

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
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //유저 정보를 불러와 뷰 업데이트
        val data = UserData("kiatae0722@naver.com", "ptwmju2199@", "박태우", 170,69, "male", 2000, 1 , 3)
        setView(data)
        binding.btnHidePassword.setOnClickListener {
            if(isPasswordVisible) {
                //비밀번호를 숨깁니다
                binding.tvPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.btnHidePassword.setText("보기")
            }
            else{
                //비밀번호를 표시합니다
                binding.tvPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.btnHidePassword.setText("숨기기")
            }
            isPasswordVisible = !isPasswordVisible
        }
        binding.btnInfoModifiy.setOnClickListener {
            findNavController().navigate(R.id.modifyFragment)
        }
        binding.btnLogout.setOnClickListener {
            // 저장소에서 토큰 삭제
            val preferences = requireActivity().getSharedPreferences("APP", AppCompatActivity.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.remove("token")
            editor.apply()
            //startActivity로 이동
            val intent = Intent(requireActivity(), StartActivity::class.java)
            startActivity(intent)
            requireActivity().finish()

        }
        binding.btnDeleteUser.setOnClickListener {

        }
    }
    fun setView(data : UserData){
        val dietPlan = listOf("빠른 체중 감소", "느린 체중 감소", "체중 유지", "느린 체중 증가", "빠른 체중 감소")
        val activeLevel = listOf(R.id.rbHigh, R.id.rbMid, R.id.rbLow)
        binding.tvEmail.setText(data.email)
        binding.tvPassword.setText(data.password)
        binding.tvNickName.setText(data.nickname)
        binding.tvHeight.setText(data.height.toString())
        binding.tvWeight.setText(data.weight.toString())
        if(data.gender.equals("male")){
            binding.rgGender.check(R.id.rbMale)
        }
        else{
            binding.rgGender.check(R.id.rbFemale)
        }
        binding.rgActiveLevel.check(activeLevel[data.level])
        binding.tvBirth.setText(data.birth.toString())
        binding.btnDietPlan.setText(dietPlan[data.dietPlan])
    }
}
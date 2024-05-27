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
import androidx.navigation.fragment.findNavController
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
    var eatTimenum = -1
    var flavor = -1
    var nationalFoodnum = -1
    var preferredStyle = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    // 처음에 내 정보를 입력하면 서버측에서 하루에 섭취해야할 목표 칼로리랑 탄단지를 계산해서 알려주면
    // 주는 이제 다이어트 식단 관리를 해주는 어플리케이션이라고 보면 됩니다.  그러면 매일 내가 먹은거랑 이번주에 내가 먹은것도
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecoBinding.bind(inflater.inflate(R.layout.fragment_reco,container,false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 식사시간
        binding.rgEattime.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.morning -> eatTimenum = 1
                R.id.lunch -> eatTimenum = 2
                R.id.dinner -> eatTimenum = 3
                R.id.snack -> eatTimenum = 4
            }
        }

        // 매움 안매움
        binding.rgFlavor.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.spicy -> flavor = 1
                R.id.plain -> flavor = 0
            }
        }

        // 한중일
        binding.rgNation.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.koreanFood -> nationalFoodnum = 1
                R.id.chineseFood -> nationalFoodnum = 2
                R.id.japaneseFood -> nationalFoodnum = 3
                R.id.noMatter -> nationalFoodnum = 4
            }
        }

        // 배달/요리
        binding.rgWant.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.delivery -> preferredStyle = 1
                R.id.cooking -> preferredStyle = 2
            }
        }

        binding.btnReco.setOnClickListener {
            findNavController().navigate(R.id.action_recoFragment_to_recoResultFragment)
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
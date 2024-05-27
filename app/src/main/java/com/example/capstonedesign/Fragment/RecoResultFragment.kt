package com.example.capstonedesign.Fragment

import Data.MenuItem
import Data.SelectData
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstonedesign.MenuAdapter
import com.example.capstonedesign.R
import com.example.capstonedesign.RecoAdapter
import com.example.capstonedesign.databinding.FragmentRecoBinding
import com.example.capstonedesign.databinding.FragmentRecoResultBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecoResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecoResultFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentRecoResultBinding
    lateinit var menuDataList : MutableList<SelectData>
    private lateinit var adapter : RecoAdapter //adapter객체 먼저 선언해주기!
    private lateinit var list : ArrayList<SelectData>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        list = arrayListOf(
            SelectData(
                name = "Apple",
                kcal = 52,
                carbohydrate = 14,
                protein = 0,
                fat = 0,
                imgUrl = "https://example.com/apple.jpg"
            ),
            SelectData(
                name = "Banana",
                kcal = 96,
                carbohydrate = 27,
                protein = 1,
                fat = 0,
                imgUrl = "https://example.com/banana.jpg"
            ),
            SelectData(
                name = "Orange",
                kcal = 47,
                carbohydrate = 12,
                protein = 1,
                fat = 0,
                imgUrl = "https://example.com/orange.jpg"
            )
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecoResultBinding.bind(inflater.inflate(R.layout.fragment_reco_result,container,false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = RecoAdapter(requireContext()) //어댑터 객체 만듦
        adapter.menuList = list //데이터 넣어줌
        binding.recoViewPager.adapter = adapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ModifyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecoResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
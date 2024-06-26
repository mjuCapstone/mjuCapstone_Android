package com.example.capstonedesign.Fragment

import Data.PrefItem
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstonedesign.PrefAdapter
import com.example.capstonedesign.R
import com.example.capstonedesign.databinding.FragmentPrefBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PrefFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PrefFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentPrefBinding
    var menuData = mutableListOf<PrefItem>()
    private lateinit var adapter : PrefAdapter //adapter객체 먼저 선언해주기!

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
        binding = FragmentPrefBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        initMenuRecyclerView()
    }

    fun initMenuRecyclerView(){
        val adapter= PrefAdapter() //어댑터 객체 만듦
        adapter.menuList = menuData //데이터 넣어줌
        binding.prefRecyclerView.adapter = adapter
        binding.prefRecyclerView.layoutManager= LinearLayoutManager(context)
    }


    fun initList(){
        with(menuData){
            add(PrefItem(R.drawable.img_pork_cutlet, "김치볶음밥", true))
            add(PrefItem(R.drawable.img_food, "볶음밥", false))
            add(PrefItem(R.drawable.img_history_menu1, "참치볶음밥", false))
            add(PrefItem(R.drawable.img_food, "김치찌개", true))
            add(PrefItem(R.drawable.img_food, "스테이크", true))
            add(PrefItem(R.drawable.img_food, "비빔밥", false))
            add(PrefItem(R.drawable.img_food, "고기국수", true))
            add(PrefItem(R.drawable.img_food, "육회", true))
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PrefFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PrefFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
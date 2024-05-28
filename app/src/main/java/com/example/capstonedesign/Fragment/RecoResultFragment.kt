package com.example.capstonedesign.Fragment

import Data.RecommendData
import Data.SelectData
import Response.RecommendResponse
import Service.RecommendService
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.capstonedesign.Dialog.LoadingDialog
import com.example.capstonedesign.R
import com.example.capstonedesign.MenuAdapter
import com.example.capstonedesign.RetrofitClient
import com.example.capstonedesign.databinding.FragmentRecoResultBinding
import retrofit2.Call
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Use the [RecoResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecoResultFragment : Fragment() {
    private val args: RecoResultFragmentArgs by navArgs()
    private lateinit var binding : FragmentRecoResultBinding
    private lateinit var list : ArrayList<SelectData>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*list = arrayListOf(
            SelectData(
                name = "쌀밥",
                kcal = 166,
                amount = 200,
                serving = 1,
                carbohydrate = 37,
                protein = 3,
                fat = 0,
                imgUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRGjg5Ua6zxkc_13Uo-oVKWspxnmLzqGXxlBew-v8Ps3Q&s"
            ),
            SelectData(
                name = "미역국",
                kcal = 12,
                amount = 200,
                serving = 1,
                carbohydrate = 1,
                protein = 1,
                fat = 1,
                imgUrl = "https://i.namu.wiki/i/gRYEv1mYsL0dRPEZheEF6R0nbu4QLoD3bb36HdRRP6Z-CP4yaU9x8GzJ7MK4NJMeogQPeiA495smmsAYwRha7g.webp"
            ),
            SelectData(
                name = "소불고기",
                amount = 200,
                serving = 1,
                kcal = 186,
                carbohydrate = 7,
                protein = 10,
                fat = 13,
                imgUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRMJUdGk6Pe8Ia6-5A2ENBmcArFhPWSxaIdnbWFQ4y6qw&s"
            )
        )*/
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
        // 전달받은 데이터 접근
        val mealTime = args.mealTime
        val tasteType = args.tasteType
        val menuCountry = args.menuCountry
        val cookOrDelivery = args.cookOrDelivery
        binding.tvInfo.text = mealTime + " / " + tasteType + " / " + menuCountry + " / " + cookOrDelivery + " / " + " 과 어울리는"
        val dialog = LoadingDialog(requireContext())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        val recommendService = RetrofitClient.setRetroFitInstanceWithToken(requireContext()).create(RecommendService::class.java)
        recommendService.recommend(RecommendData(mealTime,tasteType,menuCountry,cookOrDelivery)).enqueue(object : retrofit2.Callback<RecommendResponse>{
            override fun onResponse(
                call: Call<RecommendResponse>,
                response: Response<RecommendResponse>
            ) {
                if(response.isSuccessful){
                    Log.d("test", response.body()!!.data.toString())
                    list = arrayListOf(response.body()!!.data)
                    val adapter = MenuAdapter(requireContext()) //어댑터 객체 만듦)
                    adapter.menuList = list
                    binding.recoViewPager.adapter = adapter
                    binding.recoIndicator.setViewPager(binding.recoViewPager)
                }
                dialog.dismiss()
            }

            override fun onFailure(call: Call<RecommendResponse>, t: Throwable) {
                dialog.dismiss()
                TODO("Not yet implemented")
            }

        })
    }

}
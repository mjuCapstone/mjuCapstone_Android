package com.example.capstonedesign

import Data.MenuItem
import Data.PrefItem
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capstonedesign.databinding.ItemPrefBinding

class PrefAdapter : RecyclerView.Adapter<PrefAdapter.MenuViewHolder>() {
    var menuList = mutableListOf<PrefItem>()
    inner class MenuViewHolder(private val binding : ItemPrefBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(menuItem: PrefItem){
            binding.btnMenu.setImageResource(menuItem.imgNum)
            binding.tvMenuName.text = menuItem.name
            binding.btnLike.setOnClickListener {
                //서버에 요청 처리해야함.
                menuItem.isPrefer = !menuItem.isPrefer
                if(menuItem.isPrefer){
                    binding.btnLike.setImageResource(R.drawable.baseline_favorite_24)
                }
                else{
                    binding.btnLike.setImageResource(R.drawable.baseline_favorite_border_24)
                }
            }
            if(menuItem.isPrefer){
                binding.btnLike.setImageResource(R.drawable.baseline_favorite_24)
            }
            else{
                binding.btnLike.setImageResource(R.drawable.baseline_favorite_border_24)
            }
        }
    }
    //만들어진 뷰홀더 없을때 뷰홀더(레이아웃) 생성하는 함수
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding= ItemPrefBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MenuViewHolder(binding)
    }

    override fun getItemCount(): Int = menuList.size

    //recyclerview가 viewholder를 가져와 데이터 연결할때 호출
    //적절한 데이터를 가져와서 그 데이터를 사용하여 뷰홀더의 레이아웃 채움
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(menuList[position])
    }
}
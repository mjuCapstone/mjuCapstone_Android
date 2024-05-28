package com.example.capstonedesign

import Data.SelectData
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstonedesign.databinding.ItemRecoResultBinding

class MenuAdapter(private val context : Context) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {
    var menuList = mutableListOf<SelectData>()

    inner class MenuViewHolder(private val binding: ItemRecoResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(menuItem: SelectData) {
            //메뉴 이미지 설정
            Glide.with(context)
                .load(menuItem.imgUrl)
                .into(binding.ivMenuImage)
            binding.recoName.text = menuItem.name
            binding.recoGram.text = menuItem.amount.toString() + "g"
            binding.recoPerson.text = menuItem.serving.toString() +  " 인분"
            binding.recoCalorie.text = ((menuItem.kcal * menuItem.amount).toFloat() / 100).toInt().toString() + "kcal"
            binding.recoCarbo.text = ((menuItem.carbohydrate * menuItem.amount).toFloat() / 100).toInt().toString() + "g"
            binding.recoProtein.text = ((menuItem.protein * menuItem.amount).toFloat() / 100).toInt().toString() + "g"
            binding.recoFat.text = ((menuItem.fat * menuItem.amount).toFloat() / 100).toInt().toString() + "g"
        }
    }

    //만들어진 뷰홀더 없을때 뷰홀더(레이아웃) 생성하는 함수
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemRecoResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun getItemCount(): Int = menuList.size

    //recyclerview가 viewholder를 가져와 데이터 연결할때 호출
    //적절한 데이터를 가져와서 그 데이터를 사용하여 뷰홀더의 레이아웃 채움
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(menuList[position])
    }
}
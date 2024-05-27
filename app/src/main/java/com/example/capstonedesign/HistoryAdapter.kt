package com.example.capstonedesign

import Data.MenuItem
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capstonedesign.databinding.ItemMenuBinding

class HistoryAdapter(private val context : Context) : RecyclerView.Adapter<HistoryAdapter.MenuViewHolder>() {
    var menuList = mutableListOf<MenuItem>()

    inner class MenuViewHolder(private val binding: ItemMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(menuItem: MenuItem) {
            binding.menuItemImage.setImageBitmap(loadBitmapFromFile(menuItem.fileName))
            binding.menuItemName.text = menuItem.name
            binding.menuItemKcal.text = menuItem.kcal.toString() + "kcal"
            binding.menuItemCarbo.text = menuItem.carbohydrate.toString() + "g"
            binding.menuItemProtein.text = menuItem.protein.toString() + "g"
            binding.menuItemFat.text = menuItem.fat.toString() + "g"
        }
    }

    //만들어진 뷰홀더 없을때 뷰홀더(레이아웃) 생성하는 함수
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun getItemCount(): Int = menuList.size

    //recyclerview가 viewholder를 가져와 데이터 연결할때 호출
    //적절한 데이터를 가져와서 그 데이터를 사용하여 뷰홀더의 레이아웃 채움
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(menuList[position])
    }

    fun loadBitmapFromFile(fileName: String): Bitmap? {
        return context.openFileInput(fileName).use { fis ->
            BitmapFactory.decodeStream(fis)
        }
    }
}
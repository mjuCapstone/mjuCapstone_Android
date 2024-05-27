package com.example.capstonedesign

import Data.AddItemData
import Data.SelectData
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.capstonedesign.Dialog.SelectDialog
import com.example.capstonedesign.Dialog.SelectDialogListener
import com.example.capstonedesign.Fragment.NewResultFragment
import com.example.capstonedesign.databinding.DialogResultBinding
import com.example.capstonedesign.databinding.ItemNewResultBinding
import java.lang.Exception

class AddItemAdapter(private val context : Context) : RecyclerView.Adapter<AddItemAdapter.MenuViewHolder>(){
    var menuList = mutableListOf<AddItemData>()
    inner class MenuViewHolder(private val binding: ItemNewResultBinding) :
        RecyclerView.ViewHolder(binding.root){
            private lateinit var item: AddItemData
        fun bind(menuItem: AddItemData) {
            this.item = menuItem
            //음식 이미지 및 영양성분 설정
            setInfo(item)
        }
        fun setInfo(item : AddItemData){
            //음식 이미지 설정
            binding.ivMenuImage.setImageBitmap(loadBitmapFromFile(item.fileName))
            binding.tvMenuResultName.text = item.name
            binding.tvGram.text = item.amount.toString() + "g"
            binding.tvPerson.text = item.serving.toString() + "인분"
            binding.tvCalorie.text = item.kcal.toString() + "kcal"
            binding.tvCarbo.text = item.carbohydrate.toString() + "g"
            binding.tvProtein.text = item.protein.toString() + "g"
            binding.tvFat.text = item.fat.toString() + "g"
        }

    }
    public fun changeAll(index : Int, data : SelectData){
        var item = menuList[index]
        item.name = data.name
        item.amount = data.gram
        item.serving = 1
        item.kcal = data.kcal
        item.carbohydrate = data.carbohydrate
        item.protein = data.protein
        item.fat = data.fat
        notifyItemChanged(index)
    }

    public fun changeInfo(index : Int, gram : Int, person : Int){
        var item = menuList[index]
        val diff =  gram.toFloat() / item.amount.toFloat() / (person / item.serving)
        item.amount = gram
        item.serving = person
        item.kcal = (item.kcal.toFloat() * diff).toInt()
        item.carbohydrate = (item.carbohydrate.toFloat() * diff).toInt()
        item.protein = (item.protein.toFloat() * diff).toInt()
        item.fat = (item.fat.toFloat() * diff).toInt()
        notifyItemChanged(index)
    }

    //만들어진 뷰홀더 없을때 뷰홀더(레이아웃) 생성하는 함수
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemAdapter.MenuViewHolder {
        val binding = ItemNewResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun getItemCount(): Int = menuList.size

    //recyclerview가 viewholder를 가져와 데이터 연결할때 호출
    //적절한 데이터를 가져와서 그 데이터를 사용하여 뷰홀더의 레이아웃 채움
    override fun onBindViewHolder(holder: AddItemAdapter.MenuViewHolder, position: Int) {
        holder.bind(menuList[position])
    }
    fun loadBitmapFromFile(fileName: String): Bitmap? {
        try {
            return context.openFileInput(fileName).use { fis ->
                BitmapFactory.decodeStream(fis)
            }
        } catch(e : Exception){
            Log.d("test", "decode 실패")
            return null
        }
    }
}
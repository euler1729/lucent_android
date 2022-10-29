package com.example.lucent.adapter

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.load.engine.Resource
import com.example.lucent.view.SpendingTableFragment

class PagerAdapter(fragmentActivity: FragmentActivity):FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount()=1;

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {SpendingTableFragment()}
            else -> {throw Resources.NotFoundException("Position Not found!")}
        }
    }
}
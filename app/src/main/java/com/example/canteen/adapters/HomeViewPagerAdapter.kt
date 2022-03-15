package com.example.canteen.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.canteen.fragments.GoodsPagerFragment
import com.example.canteen.models.Category

class HomeViewPagerAdapter constructor(private val categoryList: List<Category>, fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun createFragment(position: Int): Fragment {
        return GoodsPagerFragment(categoryList[position])
    }
}
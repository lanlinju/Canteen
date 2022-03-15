package com.example.canteen.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.canteen.R
import com.example.canteen.databinding.FragmentGoodsPagerBinding
import com.example.canteen.models.Category

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GoodsPagerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GoodsPagerFragment(private val category: Category) : Fragment() {

    private lateinit var binding: FragmentGoodsPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_goods_pager, container, false)

        binding.textTitle.text = category.cname

        return binding.root
    }

}
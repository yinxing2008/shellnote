package com.cxyzy.note.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cxyzy.note.R
import com.cxyzy.note.ui.base.BaseActivity
import com.cxyzy.note.ui.fragment.ListNoteFragment
import com.cxyzy.note.ui.fragment.MineFragment
import com.cxyzy.note.viewmodels.NoteViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.getViewModel
import java.util.*

class MainActivity : BaseActivity<NoteViewModel>() {

    private val favoritesTabIndex = 0
    private val mineTabIndex = 1

    override fun viewModel(): NoteViewModel = getViewModel()

    override fun layoutId(): Int = R.layout.activity_main

    override fun initViews() {
        val fragments = ArrayList<Fragment>()

        val favoritesFragment = ListNoteFragment()
        var bundle = Bundle()
        bundle.putString("title", getString(R.string.notes))
        favoritesFragment.arguments = bundle

        val mineFragment = MineFragment()
        bundle = Bundle()
        bundle.putString("title", getString(R.string.mine))
        mineFragment.arguments = bundle

        fragments.add(favoritesFragment)
        fragments.add(mineFragment)

        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }

            override fun getItemCount(): Int {
                return fragments.size
            }
        }
        //禁用左右滑动切换页签
        viewPager.isUserInputEnabled = false

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_favorites -> {
                    viewPager.setCurrentItem(favoritesTabIndex, false)
                }
                R.id.action_music -> {
                    viewPager.setCurrentItem(mineTabIndex, false)
                }
            }
            true
        }
    }
}
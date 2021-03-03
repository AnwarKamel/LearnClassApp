package com.ouail.anwarkamel.learnclassapp.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ouail.anwarkamel.learnclassapp.LOG_TAG
import com.ouail.anwarkamel.learnclassapp.R
import com.ouail.anwarkamel.learnclassapp.databinding.FragmentDetailBinding
import com.ouail.anwarkamel.learnclassapp.ui.shared.SharedViewModel

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {

    private lateinit var viewModel: SharedViewModel
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment

        (requireActivity() as AppCompatActivity ).run {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        setHasOptionsMenu(true)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)

        viewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel::class.java)

     /*   viewModel.selectedMonster.observe(requireActivity(), Observer {
            Log.i(LOG_TAG, "selected monster ${it.monsterName}")
        })*/

        val binding = FragmentDetailBinding.inflate(
            inflater, container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
        //return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            navController.navigateUp()
        }

        return super.onOptionsItemSelected(item)
    }

}

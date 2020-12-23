package com.ouail.anwarkamel.learnclassapp.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ouail.anwarkamel.learnclassapp.LOG_TAG


import com.ouail.anwarkamel.learnclassapp.R
import com.ouail.anwarkamel.learnclassapp.data.Monster
import kotlinx.android.synthetic.main.fragment_main.*
import java.lang.StringBuilder

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)


        viewModel.monsterData.observe(viewLifecycleOwner, Observer {

            val monsterName = StringBuilder()

            for (monster in it) {
                monsterName.append(monster.monsterName).append("\n")
                /*     Log.i(LOG_TAG,"${monster.name} (\$${monster.price})")*/
            }
            message.text = monsterName
        })
        return inflater.inflate(R.layout.fragment_main, container, false)
    }






    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_click.setOnClickListener {
            Toast.makeText(context, "Btn Clicked", Toast.LENGTH_SHORT).show()
        }

    }

}

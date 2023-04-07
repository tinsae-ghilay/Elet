package com.tgk.Elet.ui.bahre_hassab

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tgk.Elet.R
import com.tgk.Elet.databinding.FragmentHassabBinding
import com.tgk.Elet.common.CommonViewModel
import com.tgk.Elet.ui.adapters.HolidayAdapter

class HassabFragment : Fragment() {

    private var _binding: FragmentHassabBinding? = null

    // This property is only valid between onCreateView and
    private var adapter: HolidayAdapter? = null
    // onDestroyView.
    private val binding get() = _binding!!
    private val hassabViewModel by viewModels<HassabViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentHassabBinding.inflate(inflater, container, false)
        val list = binding.holidays
        val vertical = LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false)
        list.layoutManager = vertical
        //val decorator = DividerItemDecoration(requireActivity(),DividerItemDecoration.VERTICAL)
        //binding.holidays.addItemDecoration(decorator)

        hassabViewModel.holidays.observe(requireActivity()){

            if (adapter != null){
                adapter?.setHolidays(it)

            }else{
                adapter = HolidayAdapter(it)
                binding.holidays.adapter = adapter
            }
        }
        val cViewModel = ViewModelProvider(requireActivity())[CommonViewModel::class.java]
        cViewModel.setFragmentTitle(requireActivity().getString(R.string.bahre_hasab))
        cViewModel.setShowNavButtons(View.GONE)
        val fab = binding.calculate
        list.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!list.canScrollVertically(1)){
                    fab.extend()
                }else{
                    fab.shrink()
                }
            }
        })
        fab.shrink()

        fab.setOnClickListener {
            captureInputText()
        }
        /*binding.inputYear.setOnEditorActionListener { _, actionId, _ ->
            Log.d("INPUT", "$actionId invoked - $IME_ACTION_DONE" )
            if (actionId == IME_ACTION_DONE){
                captureInputText()
            }
            false
        }*/

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun captureInputText(){
        // invoke input dialog
        //and go from there
        try {
            val message = "Year input dialog will be invoked "
            Log.d("HASSAB", message)
            Snackbar.make(binding.calculate,message,Snackbar.LENGTH_SHORT).show()
            //val input = binding.inputYear.text.toString()
            /*if (binding.inputYear.text?.toString()!=null){
                hassabViewModel.setYear(input.toInt())
            }*/
        }catch (e:Exception){
            Log.d("HASSAB", e.message.toString())
        }
    }
}
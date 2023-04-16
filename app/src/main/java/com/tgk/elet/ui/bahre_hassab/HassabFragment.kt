package com.tgk.elet.ui.bahre_hassab

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tgk.Elet.R
import com.tgk.Elet.databinding.FragmentHassabBinding
import com.tgk.elet.common.CommonViewModel
import com.tgk.elet.common.Util
import com.tgk.elet.ui.adapters.HolidayAdapter

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
        val fab = binding.fab
        val vertical = LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false)
        list.layoutManager = vertical
        //val decorator = DividerItemDecoration(requireActivity(),DividerItemDecoration.VERTICAL)
        //binding.holidays.addItemDecoration(decorator)
        hassabViewModel.bahti.observe(requireActivity()){
            try {
                binding.bahtiDay.text = Util.weekDays[(it + 1 ) % 7]
            }catch (e: java.lang.Exception){
                Toast.makeText(requireContext(),"Error , bahti is $it",Toast.LENGTH_SHORT).show()
                Log.e("Error", "bahti is $it")
            }
        }
        val ad = getString(R.string.ad)

        hassabViewModel.year.observe(requireActivity()){
            binding.year.text = "$it $ad"
        }

        hassabViewModel.era.observe(requireActivity()){
            binding.era.text = Util.apostles[it]
        }

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

        fab.setOnClickListener {
            fab.isExpanded = !fab.isExpanded
        }
        binding.query.queryInput.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == 0){
                hideKeyboard(v)
                captureInputText()
                fab.isExpanded = !fab.isExpanded
            }
            return@setOnKeyListener keyCode == KeyEvent.KEYCODE_ENTER && event.action == 0
        }

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
            val input = binding.query.queryInput.text.toString()
            val year = input.toInt()
            if (year < 30 || year > 9999){
                Toast.makeText(requireContext(),"we have decided. year should be between 30 and 9999", Toast.LENGTH_SHORT).show()
                return
            }else{
                hassabViewModel.setYear(input.toInt())
                return
            }
        }catch (e:Exception){
            Toast.makeText(requireContext(),"Please input a valid year",Toast.LENGTH_SHORT).show()
            Log.e("HASSAB", e.message.toString())
        }
    }

    private fun hideKeyboard(v: View){
        val inputMethodManager: InputMethodManager =
            requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.applicationWindowToken, 0)
    }
}
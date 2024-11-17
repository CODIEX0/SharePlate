package com.example.shareplate.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.shareplate.R
import com.example.shareplate.databinding.FragmentAdviceBinding
import com.example.shareplate.objects.Global


class AdviceFragment : Fragment() {

    // Declare variables for the UI elements
    private lateinit var factTextView: TextView
    private lateinit var factImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAdviceBinding.inflate(inflater, container, false)

        factTextView = binding.factTextView
        factImageView = binding.factImageView

        // Show a random fact when the fragment is created
        showRandomFact()

        binding.btnNextAdvice.setOnClickListener {
            showRandomFact()
        }

        binding.btnShare.setOnClickListener {
            Toast.makeText(requireContext(),"Advice Shared Successfully", Toast.LENGTH_SHORT).show()
        }

        binding.btnDonations.setOnClickListener {
            replaceFragment(DonationFragment())
        }

        return binding.root
    }

    private fun showRandomFact() {
        val randomFactIndex = (0 until Global.advices.size).random()
        showFact(randomFactIndex)
    }

    private fun showFact(index: Int) {
        val fact = Global.advices[index]
        factTextView.text = fact.advice
        factImageView.setImageResource(fact.image)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment) // replace with the new fragment
        fragmentTransaction.addToBackStack(null) //add to back stack
        fragmentTransaction.commit()
    }
    companion object {
        fun newInstance() =
            AdviceFragment().apply {
            }
    }
}
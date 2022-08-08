package com.example.weatherapp.presentation.ui.fragment.settings_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.weatherapp.data.data_store.SettingsDataStore
import com.example.weatherapp.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(SettingsDataStore(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        unitSelector()
        notificationSelector()

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun unitSelector() {
//        val checkedUnit = binding.unitsRg.checkedRadioButtonId
//
//        when (checkedUnit) {
//            binding.rbTempUnitsStandard.id -> {}
//            binding.rbTempUnitsImperial.id -> {}
//            binding.rbTempUnitsStandard.id -> {}
//        }

        binding.unitsRg.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                binding.rbTempUnitsStandard.id -> {
                    viewModel.saveUnits("standard")
                }
                binding.rbTempUnitsImperial.id -> {
                    viewModel.saveUnits("imperial")
                }
                binding.rbTempUnitsStandard.id -> {
                    viewModel.saveUnits("metric")
                }
            }
        }
    }

    private fun notificationSelector() {
        binding.notificationSwitch.setOnCheckedChangeListener { compoundButton, b ->
            viewModel.saveNotificationConfig(b)
        }
    }

}
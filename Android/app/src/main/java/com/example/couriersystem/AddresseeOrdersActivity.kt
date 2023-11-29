package com.example.couriersystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.couriersystem.databinding.ActivityAddresseeOrdersBinding

class AddresseeOrdersActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAddresseeOrdersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddresseeOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
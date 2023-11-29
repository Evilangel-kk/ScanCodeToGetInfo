package com.example.couriersystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.couriersystem.databinding.ActivityAddresseeOperateBinding

class AddresseeOperateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddresseeOperateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddresseeOperateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.addresseeId.text=Addressee.Id
        binding.addresseeName.text=Addressee.Name
        binding.allOrders.setOnClickListener {
            val intent=Intent(this,AddresseeOrdersActivity::class.java)
            startActivity(intent)
        }

        binding.addOrder.setOnClickListener {
            val intent= Intent(this,AddOrderActivity::class.java)
            startActivity(intent)
        }
    }
}
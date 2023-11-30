package com.example.couriersystem

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.couriersystem.databinding.ActivityCourierOrderContentBinding

class CourierOrderContentActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCourierOrderContentBinding

    companion object {
        fun actionStart(context: Context, order: EachOrder) {
            val intent = Intent(context, CourierOrderContentActivity::class.java).apply {
                putExtra("order", order)
            }
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCourierOrderContentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val order = intent.getSerializableExtra("order") as EachOrder
        Log.d("CourierOrderContentActivity", "courier")
        binding.orderId.text=order.Id
        binding.orderLocation.text=order.Location
        binding.addresseeName.text=order.AddresseeName
        binding.addresseePhone.text=order.AddresseePhone
    }
}
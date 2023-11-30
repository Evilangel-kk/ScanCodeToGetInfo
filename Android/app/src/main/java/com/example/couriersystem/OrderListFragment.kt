package com.example.couriersystem

import android.annotation.SuppressLint
import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.couriersystem.databinding.OrderItemBinding
import com.example.couriersystem.databinding.OrderListFragBinding
import java.util.Calendar

class OrderListFragment: Fragment() {
    private lateinit var binding: OrderListFragBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding=OrderListFragBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val layoutManager = LinearLayoutManager(activity)
        binding.orderListRecyclerView.layoutManager = layoutManager
        // 配置适配器
        Log.d("Adapter", OrderList.orders.size.toString())
        val adapter = NewsAdapter(OrderList.orders)
        binding.orderListRecyclerView.adapter = adapter
    }

    inner class NewsAdapter(val orderList: List<EachOrder>) :
        RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
        private lateinit var binding: OrderItemBinding

        inner class ViewHolder(binding: OrderItemBinding) : RecyclerView.ViewHolder(binding.root) {
            val id=binding.orderId
            val location=binding.location
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            binding= OrderItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            val holder = ViewHolder(binding)
            binding.detail.setOnClickListener {
                val order = orderList[holder.adapterPosition]
                Log.d("User", User.u)
                if(User.u=="courier"){
                    Log.d("User", User.u+"=courier")
                    CourierOrderContentActivity.actionStart(parent.context, order)
                }else{
                    Log.d("User", User.u+"=addressee")
                    AddresseeOrderContentActivity.actionStart(parent.context, order)
                }
            }
            return holder
        }
        @SuppressLint("DiscouragedApi")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val order = orderList[position]
            holder.id.text = order.Id
            holder.location.text=order.Location
        }
        override fun getItemCount() = orderList.size
    }
}
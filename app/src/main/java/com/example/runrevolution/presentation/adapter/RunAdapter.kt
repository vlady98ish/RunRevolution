package com.example.runrevolution.presentation.adapter

import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.runrevolution.R
import com.example.runrevolution.databinding.ItemRunDetailsBinding
import com.example.runrevolution.domain.model.RunDetails
import com.example.runrevolution.utils.other.TimeUtility
import java.text.SimpleDateFormat
import java.util.Locale

class RunAdapter : RecyclerView.Adapter<RunAdapter.RunViewHolder>()  {

    inner class RunViewHolder(private val binding: ItemRunDetailsBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(runDetails: RunDetails){

                Glide.with(binding.ivRunImage).load(runDetails.mapSnapShot).into(binding.ivRunImage)

                val calendar = Calendar.getInstance().apply {
                    timeInMillis = runDetails.timeDate
                }
                val dateFormat = SimpleDateFormat(" dd.MM.yy", Locale.getDefault())
                binding.tvDate.text ="Date: " +  dateFormat.format(calendar.time)

            val avgSpeed = "Avg. Speed " +String.format("%.2f", runDetails.avgSpeed) + " km/h"
            binding.tvAvgSpeed.text = avgSpeed

            val distanceInKm = "Distance " + String.format("%.2f", runDetails.distance) + " km"
            binding.tvDistance.text = distanceInKm

            binding.tvTime.text = "Duration: " +  TimeUtility.getFormattedTimeEnd(runDetails.time)

        }
    }



    val diffCallback = object : DiffUtil.ItemCallback<RunDetails>() {
        override fun areItemsTheSame(oldItem: RunDetails, newItem: RunDetails): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RunDetails, newItem: RunDetails): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }



    }

    val differ = AsyncListDiffer(this, diffCallback)


    fun submitList(list: List<RunDetails>) = differ.submitList(list)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        val binding = ItemRunDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RunViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val runDetails = differ.currentList[position]
        holder.bind(runDetails)

    }


}
package android.example.oneTap.Presentation

import android.example.oneTap.R
import android.example.oneTap.Room.myLocation
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

//Adapter for our recycler view
class location_Adapter(private var clickhandler: OnClickHandler) : RecyclerView.Adapter<location_Adapter.customviewholder>() {
    private var items: List<myLocation> = ArrayList()
    private var mClickHandler: OnClickHandler? = null

    init {
        mClickHandler=clickhandler
    }
    interface OnClickHandler {
        fun onClick(m: myLocation?)
    }

    override fun getItemCount(): Int {
        return difer.currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): customviewholder {
        Log.i("@@@@@", "ON CREATE VIEW HOLDER")
        return customviewholder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_view, parent, false)
        )
    }
    fun submitlist(list: List<myLocation>){
        Log.i("@@@@@", "ADAPTER CALLEd")
        items=list
        notifyDataSetChanged()
    }

    inner class customviewholder constructor(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) , View.OnClickListener {

        init{
            itemView.setOnClickListener(this)
        }

        val location_name: TextView = itemView.findViewById<View>(R.id.location_name) as TextView
        val locaion_address: TextView = itemView.findViewById<View>(R.id.location_address) as TextView
        val travel_to : CardView = itemView.findViewById(R.id.travel_to_cards_view)

        fun bind(location: myLocation) {
            Log.i("@@@@@", "BINDING VIEW HOLDER")
            location_name.setText(location.name.toString())
            locaion_address.setText(location.address.toString())
            travel_to.setOnClickListener {
                mClickHandler?.onClick(difer.currentList.get(adapterPosition))
            }
        }
        override fun onClick(v: View?) {
        }
    }

    fun getlocation(): List<myLocation> {
        return difer.currentList
    }
    override fun onBindViewHolder(holder: customviewholder, position: Int) {
        val locations = difer.currentList[position]
        holder.bind(locations)
    }
    private val differCallback = object : DiffUtil.ItemCallback<myLocation>() {

        override fun areContentsTheSame(oldItem: myLocation, newItem: myLocation): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areItemsTheSame(oldItem: myLocation, newItem:myLocation): Boolean {
            return oldItem == newItem
        }
    }

    val difer = AsyncListDiffer(this, differCallback)
}
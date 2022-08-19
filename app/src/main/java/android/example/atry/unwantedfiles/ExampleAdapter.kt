package android.example.atry.unwantedfiles

import android.example.atry.R
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso

class ExampleAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: List<BlogPost> = ArrayList()
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder){
            is custoviewgolder ->{
                Log.i("@@@@@","ON BIND VIEW HOLDER")
                holder.bind(items.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        Log.i("@@@@@","GET ITEM COUNT"+items.size)
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.i("@@@@@","ON CREATE VIEW HOLDER")

        return custoviewgolder(
         LayoutInflater.from(parent.context).inflate(R.layout.layout_list_item,parent,false)
     )
    }
    fun submitlist(list:List<BlogPost>){
        Log.i("@@@@@","ADAPTER CALLED (SORT OF)")
        items=list
    }

    class custoviewgolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val blog_image: ImageView = itemView.findViewById<ImageView>(R.id.blog_image)
        val blog_title: TextView = itemView.findViewById<View>(R.id.blog_title) as TextView
        val blog_author: TextView = itemView.findViewById<TextView>(R.id.blog_author)
        fun bind(blogpost: BlogPost) {
            Log.i("@@@@@","BINDING VIEW HOLDER")
            blog_title.setText(blogpost.Title)
            blog_author.setText(blogpost.username)
            val requestoptions=RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
            Picasso.get().load(blogpost.image).placeholder(R.drawable.ic_launcher_background).into(blog_image)


        }
    }
}
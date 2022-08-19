package android.example.atry.unwantedfiles

import android.app.SearchManager
import android.content.Intent
import android.example.atry.R
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SearchableActvity :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(R.layout.search)
        Log.i("@!@!","came bro")
        handleIntent(intent)
    }
    private fun handleIntent(intent: Intent) {

        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            //use the query to search your data somehow
            val textview=findViewById<TextView>(R.id.search_textView)
            textview.setText(query)
        }
    }
}

package android.example.oneTap.Presentation

import android.example.oneTap.Presentation.ViewModel.myviewmodel
import android.example.oneTap.R
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.save_location_activity.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Save_Location_Activity : AppCompatActivity() {

    private val myviewmodel: myviewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.save_location_activity)

        lifecycleScope.launch {
            myviewmodel.save_task_event.collectLatest {
                progressBar.progress=100
                progressBar.visibility=View.GONE
                Toast.makeText(this@Save_Location_Activity,"LOCATION SAVED",Toast.LENGTH_SHORT).show()
                finish()
                overridePendingTransition(R.anim.nothing, R.anim.slide_out)
            }
        }

        save_.setOnClickListener {
            val userEnteredname =outlinedTextField.editText?.text.toString()
            save_.isEnabled=false
            progressBar.visibility= View.VISIBLE
            Log.i("#@#","Save location clicked")
            lifecycleScope.launch {
                myviewmodel.fetch_location(userEnteredname)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.nothing, R.anim.slide_out)
    }
}
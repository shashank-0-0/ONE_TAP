package android.example.atry

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.save_location_activity.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Save_Location_Activity : AppCompatActivity() {

    public val myviewmodel: myviewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.save_location_activity)

        lifecycleScope.launch {
            myviewmodel.save_task_event.collectLatest {
                progressBar.progress=100
                progressBar.visibility=View.GONE
                finish()
                overridePendingTransition(R.anim.nothing,R.anim.slide_out)
            }
        }


        save_.setOnClickListener {
            val userEnteredname =outlinedTextField.editText?.text.toString()
            save_.isEnabled=false
            progressBar.visibility= View.VISIBLE
            Log.i("#@#","Save location clicked")
            myviewmodel.fetch_location(userEnteredname)

        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.nothing,R.anim.slide_out)
    }
}
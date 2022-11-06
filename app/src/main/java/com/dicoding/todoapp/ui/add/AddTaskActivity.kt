package com.dicoding.todoapp.ui.add

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.databinding.ActivityAddTaskBinding
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.utils.DatePickerFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {
    private var dueDateMillis: Long = System.currentTimeMillis()
    private lateinit var viewModel: AddTaskViewModel
    private lateinit var binding: ActivityAddTaskBinding
    lateinit var imageView: ImageView
    private  var pickImage = 100
    private var imageUri: Uri? = null


    companion object{
        private const val REQUEST_CODE_PERMISSION = 10
        private val REQUIRED_PERMISSION = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
        binding.btnAddPict.setOnClickListener {
            startGallery()
        }

         */
        imageView = findViewById(R.id.iv_image_preview)
        binding.btnAddPict.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery,pickImage)
        }

        supportActionBar?.title = getString(R.string.add_task)
        val viewModelFactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[AddTaskViewModel::class.java]


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage){
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                //TODO 12 : Create AddTaskViewModel and insert new task to database
                val edtTitle = findViewById<TextInputEditText>(R.id.add_ed_title)
                val edtDescription = findViewById<TextInputEditText>(R.id.add_ed_description)
                val edtDepartemen = findViewById<TextInputEditText>(R.id.ed_departemen)
                val edtKeterangan = findViewById<TextInputEditText>(R.id.ed_keterangan)
                val btnAddPict = findViewById<Button>(R.id.btn_add_pict)
                imageView = findViewById(R.id.iv_image_preview)



                val title = edtTitle.text.toString()
                val description = edtDescription.text.toString()
                val departemen = edtDepartemen.text.toString()
                val keterangan = edtKeterangan.text.toString()


                val newTask = Task(0,title, description, dueDateMillis, false, departemen, keterangan)
                viewModel.addTask(newTask)
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showDatePicker(view: View) {
        val dialogFragment = DatePickerFragment()
        dialogFragment.show(supportFragmentManager, "datePicker")
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        findViewById<TextView>(R.id.add_tv_due_date).text = dateFormat.format(calendar.time)

        dueDateMillis = calendar.timeInMillis
    }


}
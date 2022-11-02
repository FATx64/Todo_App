package com.dicoding.todoapp.ui.detail

import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.TASK_ID
import com.google.android.material.textfield.TextInputEditText

class DetailTaskActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailTaskViewModel
    private lateinit var btnDelete : Button
    private lateinit var edtTitle: TextInputEditText
    private lateinit var edtDesc: TextInputEditText
    private lateinit var edtDepartemen : TextInputEditText
    private lateinit var edtKeterangan : TextInputEditText
    private lateinit var edtDueDate : TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        //TODO 11 : Show detail task and implement delete action
        btnDelete = findViewById(R.id.btn_delete_task)
        edtTitle = findViewById(R.id.detail_ed_title)
        edtDesc = findViewById(R.id.detail_ed_description)
        edtDueDate = findViewById(R.id.detail_ed_due_date)
        edtDepartemen = findViewById(R.id.detail_departemen)
        edtKeterangan = findViewById(R.id.detail_keterangan)

        val taskId = intent.getIntExtra(TASK_ID, 0)
        val viewfactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this , viewfactory).get(DetailTaskViewModel::class.java)
        viewModel.apply {
            setTaskId(taskId)
            task.observe(this@DetailTaskActivity){ task ->
                edtTitle.text = Editable.Factory.getInstance().newEditable(task.title)
                edtDesc.text = Editable.Factory.getInstance().newEditable(task.description)
                edtDueDate.text = Editable.Factory
                    .getInstance()
                    .newEditable(DateConverter.convertMillisToString(task.dueDateMillis))
                edtDepartemen.text = Editable.Factory.getInstance().newEditable(task.departemen)
                edtKeterangan.text = Editable.Factory.getInstance().newEditable(task.keterangan)

            }


            showToast.observe(this@DetailTaskActivity) {event ->
                val message = event.getContentIfNotHandled()
                message?.let {
                    Toast.makeText(this@DetailTaskActivity, it, Toast.LENGTH_SHORT).show()
                }
            }

            deletedTask.observe(this@DetailTaskActivity){event ->
                val savedContent = event.getContentIfNotHandled()
                savedContent?.let {
                    if (it){
                        viewModel.task.removeObservers(this@DetailTaskActivity)
                        finish()
                    }
                }
            }

          btnDelete.setOnClickListener {
              viewModel.deleteTask()
          }
        }
    }
}
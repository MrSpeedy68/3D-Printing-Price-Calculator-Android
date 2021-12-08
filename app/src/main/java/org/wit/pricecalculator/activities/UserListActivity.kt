package org.wit.pricecalculator.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.pricecalculator.R
import org.wit.pricecalculator.adapters.UserAdapter
import org.wit.pricecalculator.adapters.UserListiner
import org.wit.pricecalculator.databinding.ActivityUserListBinding
import org.wit.pricecalculator.main.MainApp
import org.wit.pricecalculator.models.UserModel

class UserListActivity: AppCompatActivity(), UserListiner {

    lateinit var app: MainApp
    private lateinit var binding: ActivityUserListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = UserAdapter(app.users.findAll(),this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, UserActivity::class.java)
                startActivityForResult(launcherIntent,0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onUserClick(users: UserModel) { //Clicking on users takes us to users edit
        val launcherIntent = Intent(this, UserActivity::class.java)
        launcherIntent.putExtra("user_edit", users)
        startActivityForResult(launcherIntent,0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        binding.recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }
}
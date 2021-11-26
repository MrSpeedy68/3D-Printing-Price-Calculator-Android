package org.wit.pricecalculator.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.pricecalculator.R
import org.wit.pricecalculator.adapters.MaterialAdapter
import org.wit.pricecalculator.adapters.MaterialListiner
import org.wit.pricecalculator.databinding.ActivityMaterialListBinding
import org.wit.pricecalculator.main.MainApp
import org.wit.pricecalculator.models.MaterialsModel


class MaterialListActivity : AppCompatActivity(), MaterialListiner {

    lateinit var app: MainApp
    private lateinit var binding: ActivityMaterialListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMaterialListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = MaterialAdapter(app.materials.findAll(),this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, MaterialActivity::class.java)
                startActivityForResult(launcherIntent,0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMaterialClick(materials: MaterialsModel) { //Clicking on material takes us to material edit
        val launcherIntent = Intent(this, MaterialActivity::class.java)
        launcherIntent.putExtra("material_edit", materials)
        startActivityForResult(launcherIntent,0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        binding.recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }
}















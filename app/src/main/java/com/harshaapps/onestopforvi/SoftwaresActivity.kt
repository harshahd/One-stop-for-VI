package com.harshaapps.onestopforvi

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView


private const val tag="SoftwareListActivity"
class SoftwaresActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_softwares)
            val navView:NavigationView = findViewById<NavigationView>(R.id.navView)
            val drawerLayout:DrawerLayout=findViewById<DrawerLayout>(R.id.softwareContainer)
            val navController:NavController = findNavController(R.id.navigationDrawerFragment)

appBarConfiguration = AppBarConfiguration(setOf(R.id.tool_software, R.id.tool_tts_catalog, R.id.tool_downloads), drawerLayout)
           val toolbar=findViewById<Toolbar>(R.id.toolBar)
            setSupportActionBar(toolbar)
                            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
                }

    override fun onSupportNavigateUp(): Boolean {
val navController=findNavController(R.id.navigationDrawerFragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        cacheDir.delete()
    }
}




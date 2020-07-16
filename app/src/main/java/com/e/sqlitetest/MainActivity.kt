package com.e.sqlitetest

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val database = baseContext.openOrCreateDatabase("sqlite-test-1.db", Context.MODE_PRIVATE, null)
        database.execSQL("drop table if exists contacts")
        var sql = "create table if not exists contacts(_id integer primary key not null, name text, phone integer, email text)"
        Log.d(TAG, "onCreate: sql is $sql")
        database.execSQL(sql)

        sql = "insert into contacts(name, phone, email) values ('aaa', '6456789', 'aaa@email.com')"
        Log.d(TAG, "onCreate: sql is $sql")
        database.execSQL(sql)

        val values = ContentValues().apply {
            put("name", "Fred")
            put("phone", 12345)
            put("email", "fred@nurk.com")
        }

        //applyの有無での比較↑↓
//        val values = ContentValues()
//        values.put("name", "Fred")
//        values.put("phone", 12345)
//        values.put("email", "fred@nurk.com")

        val generatedId = database.insert("contacts", null, values)

        val query = database.rawQuery("select * from contacts", null)
        query.use {
            while (it.moveToNext()) { //カーソルが次のレコードに移動する。何もないならfalseを返す。
                //全てのレコードを循環する
                with(it) {
                    val id = getLong(0)
                    val name = getString(1)
                    val phone = getInt(2)
                    val email = getString(3)
                    val result = "ID: $id, Name = $name, phone = $phone, email = $email"
                    Log.d(TAG, "onCreate: reading data $result")
                }
            }
        }

        database.close()

        Log.d(TAG, "onCreate: record added with id $generatedId")

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
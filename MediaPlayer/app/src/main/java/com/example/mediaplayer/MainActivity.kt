package com.example.mediaplayer

import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.song_ticket.view.*
import java.io.IOException
import java.lang.Exception

class MainActivity : AppCompatActivity(){


    var ListSong=ArrayList<Song>()
    var mp:MediaPlayer ?=null
    var adapter:SongAdapter ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LoadUrlOnline()

        adapter=SongAdapter(this,ListSong)
        lvSong.adapter=adapter
        var mytrack= myTrackSong()

        mytrack.start()
    }

    fun LoadUrlOnline(){
        ListSong.add(Song("Song Gio","Jack","http://urban180.com/wp-content/uploads/2017/09/olaide-Bambi_urban180.com_-1.mp3"))
        ListSong.add(Song("Chuyen Hoa Sim","Dan Nguyen","http://server6.mp3quran.net/thubti/004.mp3"))
        ListSong.add(Song("24 25","King of Convenience","https://data.chiasenhac.com/dataxx/04/downloads/1494/1/1493403-8b74b39b/m4a/24-25%20-%20Kings%20Of%20Convenience.m4a"))
        ListSong.add(Song("Ed Sheeran","Happier","http://server6.mp3quran.net/thubti/005.mp3"))


    }

    inner class SongAdapter: BaseAdapter , MediaPlayer.OnPreparedListener{
        override fun onPrepared(mp: MediaPlayer?) {
            mp!!.start()
            sbProgess.max = mp!!.duration
        }


        var ListSongAdap=ArrayList<Song>()
        var context:Context?=null
        constructor(context: Context,ListSongAdap:ArrayList<Song>):super(){
            this.context=context
            this.ListSongAdap=ListSongAdap
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var mysong=this.ListSongAdap[position]
            var myView =layoutInflater.inflate(R.layout.song_ticket,null)
            myView.tvSongName.text=mysong.title
            myView.tvAuthor.text=mysong.author

            myView.buPlay.setOnClickListener (View.OnClickListener{
                if( myView.buPlay.text.equals("Stop") ){
                    mp!!.stop()
                    myView.buPlay.text = "Start"
                }else {

                    mp = MediaPlayer()
                    mp!!.setOnPreparedListener(this)
                    try {
                        mp!!.setDataSource(mysong.LyricUrl)
                        mp!!.prepareAsync()
                        myView.buPlay.text = "Stop"

                    } catch (ex: Exception) {
                    }
                }
        })
            return myView
        }

        override fun getItem(position: Int): Any {
            return this.ListSongAdap[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return this.ListSongAdap.size
        }

    }
    inner class myTrackSong():Thread(){


        override fun run() {
            while (true){
                try {

                    Thread.sleep(1000)
                }catch (ex:Exception){}
                runOnUiThread{
                    if (mp!=null)
                        sbProgess.progress=mp!!.currentPosition
                }

            }
            }

            }
    fun CheckUserPermsions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_ASK_PERMISSIONS)
                return
            }
        }

        LoadSong()

    }

    //get acces to location permsion
    private val REQUEST_CODE_ASK_PERMISSIONS = 123


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LoadSong()
            } else {
                // Permission Denied
                Toast.makeText(this, "denail", Toast.LENGTH_SHORT)
                    .show()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }


    fun LoadSong(){
        val allSongUrl =MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection =MediaStore.Audio.Media.IS_MUSIC +"!=0"
        val cursor=contentResolver.query(allSongUrl,null,selection,null,null)
        if (cursor!=null){

            if (cursor!!.moveToFirst()){
                do {
                    val songURL=cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val SongAuthor =cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val SongName=cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                    ListSong.add(Song(songURL,SongAuthor,SongName))

                }while (cursor!!.moveToNext())
            }
            cursor!!.close()
        }
    }
    }


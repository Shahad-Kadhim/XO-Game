package com.example.xogame

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.xogame.databinding.ActivityGameBinding
import com.example.xogame.databinding.ActivityMainBinding

class Game : AppCompatActivity() {
    var scoreX=0
    var scoreO=0
    var winner :String=""
    var result="p1      0 -  0      p2"
    lateinit var binding: ActivityGameBinding
    var playerturn=Setting.playerChar
    var aiChar:XO?=null
    var aiTag :String?=null
    var idDrawableAi:Int?=null
    val buttonArray= arrayOfNulls<Array<ImageView?>>(3)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        takeIf { Setting.Mode==Mode.Ai }?.let {
            if(playerturn==XO.X){
                aiChar=XO.O
                idDrawableAi=R.drawable.ic_circle
                aiTag="O"
                } else{
                aiChar=XO.X
                idDrawableAi=R.drawable.ic_cross
                aiTag="X"
            }
        }
        binding.apply {
            buttonArray.apply{
                this[0]= arrayOfNulls(3)
                this[1]= arrayOfNulls(3)
                this[2]= arrayOfNulls(3)
                initialArray()
            }
        }
    }
    private fun checkResult():Boolean{
        if(buttonArray.flatMap { it!!.toList() }.filter { it!!.isClickable }.isEmpty()) {
            binding.playAgainBtn.visibility = View.VISIBLE
            return true
        }
        if(buttonArray.any { it!!.all {image -> image!!.tag=="X"} }) {
            winner="X"
           return finishGame()
        }
        if(buttonArray.any { it!!.all { image ->image!!.tag=="O" } })
        {
            winner="O"
            return finishGame()
        }
        buttonArray.forEachIndexed { row, chars ->
            val m= mutableListOf<String>()
            chars!!.forEachIndexed { col, _ -> m.add(buttonArray[col]!![row]!!.tag  as String) }
            if(m.all { it=="X" }) {
                winner="X"
                return finishGame()
            }
            else if(m.all { it=="O" }) {
                winner="O"
                return finishGame()
            }
        }
        if(buttonArray[0]!![0]!!.tag.toString().isNotBlank()) {
            if (mutableListOf<String>().apply { buttonArray.forEachIndexed { index, _ -> add(buttonArray[index]!![index]!!.tag as String) } }.all { it == buttonArray[0]!![0]!!.tag }) {
                winner=if (buttonArray[0]!![0]!!.tag=="X") "X" else "O"
                return finishGame()
            }
        }
        if(buttonArray[0]!![2]!!.tag.toString().isNotBlank() ){
            if(mutableListOf<String>().apply { buttonArray.forEachIndexed { index, _ -> add(buttonArray[index]!![2-index]!!.tag as String) } }.all { it==buttonArray[0]!![2]!!.tag as String })
            {
                winner=if (buttonArray[0]!![2]!!.tag=="X") "X" else "O"
                return finishGame()
            }
        }
        return false
    }
    fun playHuman(v: View){
        if(playerturn==XO.X){
            (v as ImageView).run {
                setImageDrawable(ContextCompat.getDrawable(this@Game,R.drawable.ic_cross)!!)
                tag = "X"
            }
            takeIf { Setting.Mode==Mode.Friend }?.let {  playerturn=XO.O}
        } else{
            (v as ImageView).run{
                setImageDrawable(ContextCompat.getDrawable(this@Game,R.drawable.ic_circle)!!)
                tag="O"
            }
            takeIf { Setting.Mode==Mode.Friend }?.let {  playerturn=XO.X}
        }
        v.isClickable=false
        if (!checkResult() &&Setting.Mode==Mode.Ai) playAi()
        checkResult()
    }
    private fun playAi() {
        buttonArray.flatMap { it!!.toList() }.filter { it!!.isClickable }.takeIf { it.isNotEmpty() }?.let{it.random()!!.apply { this.isClickable=false }
                .run{
                      setImageDrawable(ContextCompat.getDrawable(this@Game,idDrawableAi!!))
                      tag=aiTag
                }
        }
    }
    private fun finishGame():Boolean{
        binding.playAgainBtn.visibility=View.VISIBLE
        if (winner=="X") scoreX++ else if (winner=="O") scoreO++
        buttonArray.forEach {
            it!!.forEach { image ->
                image!!.isClickable=false
            }
        }
        winner=""
        result=if(Setting.playerChar==XO.X){
            "p1      $scoreX  -  $scoreO      p2"
        }else "p1      $scoreO  -  $scoreX      p2"
        binding.score.text =result
        return true
    }
    fun playAgain(v:View){
        binding= ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.score.text
        initialArray()
        binding.score.text =result
    }
    fun goToHome(v: View){
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
    private fun initialArray(){
       binding.run {
           buttonArray.apply {
               this[0]!!.apply {
                   this[0] = r1c1
                   this[1] = r1c2
                   this[2] = r1c3
               }
               this[1]!!.apply {
                   this[0] = r2c1
                   this[1] = r2c2
                   this[2] = r2c3
               }
               this[2]!!.apply {
                   this[0] = r3c1
                   this[1] = r3c2
                   this[2] = r3c3
               }
           }
       }
    }
}
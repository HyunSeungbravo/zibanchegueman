package com.davidlee.zibanchegueman_android.navigation

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.davidlee.zibanchegueman_android.AdminActivity
import com.davidlee.zibanchegueman_android.R
import com.davidlee.zibanchegueman_android.func.chosenGroup
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_accounting.*
import org.json.JSONArray
import java.net.URL

class AccountingFragment : Fragment(){

    var depoist_amount_Int: Int = 0
    var withdraw_amount_Int: Int = 0
    var balance_Int: Int = 0

    var fragmentView : View? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        Account_progressBar.visibility = View.GONE
        account_admin.isGone = true


        init_btns()

        //처음에 0번꺼 가져온다.
        getAccounting(chosenGroup.mode!!.toInt())


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        fragmentView = LayoutInflater.from(activity).inflate(R.layout.fragment_accounting, container, false)


        return fragmentView
    }


    fun init_btns(){

        selectGroup.setOnClickListener {
            setGroup()

        }

        accounting_refresh.setOnClickListener {

            Account_progressBar.visibility = View.VISIBLE
            getAccounting(0)
        }

        view_realblock.setOnClickListener {

            createFragmentonTheFly()

            //bottom_navigation.selectedItemId = R.id.navigation_RealBlockInfo



        }

        account_admin.setOnClickListener {

            startActivity(Intent(view!!.context, AdminActivity::class.java ))
        }

    }

    fun createFragmentonTheFly(){


        var mFragmentTransaction: FragmentTransaction = getActivity()!!.getSupportFragmentManager().beginTransaction()

        mFragmentTransaction.replace(R.id.main_content, RealBlockInfoFragment())
        mFragmentTransaction.commit()



    }




    fun setGroup(){

        var groups = arrayOf("세종대학교 총학생회", "테니스동호회", "절친구들", "종교 소그룹모임")

        var index = -1
        var temp : Int? = null
        val builder = AlertDialog.Builder(view!!.context)

        builder.setTitle("소그룹을 선택해주세요")

        builder.setSingleChoiceItems(groups, index){dialog, which ->

            index = which
            temp = which


            selectGroup.text = groups[temp!!]
            chosenGroup.mode = temp!!
            selectGroup.setTextColor(-0x1000000)

            //정보가져오고 종료시킨다.
            getAccounting(temp!!)

            //Toast.makeText(view!!.context, "${chosenGroupt.mode}", Toast.LENGTH_SHORT).show()


            dialog.dismiss()


        }

        val dialog = builder.create()
        dialog.show()


    }

    fun rearrangeAdminMode(){

        when (chosenGroup.mode){
            0->{
                account_admin.isGone = true

            }
            1 ->{
                account_admin.isGone = true
            }
            2 ->{
                account_admin.isGone = false

            }
            3 ->{
                account_admin.isGone = true
            }
        }

    }

    fun getAccounting(mode: Int){

        when (mode){
            0 ->{
                sejongAccounting()

            }
            1 ->{
                tennisAccounting()

            }
            2 ->{

                friendsAccounting()

            }
            3 ->{

                religiousAccounting()

            }
        }

        rearrangeAdminMode()
        Account_progressBar.visibility = View.GONE



    }

    fun sejongAccounting(){
        val url2 = "http://35.235.52.246:8080/api/web/ious"

        var depoist_amount_Int_temp: Int = 0
        var withdraw_amount_Int_temp: Int = 0




        val apiResponse = URL(url2).readText()

        println("**************************************")
        //println(apiResponse)
        println("**************************************")
        val jArray = JSONArray(apiResponse)



        for (i in 0 until jArray.length()){
            val obj = jArray.getJSONObject(i)
            val stateArray = obj.getJSONObject("state")
            val dataArray = stateArray.getJSONObject("data")



            val from = dataArray.getString("from")
            val to = dataArray.getString("to")
            val amount = dataArray.getString("amount")
            val date = dataArray.getString("date")
            val type = dataArray.getString("type")

            val amount_int = amount.toInt()

            if (type == "Withdraw"){
                withdraw_amount_Int_temp = withdraw_amount_Int_temp + amount_int

            }
            else if (type == "Deposit"){
                depoist_amount_Int_temp = depoist_amount_Int_temp + amount_int

            }


            Log.d("&&&", "^^^^^^^^^^^^^^^^^^^^^^^")
            Log.d("&&&", "from($i): $from")
            Log.d("&&&", "to($i): $to")
            Log.d("&&&", "amount($i): $amount")
            Log.d("&&&", "date($i): $date")
            Log.d("&&&", "type($i): $type")
            Log.d("&&&", "^^^^^^^^^^^^^^^^^^^^^^^")




        }

        withdraw_amount_Int = withdraw_amount_Int_temp
        depoist_amount_Int = depoist_amount_Int_temp

        balance_Int = depoist_amount_Int - withdraw_amount_Int

        setDataString()



        //Log.d("&&&", jArray.length().toString())

        //println("**************************************")



    }

    fun tennisAccounting(){
        deposit_amount_text.text = "900000"
        withdraw_amount_text.text = "700000"
        balance_amount_text.text = "200000"

    }

    fun friendsAccounting(){
        deposit_amount_text.text = "1500000"
        withdraw_amount_text.text = "250000"
        balance_amount_text.text = "1250000"

    }

    fun religiousAccounting(){
        deposit_amount_text.text = "3100000"
        withdraw_amount_text.text = "125300"
        balance_amount_text.text = "2974700"

    }

    fun setDataString(){

        deposit_amount_text.text = depoist_amount_Int.toString()
        withdraw_amount_text.text = withdraw_amount_Int.toString()
        balance_amount_text.text = balance_Int.toString()

    }

/////////////
}



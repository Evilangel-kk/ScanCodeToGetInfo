package com.example.couriersystem

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.couriersystem.databinding.ActivityMainBinding
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Websocket.connect("https://16dc7372.r16.vip.cpolar.cn")
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var intent= Intent(this,LoginActivity::class.java)
        binding.courier.setOnClickListener {
            intent.putExtra("user","courier")
            startActivity(intent)
        }
        binding.addressee.setOnClickListener {
            intent.putExtra("user", "addressee")
            startActivity(intent)
        }
        binding.scan.setOnClickListener {
//            val intentIntegrator = IntentIntegrator(this@MainActivity)
            IntentIntegrator(this).setOrientationLocked(false)
                .setCaptureActivity(ScanCodeActivity::class.java)
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)// 扫码的类型,可选：一维码，二维码，一/二维码
                .setPrompt("请对准二维码")// 设置提示语
                .setCameraId(0)// 选择摄像头,可使用前置或者后置
                .setBeepEnabled(false)// 是否开启声音,扫完码之后会"哔"的一声
                .initiateScan()
        }
    }

    // Get the results:
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                var msg=result.contents.split(":")
                if(msg[0]=="码上识件"){
                    Toast.makeText(this, "Scanned:"+result.contents, Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this, "请扫描本系统的二维码", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    override fun onDestroy() {
        Websocket.close()
        super.onDestroy()
    }
}
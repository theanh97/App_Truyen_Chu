package com.theanhdev97.truyenchu.Utils

import android.content.Context
import android.os.Environment
import com.theanhdev97.truyenhchu.Utils.Const
import java.io.*

/**
 * Created by DELL on 16/08/2017.
 */
class ExternalStorage {
    companion object {
        fun saveObject(context: Context, fileName: String, any: Any) {
            // chưa đầy bộ nhớ
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                var path = Environment.getExternalStorageDirectory().path
                try {
                    var dir = File(path)
                    if (!dir.exists()) {
                        dir.mkdir()
                    }
                    var file = File(path, fileName)
                    var fos = FileOutputStream(file)
                    var oops = ObjectOutputStream(fos)
                    oops.writeObject(any)
                    oops.close()

                } catch (ex: Exception) {
                    Const.log("\n" + ex.toString())
                }
            }
            // đầy bộ nhớ
            else {
                Const.toast("Bộ nhớ full !!! Không thể thực hiện lưu truyện vào bộ nhớ ngoài", context)
            }
        }

        fun getObject(context: Context, fileName: String): Any? {
            var path = Environment.getExternalStorageDirectory().path
            try {
                var dir = File(path)
                if (!dir.exists())
                    dir.mkdir()
                var file = File(path, fileName)
                var fis = FileInputStream(file)
                var oips = ObjectInputStream(fis)
                var obj = oips.readObject()
                if (obj != null)
                    return obj
            } catch (ex: Exception) {
            }
            return null

        }
    }
}
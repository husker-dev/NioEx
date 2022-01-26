
import com.huskerdev.mio.*
import java.io.File
import java.net.URL

fun main(){
    val imagePath = "https://sun9-18.userapi.com/impg/oU8g6f1Kkoo1-dIMyozPeXX_BiltOH5rglWSlA/BSgZKmKPb7M.jpg?size=1440x1920&quality=96&sign=62a00ffc3917df345bc50f0e07baa8aa&type=album"

    URL(imagePath).downloadToFile(File("test.jpg"))
}



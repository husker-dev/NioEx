
import com.huskerdev.nioex.children
import com.huskerdev.nioex.copyTo
import com.huskerdev.nioex.copyToFolder
import com.huskerdev.nioex.speedometer.CopyingSpeedometer
import java.io.File

fun main(){
    val file1 = File("myFolder")
    val file2 = File("destFolder")

    // Copy file/folder to folder
    file1.copyToFolder(file2)

    // Copy file to destination file
    file1.copyTo("newFile")

    // Copy array to folder
    file1.children.copyToFolder("newFolder")

    // Copy file array to array
    arrayOf(File("file_1"), File("file_2"))
        .copyTo(arrayOf("new_file_1", "new_file_2"))

    // Copying handler
    file1.copyTo("newFile") {
        println("-------------------------")
        println("Size: ${it.size}")
        println("Current: ${it.current}")
        println("Copying from: ${it.source}")
        println("Copying to: ${it.target}")
    }

    // Copying speedometer
    file1.copyTo("newFile", CopyingSpeedometer {
        println("-------------------------")
        println("Size: ${it.size}")
        println("Current: ${it.current}")
        println("Copying from: ${it.source}")
        println("Copying to: ${it.target}")
        println("Speed: ${it.bytesPerSecond}")
    })

    // Copying speedometer (advanced)
    file1.copyTo("newFile", CopyingSpeedometer().apply {
        updateDelay = 10
        speedUpdateDelay = 10
        onStarted {
            println("Copying started!")
        }
        onCompleted {
            println("Copying completed!")
        }
        onUpdate {
            println("-------------------------")
            println("Size: ${it.size}")
            println("Current: ${it.current}")
            println("Copying from: ${it.source}")
            println("Copying to: ${it.target}")
            println("Speed: ${it.bytesPerSecond}")
        }
    })
}


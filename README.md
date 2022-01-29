# NioEx

<a href="LICENSE"><img src="https://img.shields.io/github/license/husker-dev/NioEx?style=flat-square"></a>
<a href="https://jitpack.io/#husker-dev/NioEx"><img src="https://img.shields.io/jitpack/v/github/husker-dev/NioEx?style=flat-square"></a>
<a href="https://github.com/husker-dev/NioEx/releases/latest"><img src="https://img.shields.io/github/v/release/husker-dev/NioEx?style=flat-square"></a>


Advanced I/O usage in Kotlin

# Features

- Speedometers for every operation
- Zipping file/folder
- Unzipping
- Copying
- Downloading from URL
- > OS acceleration (in future)

## Examples

Here are some of examples, to see all of them, follow [this link](https://github.com/husker-dev/NioEx/tree/master/core/src/examples/kotlin)

- Zip file
  ```kotlin
  // Zip to archive
  fileToZip.zipTo("archive.zip")
  ```

- Zip several files
  ```kotlin
  // Zip file array
  fileToZip.children.zipTo("archive.zip")
  ```

- Zip file with speedometer
  ```kotlin
  fileToZip.zipTo("archive.zip", ZipSpeedometer().apply {
      updateDelay = 10
      speedUpdateDelay = 10
      
      onStarted {
          println("Zipping started!")
      }
      onCompleted {
          println("Zipping completed!")
      }
      onUpdate {
          println("-------------------------")
          println("Size: ${it.size}")
          println("Zipped bytes: ${it.current}")
          println("Zipping entry: ${it.entry}")
          println("Zipping file: ${it.file}")
          println("Speed: ${it.bytesPerSecond}")
      }
  })

  ```

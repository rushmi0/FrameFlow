import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import kotlin.concurrent.thread

class FrameSnap(private val videoFileName: String, outputDirName: String) {
    private val videoPath = "src/main/kotlin/video/$videoFileName"
    private val outputDirectory = "src/main/kotlin/$outputDirName/"

    // Interface สำหรับติดตามความคืบหน้าของการประมวลผล
    interface ProgressListener {
        fun onProgress(progress: Int)
    }

    private var progressListener: ProgressListener? = null

    // กำหนด ProgressListener
    fun setProgressListener(listener: ProgressListener) {
        this.progressListener = listener
    }

    // อัปเดตความคืบหน้า
    private fun updateProgress(progress: Int) {
        if (progressListener != null) {
            progressListener!!.onProgress(progress)
        }
    }

    // แสดง Progress Bar
    private fun displayProgressBar(totalFrames: Int) {
        var progress = 0
        while (progress < 100) {
            val currentFrames = countImageFiles(outputDirectory)
            progress = ((currentFrames.toFloat() / totalFrames.toFloat() * 100).toInt()).coerceAtMost(100)
            print("\rProgress: $progress%")
            updateProgress(progress)  // เรียกใช้ updateProgress เพื่ออัปเดตความคืบหน้า
            Thread.sleep(1000)
        }
        println("\nProcessing complete.")
    }

    // ทำการแปลงวิดีโอเป็นรูปภาพ
    fun snapVideo(fileExtension: String, FPS: Int, CPUthreads: Int): String {
        File(outputDirectory).mkdirs()

        // สร้างคำสั่ง ffmpeg สำหรับแปลงวิดีโอเป็นรูปภาพ
        val ffmpegCommand = "ffmpeg -i ${this.videoPath} -vf fps=${FPS} -threads $CPUthreads ${this.outputDirectory}/Frame_%06d.$fileExtension"

        return try {
            // ประมาณจำนวนเฟรมทั้งหมด
            val totalFrames = estimateTotalFrames(FPS)
            // เริ่มการแสดงความคืบหน้า
            thread { displayProgressBar(totalFrames) }

            // ทำการเรียกใช้คำสั่ง ffmpeg
            val process = Runtime.getRuntime().exec(ffmpegCommand)
            val exitCode = process.waitFor()

            if (exitCode == 0) {
                // นับจำนวนไฟล์รูปภาพ
                val count = countImageFiles(outputDirectory)
                "\nVideo frames extracted successfully. \nTotal number of files $count"
            } else {
                // แสดงข้อความเมื่อไม่สามารถแปลงได้
                val errorStream = BufferedReader(InputStreamReader(process.errorStream))
                val errorMessage = errorStream.readText()
                "Failed to extract video frames. Exit code: $exitCode\nError message:\n$errorMessage"
            }
        } catch (e: Exception) {
            // แสดงข้อผิดพลาดที่เกิดขึ้น
            "An error occurred: ${e.message}"
        }
    }

    // คำนวณจำนวนเฟรมทั้งหมดโดยพิจารณา FPS และความยาวของวิดีโอ
    private fun estimateTotalFrames(FPS: Int): Int {
        val videoDurationSeconds = 180
        return FPS * videoDurationSeconds
    }

    // นับจำนวนไฟล์รูปภาพในโฟลเดอร์ที่กำหนด
    private fun countImageFiles(directoryPath: String): Int {
        val directory = File(directoryPath)

        return if (directory.isDirectory) {
            val imageFiles = directory.listFiles { file ->
                file.isFile && file.extension.toLowerCase() in listOf("jpg", "jpeg", "png", "bmp")
            }
            imageFiles?.size ?: 0
        } else {
            println("The specified folder was not found or does not have permission to access it.")
            0
        }
    }
}
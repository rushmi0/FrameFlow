import java.io.File

class FrameSnap(private val videoFileName: String, outputDirName: String) {

    // ที่อยู่ของ video
    private val videoPath = "src/main/kotlin/video/$videoFileName"

    // ที่อยู่ของ directory ที่จะบันทึกภาพ
    private val outputDirectory = "src/main/kotlin/$outputDirName/"

    // ฟังก์ชันสำหรับแปลง video เป็น frames
    fun snapVideo(fileExtension: String, FPS: Int): String {
        // สร้าง directory ถ้ายังไม่มี
        File(outputDirectory).mkdirs()

        // คำสั่ง ffmpeg สำหรับแปลง video เป็น frames
        val ffmpegCommand = "ffmpeg -i ${this.videoPath} -vf fps=${FPS} ${this.outputDirectory}/image_%05d.$fileExtension"

        return try {
            val process = Runtime.getRuntime().exec(ffmpegCommand)
            val exitCode = process.waitFor()  // รอให้ ffmpeg เสร็จสิ้นการทำงาน

            if (exitCode == 0) {
                val count = countImageFiles(outputDirectory)  // นับจำนวนไฟล์หลังจาก ffmpeg เสร็จสิ้น
                "Video frames extracted successfully. \nTotal number of files $count"
            } else {
                "Failed to extract video frames. Exit code: $exitCode"
            }
        } catch (e: Exception) {
            "An error occurred: ${e.message}"
        }
    }

    // ฟังก์ชันนับจำนวนไฟล์ภาพ
    private fun countImageFiles(directoryPath: String): Int {
        val directory = File(directoryPath)

        // ตรวจสอบว่า directory เป็นโฟลเดอร์และมีสิทธิ์เข้าถึง
        if (directory.isDirectory) {
            val imageFiles = directory.listFiles { file ->
                file.isFile && file.extension.toLowerCase() in listOf("jpg", "jpeg", "png", "bmp")
            }
            return imageFiles?.size ?: 0
        } else {
            println("The specified folder was not found or does not have permission to access it.")
            return 0
        }
    }
}

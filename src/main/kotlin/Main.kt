import java.io.File

fun main() {
    val videoPath = "src/main/kotlin/video/video.mp4"  // ที่อยู่ของ video
    val outputDirectory = "src/main/kotlin/image/"  // ที่อยู่ของ directory ที่จะบันทึกภาพ

    // สร้าง directory ถ้ายังไม่มี
    File(outputDirectory).mkdirs()

    // คำสั่ง ffmpeg สำหรับแปลง video เป็น frames ที่ FPS 1
    val ffmpegCommand = "ffmpeg -i $videoPath -vf fps=1 $outputDirectory/frame_%04d.jpg"

    try {
        val process = Runtime.getRuntime().exec(ffmpegCommand)
        val exitCode = process.waitFor()

        if (exitCode == 0) {
            println("Video frames extracted successfully.")
        } else {
            println("Failed to extract video frames. Exit code: $exitCode")
        }
    } catch (e: Exception) {
        println("An error occurred: ${e.message}")
    }
}

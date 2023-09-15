fun main() {
    // กำหนด Progress Bar เพื่อแสดงผลความคืบหน้าในการแปลงวิดีโอเป็นรูปภาพ
    val progressListener = object : FrameSnap.ProgressListener {
        override fun onProgress(progress: Int) {
            print("\rProgress: $progress%")
        }
    }

    // สร้าง FrameSnap instance
    val worker1 = FrameSnap("Metallica_EnterSandman.mp4", "image1")
    worker1.setProgressListener(progressListener)

    // กำหนดนามสกุลของไฟล์, FPS, และจำนวนเธรดของ CPU
    val fileExtension = "jpg" // * "jpg", "jpeg", "png", "bmp"
    val fps = 2
    val threads = 8

    // เรียกใช้งาน snapVideo สำหรับการแปลงวิดีโอเป็นรูปภาพ
    val result = worker1.snapVideo(fileExtension, fps, threads)
    println(result)  // แสดงผลลัพธ์
}
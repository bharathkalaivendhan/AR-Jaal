import android.app.Activity
import android.content.Context
import android.graphics.PointF
import android.media.Image
import android.net.Uri
import android.util.Log
import com.bk.arjaal.FRONT
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import kotlinx.coroutines.tasks.asDeferred

abstract class PoseAnalyzer(context : Context)   {
    abstract suspend fun analyse(image : Image):  List<PoseLandmark>

}
class PoseDetector(context: Activity) : PoseAnalyzer(context) {
     override suspend fun analyse(image: Image) :  List<PoseLandmark> {
        val builder = PoseDetectorOptions.Builder()

         lateinit var poseLandmark : List<PoseLandmark>
        val options = builder
            .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
            .build()
        val inputImage = InputImage.fromMediaImage(image,0)
        val detector = PoseDetection.getClient(options)
         detector.process(inputImage)
             .addOnSuccessListener {
                     pose->



                 poseLandmark = pose.allPoseLandmarks

                 Log.d(FRONT, poseLandmark.size.toString())

             //anchorToLoadJewel= createAnchor(poseLandmarkElbow!!.position.x, poseLandmarkElbow!!.position.y!!,frame)!!

             }.asDeferred()
             .await()
         image.close()
         return  poseLandmark

    }


}


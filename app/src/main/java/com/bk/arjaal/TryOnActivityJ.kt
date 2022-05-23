package com.bk.arjaal



import PoseDetector
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PointF
import android.media.Image
import android.net.Uri
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import com.google.ar.core.*
import com.google.ar.core.exceptions.NotYetAvailableException
import com.google.ar.core.exceptions.ResourceExhaustedException
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.ArSceneView
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.google.mlkit.vision.pose.PoseLandmark
import com.gorisse.thomas.sceneform.Position
import com.gorisse.thomas.sceneform.scene.await
import kotlinx.coroutines.*
import java.util.*

const val FRONT = "YOOGUYS"
lateinit var session : Session
var anchorToLoadJewel : Anchor? = null
lateinit var frame : Frame
lateinit var leftPose : PointF
lateinit var rightPose : PointF


val viewMatrix = FloatArray(16)
val projectionMatrix = FloatArray(16)
val viewProjectionMatrix = FloatArray(16)

lateinit var arFragment: ArFragment
lateinit var btnScan:Button
var scanButtonWasPressed = false

class TryOnActivityJ : AppCompatActivity(){

    private var poseResults: List<PoseLandmark>? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        val myIntent = intent

        var jewelleryInfo = myIntent.getSerializableExtra("jewelleryInfo") as Jewellery

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try_on_j)
        // Set a camera configuration that uses the front-facing camera.
        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment
        btnScan = findViewById(R.id.btnScan)
        fun tryCreateSession(): Session? {
            return try {

                Session(applicationContext)
            } catch (e: Exception) {
                null
            }
        }
        session = tryCreateSession() ?: return

        try {
            session.resume()
        } catch (e: Exception) {
        }


        btnScan.setOnClickListener {
            Log.d(FRONT,"Pressed")
            arFragment.arSceneView.scene.addOnUpdateListener {

                arFragment.instructionsController.isEnabled = false
                arFragment.arSceneView.setMaxFramesPerSeconds(24)

                lifecycleScope .launch {

                    delay(5000)
                    frame = try{
                        arFragment.arSceneView.arFrame!!
                    }

                    catch (e : NotYetAvailableException){
                        return@launch
                    }
                    // Get camera and projection matrices.
                    val camera = frame.camera
                    camera.getViewMatrix(viewMatrix, 0)
                    camera.getProjectionMatrix(projectionMatrix, 0, 0.01f, 100.0f)
                    Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
                    val image =
                        try {
                            frame.acquireCameraImage()
                        }
                        catch (e:ResourceExhaustedException){
                            return@launch
                        }
                        catch (e:Exception){
                            Log.d(FRONT,e.toString())
                            return@launch
                        }
                    //Returned image height
                    Log.d(FRONT, image.height.toString())
                    detectPose(image, this@TryOnActivityJ)
                    image.close()
                    // Log.d(FRONT, pose.toString())
                    val poses = poseResults
                    if (poses != null) {

                        for (pose in poses) {

                            // if(jewelleryInfo.Category == "Ear rings") {


                            if(pose.landmarkType == PoseLandmark.RIGHT_SHOULDER) {
                                leftPose = pose.position
                                val cpuCoordinates = floatArrayOf(pose.position.x+4f, pose.position.y)
                                val viewCoordinates = FloatArray(2)
                                val currentFrame = arFragment.arSceneView.arFrame
                                currentFrame?.transformCoordinates2d(
                                    Coordinates2d.IMAGE_PIXELS,
                                    cpuCoordinates,
                                    Coordinates2d.VIEW,
                                    viewCoordinates
                                )
                                detachModel()
                                val hits = currentFrame?.hitTest(viewCoordinates[0], viewCoordinates[1])
                                if (hits != null) {
                                    for (hit in hits) {
                                        try {

                                            loadJewel(
                                                jewelleryInfo,
                                                applicationContext,
                                                hit.createAnchor(), arFragment
                                            )
                                            Log.d(FRONT, "HITS CREATE ANCHOR DONE")
                                        }
                                        catch (e:Exception){
                                            Log.d(FRONT, e.toString())
                                        }

                                    }
                                }
                            }

//                            if (pose.landmarkType == PoseLandmark.RIGHT_EAR) {
//                                rightPose = pose.position
//                                val cpuCoordinates =
//                                    floatArrayOf(pose.position.x, pose.position.y)
//                                val viewCoordinates = FloatArray(2)
//                                val currentFrame = arFragment.arSceneView.arFrame
//                                currentFrame?.transformCoordinates2d(
//                                    Coordinates2d.IMAGE_PIXELS,
//                                    cpuCoordinates,
//                                    Coordinates2d.VIEW,
//                                    viewCoordinates
//                                )
//                                //detachModel()
//                                val hits = currentFrame?.hitTest(
//                                    viewCoordinates[0],
//                                    viewCoordinates[1]
//                                )
//                                if (hits != null) {
//                                    for (hit in hits) {
//
//                                        try {
//                                            loadJewel(
//                                                jewelleryInfo,
//                                                applicationContext,
//                                                hit.createAnchor(), arFragment
//                                            )
//                                            Log.d(FRONT, "HITS CREATE ANCHOR DONE")
//                                        } catch (e: Exception) {
//                                            Log.d(FRONT, e.toString())
//                                        }
//                                    }
//                                }
//                            }


                            // }
                            /*
                            else if(jewelleryInfo.Category == "Necklaces")
                            {
                                if (pose.landmarkType == PoseLandmark.LEFT_SHOULDER) {



                                    val cpuCoordinates =
                                        floatArrayOf(pose.position.x+5, pose.position.y+5)
                                    val viewCoordinates = FloatArray(2)
                                    val currentFrame = arFragment.arSceneView.arFrame
                                    currentFrame?.transformCoordinates2d(
                                        Coordinates2d.IMAGE_PIXELS,
                                        cpuCoordinates,
                                        Coordinates2d.VIEW,
                                        viewCoordinates
                                    )

                                    detachModel()
                                    val hits = currentFrame?.hitTest(
                                        viewCoordinates[0],
                                        viewCoordinates[1]
                                    )
                                    if (hits != null) {
                                        for (hit in hits) {

                                            try {
                                                loadJewel(
                                                    jewelleryInfo,
                                                    applicationContext,
                                                    hit.createAnchor(), arFragment
                                                )
                                                Log.d(FRONT, "HITS CREATE ANCHOR DONE")
                                            } catch (e: Exception) {
                                                Log.d(FRONT, e.toString())
                                            }
                                        }
                                    }
                                }
                            }
                            else if(jewelleryInfo.Category == "Rings")
                            {
                                if (pose.landmarkType == PoseLandmark.RIGHT_INDEX) {



                                    val cpuCoordinates =
                                        floatArrayOf(pose.position.x+5, pose.position.y+5)
                                    val viewCoordinates = FloatArray(2)
                                    val currentFrame = arFragment.arSceneView.arFrame
                                    currentFrame?.transformCoordinates2d(
                                        Coordinates2d.IMAGE_PIXELS,
                                        cpuCoordinates,
                                        Coordinates2d.VIEW,
                                        viewCoordinates
                                    )

                                    detachModel()
                                    val hits = currentFrame?.hitTest(
                                        viewCoordinates[0],
                                        viewCoordinates[1]
                                    )
                                    if (hits != null) {
                                        for (hit in hits) {

                                            try {
                                                loadJewel(
                                                    jewelleryInfo,
                                                    applicationContext,
                                                    hit.createAnchor(), arFragment
                                                )
                                                Log.d(FRONT, "HITS CREATE ANCHOR DONE")
                                            } catch (e: Exception) {
                                                Log.d(FRONT, e.toString())
                                            }
                                        }
                                    }
                                }
                            }
                            */
                        }
                        setScanningActive(false)

                    }

                }
                scanButtonWasPressed = false

            }}

    }
    suspend fun detectPose(image: Image?, context: Activity) {
        val poseAnalyzer = PoseDetector(context)

        if (image != null) {
            //Log.d(FRONT, image.height.toString())
            poseResults= poseAnalyzer.analyse(image)
            Log.d(FRONT, poseResults.toString())
        }
    }

}

suspend fun loadJewel(jewelleryInfo  : Jewellery,context: Context, anchor: Anchor, arFragment : ArFragment){



    val model =
        //  if(jewelleryInfo.Category == "Ear rings") {

        ModelRenderable.builder()
            .setSource(
                context,
                Uri.parse("Necklacecut.glb")
            )
            .setIsFilamentGltf(true)
            .await()

    //  }
    /*
    else if(jewelleryInfo.Category == "Necklaces"){


            ModelRenderable.builder()
                .setSource(
                    context,
                    Uri.parse("Necklacecut.glb")
                )
                .setIsFilamentGltf(true)
                .await()
        }
        else if(jewelleryInfo.Category == "Rings"){


            ModelRenderable.builder()
                .setSource(
                    context,
                    Uri.parse("ringred.glb")
                )
                .setIsFilamentGltf(true)
                .await()
        }*/
    //else{
    null
    //  }

    arFragment.arSceneView.scene.addChild(AnchorNode(anchor).apply {
        // Create the transformable model and add it to the anchor
        addChild(TransformableNode(arFragment.transformationSystem).apply {
            this.scaleController.minScale=0.1f
            this.scaleController.maxScale = 0.3f
            this.localScale =Vector3(0.01f,0.01f,0.01f)
            renderable = model

        })
    })

}

fun setScanningActive(active: Boolean) = when (active) {
    true -> {
        btnScan.isEnabled = false
        btnScan.setText("Scanning")
    }
    false -> {
        btnScan.isEnabled = true
        btnScan.setText("Scan")
    }
}



fun detachModel(){


    val children: List<Node> = ArrayList(arFragment.arSceneView.scene.children)
    for (node in children) {
        if (node is AnchorNode) {
            if (node.anchor != null) {
                node.anchor!!.detach()
            }
        }

    }
}

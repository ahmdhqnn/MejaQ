package org.d3ifcool.mejaq.ui.pemesanan

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import org.d3ifcool.mejaq.R
import org.d3ifcool.mejaq.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrCodeScannerScreen(navController: NavHostController) {

    var result by remember { mutableStateOf("") }
    val context = LocalContext.current

    var hasCamPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCamPermission = granted
    }

    LaunchedEffect(Unit) {
        launcher.launch(Manifest.permission.CAMERA)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            contentDescription = stringResource(
                                id = org.d3ifcool.shared.R.string.kembali
                            ),
                            tint = Color(0xFFD61355)
                        )
                    }
                },
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_mejaq),
                            contentDescription = "Logo MejaQ",
                            modifier = Modifier
                                .size(120.dp)
                                .padding(end = 6.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFFDFDFE),
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFDFDFE))
                .navigationBarsPadding()
                .padding(innerPadding)
        ) {
            if (hasCamPermission) {
                QrScanner(
                    data = { result = it },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // overlay scanner
        ScannerMask()
        AnimatedScanLine()

        if (result.isNotEmpty()) {
            navController.navigate(Screen.Order.withTableNumber(result))
        }
    }
}

@Composable
fun QrScanner(data: (String) -> Unit, modifier: Modifier) {
    var result by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }

    AndroidView(factory = { it ->
        val previewView = PreviewView(it)
        val preview = Preview.Builder().build()
        val selector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
        preview.surfaceProvider = previewView.surfaceProvider
        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(it),
            QrCodeAnalyzer { it ->
                result = it
            }
        )
        try {
            cameraProviderFuture.get().bindToLifecycle(
                lifecycleOwner,
                selector,
                preview,
                imageAnalysis
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        previewView
    },
        modifier = modifier
    )
    data(result)
}

@Composable
fun AnimatedScanLine(
    scanWindowSize: Dp = 300.dp,
    scanLineHeight: Dp = 4.dp,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "scanLineTransition")

    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scanLineAnimation"
    )

    val maxTravel = remember(scanWindowSize, scanLineHeight) {
        scanWindowSize - scanLineHeight
    }
    val currentYOffset by remember(offsetY) {
        derivedStateOf {
            maxTravel * offsetY
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(scanWindowSize)
                .border(2.dp, Color.White)
                .background(Color.Transparent)
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(scanLineHeight)
                    .offset(y = currentYOffset)
                    .background(Color(0xFFD61355))
            )
        }
    }
}

@Composable
fun ScannerMask(scanWindowSize: Dp = 300.dp) {
    val density = LocalDensity.current
    val scanWindowSizePx = with(density) { scanWindowSize.toPx() }
    val overlayColor = Color.Black.copy(alpha = 0.6f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawWithContent {
                drawRect(color = overlayColor)

                val center = size.center
                val rect = Rect(
                    center.x - scanWindowSizePx / 2,
                    center.y - scanWindowSizePx / 2,
                    center.x + scanWindowSizePx / 2,
                    center.y + scanWindowSizePx / 2
                )

                //Hapus bagian kotak scan dari overlay
                drawIntoCanvas { canvas ->
                    canvas.drawRect(rect, Paint().apply {
                        blendMode = BlendMode.Clear
                    })
                }
            }
    )
}
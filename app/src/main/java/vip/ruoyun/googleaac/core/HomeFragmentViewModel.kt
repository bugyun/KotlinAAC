package vip.ruoyun.googleaac.core

import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.work.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.io.File

const val TAG_OUTPUT = "OUTPUT"
const val IMAGE_MANIPULATION_WORK_NAME = "IMAGE_MANIPULATION_WORK_NAME"
const val OUTPUT_PATH = "OUTPUT_PATH"

class HomeFragmentViewModel(app: App) : AndroidViewModel(app) {


    val user: LiveData<User> = liveData {
        val data = User("")
        emit(data)
    }


    suspend fun testData() = coroutineScope {
        val deferreds = listOf(     // fetch two docs at the same time
            async { },  // async returns a result for the first doc
            async { }   // async returns a result for the second doc
        )
        deferreds.awaitAll()
    }


    private val workManager: WorkManager = WorkManager.getInstance(app)

    val outputWorkInfoItems: LiveData<List<WorkInfo>>

    init {
        outputWorkInfoItems = workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)
    }

    fun applyBlur() {


        val beginUniqueWork = workManager.beginUniqueWork(
            IMAGE_MANIPULATION_WORK_NAME, ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequest.from(CleanupWorker::class.java)
        )
//        beginUniqueWork.then()




        beginUniqueWork.enqueue()
    }

}

class CleanupWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    override fun doWork(): Result {

        // Makes a notification when the work starts and slows down the work so that
        // it's easier to see each WorkRequest start, even on emulated devices

        return try {
            val outputDirectory = File(applicationContext.filesDir, OUTPUT_PATH)
            if (outputDirectory.exists()) {
                val entries = outputDirectory.listFiles()
                if (entries != null) {
                    for (entry in entries) {
                        val name = entry.name
                        if (name.isNotEmpty() && name.endsWith(".png")) {
                            val deleted = entry.delete()
                        }
                    }
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}

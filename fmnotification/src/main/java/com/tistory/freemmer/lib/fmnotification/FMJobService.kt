package com.tistory.freemmer.lib.fmnotification

import android.content.Context
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService

/**
 * Created by freemmer on 24/01/2019.
 * History
 *    - 24/01/2019 Create file
 */
class FMJobService : JobService() {

    companion object {
        fun scheduleJob(context: Context, tag: String) {
            val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(context))
            val job = dispatcher.newJobBuilder()
                .setService(FMJobService::class.java)
                .setTag(tag)
                .build()
            dispatcher.schedule(job)
        }
    }

    override fun onStartJob(jobParameters: JobParameters): Boolean {
//        Log.d(TAG, "Performing long running task in scheduled job")
//        // TODO(developer): add long running task here.
        return false
    }

    override fun onStopJob(jobParameters: JobParameters): Boolean {
        return false
    }

}
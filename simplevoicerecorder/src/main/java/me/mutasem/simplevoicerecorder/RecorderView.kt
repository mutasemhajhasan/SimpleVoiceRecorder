package me.mutasem.simplevoicerecorder

import android.content.Context
import android.media.MediaRecorder

import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import java.io.File


class RecorderView : androidx.appcompat.widget.AppCompatImageButton, View.OnClickListener {
    private var audioSource: Int? = null
    private var audioEncoder: Int? = null
    private var outputFormat: Int? = null
    private val TAG = "RecorderView"
    private var isRecording = false
    private var recordIconRes = R.drawable.ic_record
    private var stopIconRes = R.drawable.ic_stop
    private var recorder = MediaRecorder()
    private var currentFile: File? = null
    private var recordingListener: RecordingListener? = null

    constructor(context: Context) : super(context) {
        mInit(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mInit(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mInit(context, attrs, defStyleAttr)
    }

    private fun mInit(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs != null) {
            val a = getContext().theme.obtainStyledAttributes(
                attrs,
                R.styleable.RecorderView,
                defStyleAttr,
                0
            )

            try {
                recordIconRes =
                    a.getResourceId(R.styleable.RecorderView_recordIconRes, R.drawable.ic_record)
                stopIconRes =
                    a.getResourceId(R.styleable.RecorderView_stopIconRes, R.drawable.ic_stop)
                setImageResource(recordIconRes)
                scaleType = ScaleType.FIT_XY
                setOnClickListener(this)
                setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            } catch (e: Exception) {
                Log.e(TAG, "Error", e)
            } finally {
                a.recycle()
            }
        }
    }

    override fun onClick(p0: View?) {
        if (isRecording)
            stop()
        else
            start()
    }

    private fun start() {
        try {
            if (currentFile == null)
                currentFile = Utils.createFile()
            setUpRecorder()
            recorder.start()
            isRecording = true
            setImageResource(stopIconRes)
            recordingListener?.onRecordingStarted()
        } catch (e: Exception) {
            recordingListener?.onError(e)
        }
    }


    private fun stop() {
        try {
            isRecording = false
            recorder.stop()
            recorder.reset()
            setImageResource(recordIconRes)
            recordingListener?.onRecordingCompleted(currentFile)
        } catch (e: Exception) {
            recordingListener?.onError(e)
        }
    }

    private fun setUpRecorder() {
        recorder.setAudioSource(if (audioSource == null) MediaRecorder.AudioSource.MIC else audioSource as Int);
        recorder.setOutputFormat(if (outputFormat == null) MediaRecorder.OutputFormat.MPEG_4 else outputFormat as Int);
        recorder.setAudioEncoder(if (audioEncoder == null) MediaRecorder.AudioEncoder.AAC else audioEncoder as Int);
        recorder.setOutputFile((currentFile as File).absolutePath)
        recorder.prepare()
    }

    fun getSavedFile(): File? {
        return currentFile
    }

    fun release() {
        recorder.release()
    }

    fun setSavePath(file: File) {
        this.currentFile = file
    }

    fun setSavePath(path: String) {
        this.currentFile = File(path)
    }

    fun setRecordingListener(recordingListener: RecordingListener) {
        this.recordingListener = recordingListener
    }

    public interface RecordingListener {
        fun onRecordingStarted()
        fun onRecordingCompleted(file: File?)
        fun onError(error: Throwable)
    }

    fun setOutputFormat(format: Int) {
        outputFormat = format
    }

    fun setAudioEncoder(encoder: Int) {
        this.audioEncoder = encoder
    }

    fun setAudioSource(source: Int) {
        this.audioSource = source
    }
}
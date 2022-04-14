package com.example.canteen.utilities

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.BaseColumns
import android.provider.DocumentsContract
import android.provider.MediaStore


class ParseUriPath {
    companion object{
        /**
         * 根据Uri获取图片的绝对路径
         *
         * @param context 上下文对象
         * @param uri     图片的Uri
         * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
         */
        fun getRealPathFromUri(context: Context, uri: Uri): String? {

            // Uri分为三个部分   域名://主机名/路径/id
            // content://media/extenral/images/media/17766
            // content://com.android.providers.media.documents/document/image:2706
            // file://com.xxxx.xxxxx ---- 7.0 有限制
            val sdkVersion = Build.VERSION.SDK_INT
            return if (sdkVersion >= 19) { // api >= 19
                getRealPathFromUriAboveApi19(context, uri)
            } else { // api < 19
                getRealPathFromUriBelowAPI19(context, uri)
            }
        }
        /**
         * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
         *
         * @param context 上下文对象
         * @param uri     图片的Uri
         * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
         */
        private fun getRealPathFromUriBelowAPI19(context: Context, uri: Uri): String? {
            val scheme: String? = uri.scheme
            var data: String? = null
            when {
                scheme == null -> data = uri.path
                ContentResolver.SCHEME_FILE == scheme -> {
                    data = uri.path
                }
                ContentResolver.SCHEME_CONTENT == scheme -> {
                    data = getDataColumn(context, uri, null, null)
                }
            }
            return data
        }

        /**
         * 适配api19及以上,根据uri获取图片的绝对路径
         *
         * @param context 上下文对象
         * @param uri     图片的Uri
         * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
         */
        @SuppressLint("NewApi")
        private fun getRealPathFromUriAboveApi19(context: Context, uri: Uri): String? {
            var filePath: String? = null
            if (DocumentsContract.isDocumentUri(context, uri)) {
                // 如果是document类型的 uri, 则通过document id来进行处理
                val documentId = DocumentsContract.getDocumentId(uri)
                if (isMediaDocument(uri)) { // MediaProvider
                    val divide = documentId.split(":".toRegex()).toTypedArray()
                    val type = divide[0]
                    var mediaUri: Uri? = null
                    mediaUri = if ("image" == type) {
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    } else {
                        return null
                    }
                    val selection = BaseColumns._ID + "=?"
                    val selectionArgs = arrayOf(divide[1])
                    filePath = getDataColumn(context, mediaUri, selection, selectionArgs)
                } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                    val contentUri: Uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(documentId)
                    )
                    filePath = getDataColumn(context, contentUri, null, null)
                } else if (isExternalStorageDocument(uri)) {
                    val split = documentId.split(":".toRegex()).toTypedArray()
                    if (split.size >= 2) {
                        val type = split[0]
                        if ("primary".equals(type, ignoreCase = true)) {
                            filePath =
                                Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                        }
                    }
                }
            } else if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme(), ignoreCase = true)) {
                // 如果是 content 类型的 Uri
                filePath = getDataColumn(context, uri, null, null)
            } else if (ContentResolver.SCHEME_FILE == uri.getScheme()) {
                // 如果是 file 类型的 Uri,直接获取图片对应的路径
                filePath = uri.getPath()
            }
            return filePath
        }

        /**
         * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
         * @return
         */
        private fun getDataColumn(
            context: Context,
            uri: Uri,
            selection: String?,
            selectionArgs: Array<String>?
        ): String? {
            var path: String? = null
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            var cursor: Cursor? = null
            try {
                cursor =
                    context.contentResolver.query(uri, projection, selection, selectionArgs, null)
                if (cursor != null && cursor.moveToFirst()) {
                    val columnIndex: Int = cursor.getColumnIndexOrThrow(projection[0])
                    path = cursor.getString(columnIndex)
                }
            } catch (e: Exception) {
                if (cursor != null) {
                    cursor.close()
                }
            }
            return path
        }

        /**
         * @param uri the Uri to check
         * @return Whether the Uri authority is MediaProvider
         */
        private fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.getAuthority()
        }

        /**
         * @param uri the Uri to check
         * @return Whether the Uri authority is DownloadsProvider
         */
        private fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.getAuthority()
        }

        private fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.getAuthority()
        }
    }
}
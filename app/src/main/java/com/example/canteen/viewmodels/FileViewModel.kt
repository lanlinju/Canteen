package com.example.canteen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.canteen.respositories.FileRepository

class FileViewModel : ViewModel() {
    private val fileRepository = FileRepository()
    val uploadImageInfo: LiveData<String> get() = fileRepository.uploadInfo

    fun uploadFile(filePath: String) {
        fileRepository.uploadFile(filePath)
    }
}
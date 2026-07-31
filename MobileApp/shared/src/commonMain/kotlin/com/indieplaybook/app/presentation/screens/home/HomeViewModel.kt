package com.indieplaybook.app.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indieplaybook.app.data.repository.CreditRepository
import com.indieplaybook.app.data.repository.GenerationRepository
import com.indieplaybook.app.designsystem.components.addorchosefilecontainer.FileItemUiState
import com.indieplaybook.app.domain.exceptions.CreditRequiredException
import com.indieplaybook.app.domain.exceptions.PurchaseRequiredException
import com.indieplaybook.app.domain.exceptions.UnAuthorizedException
import com.indieplaybook.app.domain.model.generation.GenerationInput
import com.indieplaybook.app.domain.model.generation.generationInput
import com.indieplaybook.app.generated.resources.Res
import com.indieplaybook.app.generated.resources.home_error_choosing_file
import com.indieplaybook.app.generated.resources.home_error_file_size
import com.indieplaybook.app.root.AppGlobalUiState
import com.indieplaybook.app.util.Constants
import com.indieplaybook.app.util.UiMessage
import com.indieplaybook.app.util.file.FileManager
import com.indieplaybook.app.util.file.absolutePathCommon
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.extension
import io.github.vinceglb.filekit.size
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val generationRepository: GenerationRepository,
    private val creditRepository: CreditRepository,
    private val fileManager: FileManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeCreditBalance()
    }

    fun onUiEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.OnClickGenerate -> generate()

            HomeUiEvent.OnClickToolbarCredits -> {
                _uiState.update { it.copy(isMoreCreditsRequired = true) }
            }

            is HomeUiEvent.OnPromptChanged -> {
                _uiState.update { it.copy(prompt = event.value) }
            }

            is HomeUiEvent.OnReferenceImageRemoved -> {
                _uiState.update { it.copy(referenceImage = it.referenceImage.removeFile(event.file)) }
            }

            is HomeUiEvent.OnReferenceImageSelected -> {
                saveFileLocallyAndUpdateState(event.file)
            }
        }
    }

    fun onPremiumRequiredHandled() {
        _uiState.update { it.copy(isPremiumRequired = false) }
    }

    fun onMoreCreditsRequiredHandled() {
        _uiState.update { it.copy(isMoreCreditsRequired = false) }
    }

    fun onGenerationResultHandled() {
        _uiState.update { it.copy(generatedResult = null) }
    }

    fun onAuthRequiredHandled() {
        _uiState.update { it.copy(isAuthRequired = false) }
    }

    private fun observeCreditBalance() = viewModelScope.launch {
        creditRepository.balance.collectLatest { creditBalance ->
            _uiState.update { it.copy(creditBalance = creditBalance) }
        }
    }

    private fun saveFileLocallyAndUpdateState(file: PlatformFile?) = viewModelScope.launch {
        if (file == null) return@launch

        if (file.size() > Constants.MAX_FILE_UPLOAD_SIZE) {
            AppGlobalUiState.showUiMessage(UiMessage.Resource(Res.string.home_error_file_size))
            return@launch
        }
        val originalFileAbsolutePath = file.absolutePathCommon()
        val fileNameWithExtension =
            fileManager.createNewUniqueFileNameWithExtension(file.extension)

        _uiState.update {
            it.copy(
                referenceImage = it.referenceImage.addFile(
                    FileItemUiState(
                        path = originalFileAbsolutePath,
                        nameWithExtension = fileNameWithExtension,
                        isUploading = true,
                    ),
                ),
            )
        }

        fileManager.copyFileToInternalDirectory(
            originalFileAbsolutePath = originalFileAbsolutePath,
            newFileName = fileNameWithExtension,
        ).onSuccess { copiedFileNameWithExtension ->
            _uiState.update {
                it.copy(
                    referenceImage = it.referenceImage.updateFile(
                        FileItemUiState(
                            path = fileManager.getAbsoluteFilePathRelativeToInternal(
                                copiedFileNameWithExtension,
                            ),
                            nameWithExtension = fileNameWithExtension,
                            isUploading = false,
                        ),
                    ),
                )
            }
        }.onFailure {
            AppGlobalUiState.showUiMessage(UiMessage.Resource(Res.string.home_error_choosing_file))
            _uiState.update {
                it.copy(
                    referenceImage = it.referenceImage.removeFile(
                        FileItemUiState(nameWithExtension = fileNameWithExtension, path = ""),
                    ),
                )
            }
        }
    }

    private fun generate() = viewModelScope.launch {
        _uiState.update { it.copy(isGenerationInProgress = true) }
        generationRepository.generate(_uiState.value.buildGenerationInput())
            .onSuccess { generationOutput ->
                _uiState.update {
                    it.copy(
                        generatedResult = generationOutput,
                        isGenerationInProgress = false,
                        prompt = "",
                        referenceImage = it.referenceImage.removeAllFiles(),
                    )
                }
            }.onFailure { error -> onError(error) }
    }

    private fun HomeUiState.buildGenerationInput(): GenerationInput = generationInput {
        stringParam(key = KEY_PROMPT, value = prompt)
        referenceImage.files.forEach { fileItem ->
            image(key = KEY_REFERENCE_IMAGE, fileNameWithExtension = fileItem.nameWithExtension)
        }
    }

    private fun onError(error: Throwable) {
        when (error) {
            is UnAuthorizedException -> {
                _uiState.update {
                    it.copy(isAuthRequired = true, isGenerationInProgress = false)
                }
            }

            is PurchaseRequiredException -> {
                _uiState.update {
                    it.copy(isPremiumRequired = true, isGenerationInProgress = false)
                }
            }

            is CreditRequiredException -> {
                _uiState.update {
                    it.copy(isMoreCreditsRequired = true, isGenerationInProgress = false)
                }
            }

            else -> {
                _uiState.update { it.copy(isGenerationInProgress = false) }
                AppGlobalUiState.showUiMessage(UiMessage.Message(error.message))
            }
        }
    }

    companion object {
        const val KEY_PROMPT = "prompt"

        // The Replicate model's input field for images. google/nano-banana expects `image_input`;
        // change this to match your model's schema.
        const val KEY_REFERENCE_IMAGE = "image_input"
    }
}

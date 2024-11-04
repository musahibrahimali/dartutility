package com.github.softwarearoma.dartutility.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.vfs.VirtualFile
import java.io.File

class GenerateIterableIndexAction : AnAction("Generate Iterable Index") {
    override fun actionPerformed(event: AnActionEvent) {
        // Get the selected virtual file (directory) in the Project view
        val folder: VirtualFile? = event.getData(CommonDataKeys.VIRTUAL_FILE)

        // Ensure the folder is not null and is indeed a directory
        if (folder != null && folder.isDirectory) {
            generateIndexFilesRecursively(File(folder.path))
        }
    }

    private fun generateIndexFilesRecursively(folder: File) {
        val dartFiles = folder.listFiles { file ->
            file.extension == "dart" && file.name != "index.dart" // Exclude any existing index.dart
        } ?: return

        val indexFile = File(folder, "index.dart")
        indexFile.writeText("") // Clear existing contents of index.dart

        // Export each Dart file in the current directory, except index.dart
        dartFiles.forEach { file ->
            indexFile.appendText("export '${file.name}';\n")
        }

        // Traverse subdirectories and generate their own index.dart files
        folder.listFiles { file -> file.isDirectory }?.forEach { subfolder ->
            generateIndexFilesRecursively(subfolder)
            if (File(subfolder, "index.dart").exists()) {
                indexFile.appendText("export '${subfolder.name}/index.dart';\n")
            }
        }
    }
}

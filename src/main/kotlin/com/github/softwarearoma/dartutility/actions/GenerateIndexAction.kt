package com.github.softwarearoma.dartutility.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.vfs.VirtualFile
import java.io.File


class GenerateIndexAction : AnAction("Generate Index") {
    override fun actionPerformed(event: AnActionEvent) {
        // Get the selected virtual file (directory) in the Project view
        val folder: VirtualFile? = event.getData(CommonDataKeys.VIRTUAL_FILE)

        // Ensure the folder is not null and is indeed a directory
        if (folder != null && folder.isDirectory) {
            generateIndexFile(File(folder.path))
        }
    }

    private fun generateIndexFile(folder: File) {
        val dartFiles = folder.listFiles { file -> file.extension == "dart" } ?: return
        val indexFile = File(folder, "index.dart")
        indexFile.writeText("") // Clear existing contents

        dartFiles.forEach { file ->
            indexFile.appendText("export '${file.name}';\n")
        }
    }
}
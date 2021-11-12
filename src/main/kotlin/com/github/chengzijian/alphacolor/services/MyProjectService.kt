package com.github.chengzijian.alphacolor.services

import com.intellij.openapi.project.Project
import com.github.chengzijian.alphacolor.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}

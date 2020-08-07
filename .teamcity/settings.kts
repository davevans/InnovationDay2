/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import java.io.File

version = "2020.1"

/*
project {
    buildType(HelloWorld)
}

object HelloWorld: BuildType({
    name = "Hello world"
    steps {
        script {
            scriptContent = "echo 'Hello world!'"
        }
    }
})
*/

project {

    /* Read yml files to create subprojects */
    val projectsRoot = File(DslContext.baseDir, "/Projects")
    addProject(projectsRoot, this)

}


/*
* Recursively adds projects to match the file system
*/
fun addProject(currentDirectory: File, parent: Project) : Unit {
    val subdirectories = currentDirectory.listFiles()

    subdirectories?.forEach{
        if(it.isDirectory)
        {
            println("Project Directory --> $it")

            val projectDto : ProjectDto = YAMLUtil.parseDto("/Projects/Develop/_Project.yaml", ProjectDto::class)
            //TODO:

            println("Project metadata $projectDto")

            val projectName = projectDto.name
            val sub = parent.subProject {
                id(projectName)
                name = projectName
            }
            addProject(it, sub)
        }
    }

}

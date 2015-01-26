import org.gradle.api.Plugin
import org.gradle.api.Project

class DuplicateClassesPlugin implements Plugin<Project> {
  void apply(Project project) {
    project.task('duplicateClasses', type: DuplicateClassesTask)
  }
}

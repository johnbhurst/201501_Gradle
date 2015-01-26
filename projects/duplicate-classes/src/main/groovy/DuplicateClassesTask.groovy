import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskAction

class DuplicateClassesTask extends DefaultTask {
  List<String> configurations       // Default: all configurations are checked
  List<String> ignoreClasses = []   // Class name patterns to ignore (note: treated as regex patterns)
  LogLevel logLevel = LogLevel.WARN // Default: duplicates are reported as warnings
  boolean fail = false              // Set true to fail build on duplicates

  void logDuplicate(String message) {
    logger.log(logLevel, message)
  }

  @TaskAction
  void taskAction() {
    logger.info "Analysing for duplicate classes..."
    logger.info "  Configurations: ${configurations?.join(',')}"
    logger.info "  Ignoring classes: ${ignoreClasses.join(',')}"
    boolean duplicatesFound = false
    project.configurations.each {Configuration configuration ->
      if (!configurations || configuration.name in configurations) {
        // Below we select only Files among the dependencies, assuming they are JAR files.
        // We skip directories, which are sometimes present in builds, such as with GWT.
        // Maybe we should improve this task to deal with classpath directories too,
        // but that case does not seem to be relevant to the problem this task is solving.
        Collection<File> jarFiles = configuration.files(configuration.allDependencies as Dependency[]).findAll {it.file}
        new DuplicateClasses(jarFiles, ignoreClasses).duplicates.each {className, dataFiles ->
          duplicatesFound = true
          logDuplicate "[${configuration.name}] Class $className has ${dataFiles.size()} different definitions:"
          dataFiles.each {md5, jarFileNames ->
            logDuplicate "  version in ${jarFileNames.join(",")}"
          }
        }
      }
    }
    if (fail && duplicatesFound) {
      throw new GradleException("Inconsistent duplicate classes found.")
    }
  }
}

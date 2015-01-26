import org.springframework.core.io.FileSystemResource

import java.util.zip.ZipEntry

import static org.apache.commons.codec.digest.DigestUtils.md5Hex

/**
 * Worker class for DuplicateClassesTask.
 */
class DuplicateClasses {

  Iterable<File> jarFiles
  Iterable<String> ignoreClasses

  DuplicateClasses(Iterable<File> jarFiles, Iterable<String> ignoreClasses) {
    this.jarFiles = jarFiles
    this.ignoreClasses = ignoreClasses
  }

  DuplicateClasses(Iterable<File> jarFiles) {
    this(jarFiles, [])
  }

  /**
   * Converts a path/file in a JAR file to a fully-qualified class name.
   * @param classFile
   * @return
   */
  static String className(String classFile) {
    return classFile.replaceAll('/', '.').replaceAll(/.class$/, '')
  }

  /**
   * Returns true if the given class name matches one of the ignore pattens.
   * @param className
   * @return
   */
  boolean ignoredClass(String className) {
    return ignoreClasses.any {className =~ it}
  }

  /**
   * Returns identically-named classes with different binary content in a Map structure.
   * The first level map key is the (fully qualified) class name.
   * The second level map key is the MD5 of the binary class content.
   * The second level map value is a list of input files having the class with that content.
   * Only entries having more than one distinct binary content are returned,
   * so classes in input that do not have duplicate clashes are not returned.
   * @return
   */
  Map<String, Map<String, List<String>>> getDuplicates() {
    Map<String, Map<String, List<String>>> result = [:].withDefault {[:].withDefault {[]}}
    jarFiles.each {File jarFile ->
      new ZipInputStreamTemplate(new FileSystemResource(jarFile)).withZipInputStream {InputStream is, ZipEntry entry ->
        if (entry.name.endsWith("class")) {
          String className = className(entry.name)
          if (!ignoredClass(className)) {
            String md5 = md5Hex(is)
            result[className][md5] << jarFile.name
          }
        }
      }
    }
    return result.findAll {className, dataFiles -> dataFiles.size() > 1}
  }
}

import org.junit.Before
import org.junit.Test

class DuplicateClassesTest {

  File jarFilesDir

  @Before
  void setupJarFilesDir() {
    jarFilesDir = new File("build/jarfiles")
    jarFilesDir.mkdirs()
  }

  File jarFile(String name, Closure entries) {
    File result = new File(jarFilesDir, name)
    result.bytes = new ZipBuilder().zip(entries).data
    return result
  }

  File jarFile(String name, String resourceName, String data) {
    return jarFile(name) {
      entry(resourceName, data)
    }
  }

  @Test
  void testOk() {
    File file1 = jarFile("file1.jar") {
      entry("au/com/redenergy/Test1.class", "data1")
      entry("au/com/redenergy/Test2.class", "data2a")
      entry("au/com/redenergy/Test3.class", "data3")
    }
    File file2 = jarFile("file2.jar") {
      entry("au/com/redenergy.Test1.class", "data1")
      entry("au/com/redenergy.Test2.class", "data2b")
      entry("au/com/redenergy.Test4.class", "data4")
    }

    assert new DuplicateClasses([file1, file2]).duplicates == [
      "au.com.redenergy.Test2": ["ce033f92257e45fc8051bbb26222788c": ["file1.jar"], "fe67cc5f601162f97657d177398f47b2": ["file2.jar"]]
    ]
  }

  @Test
  void testMultipleOccurrences() {
    File file1 = jarFile("file1.jar", "au/com/redenergy/Test1.class", "data1a")
    File file2 = jarFile("file2.jar", "au/com/redenergy/Test1.class", "data1b")
    File file3 = jarFile("file3.jar", "au/com/redenergy/Test1.class", "data1a")
    File file4 = jarFile("file4.jar", "au/com/redenergy/Test1.class", "data1b")

    assert new DuplicateClasses([file1, file2, file3, file4]).duplicates == [
      "au.com.redenergy.Test1": ["9ea421ed76cbe4881089a649208b75f7": ["file1.jar", "file3.jar"], "025b656fc39c578131137b4c5e297a24": ["file2.jar", "file4.jar"]]
    ]
  }

  @Test
  void testNonClassResources() {
    File file1 = jarFile("file1.jar", "au/com/redenergy/Test1.xml", "data1a")
    File file2 = jarFile("file2.jar", "au/com/redenergy/Test1.xml", "data1b")

    assert new DuplicateClasses([file1, file2]).duplicates == [:]
  }

  @Test
  void testDifferentPackage() {
    File file1 = jarFile("file1.jar", "au/com/redenergy1/Test.class", "data1a")
    File file2 = jarFile("file2.jar", "au/com/redenergy2/Test.class", "data1b")

    assert new DuplicateClasses([file1, file2]).duplicates == [:]
  }

  @Test
  void testIgnoreClass() {
    File file1 = jarFile("file1.jar", "au/com/redenergy/Test1.class", "data1a")
    File file2 = jarFile("file2.jar", "au/com/redenergy/Test1.class", "data1b")

    assert new DuplicateClasses([file1, file2], ["au.com.redenergy.Test1"]).duplicates == [:]
  }

  @Test
  void testIgnoreClassPattern() {
    File file1 = jarFile("file1.jar", "au/com/redenergy/Test1.class", "data1a")
    File file2 = jarFile("file2.jar", "au/com/redenergy/Test1.class", "data1b")

    assert new DuplicateClasses([file1, file2], ["au.com.redenergy.Test.*"]).duplicates == [:]
  }

  @Test
  void testIgnorePackagePattern() {
    File file1 = jarFile("file1.jar", "au/com/redenergy/Test1.class", "data1a")
    File file2 = jarFile("file2.jar", "au/com/redenergy/Test1.class", "data1b")

    assert new DuplicateClasses([file1, file2], ["au.com.red.*"]).duplicates == [:]
  }

}

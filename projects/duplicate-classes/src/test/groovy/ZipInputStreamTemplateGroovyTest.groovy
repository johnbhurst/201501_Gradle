import org.springframework.core.io.ClassPathResource
import org.junit.Test

class ZipInputStreamTemplateGroovyTest {

  def template = new ZipInputStreamTemplate(new ClassPathResource("test.zip", getClass()))

  @Test
  void testWithZipInputStream() {
    def content = []
    template.withZipInputStream {entry ->
      content << "${entry.name}:${entry.size}"
    }
    assert ["one.txt:4", "two.txt:4"] == content
  }

  @Test
  void testWithZipInputStreamWithStream() {
    def content = []
    template.withZipInputStream {zis, entry ->
      content << "${entry.name}:${entry.size}:${zis.text}"
    }
    assert ["one.txt:4:one\n", "two.txt:4:two\n"] == content
  }

  @Test
  void testWithNoArgs() {
    def content = []
    template.withZipInputStream {
      content << "${it.name}:${it.size}"
    }
    assert ["one.txt:4", "two.txt:4"] == content
  }

}

import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import org.junit.Test
import org.springframework.core.io.ByteArrayResource

class ZipBuilderTest {

  @Test
  void testOk() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream()
    new ZipBuilder(baos).zip {
      entry("one") {os ->
        os.bytes = "Hello".bytes
      }
    }

    def content = []
    new ZipInputStreamTemplate(new ByteArrayResource(baos.toByteArray())).withZipInputStream {InputStream zis, ZipEntry entry ->
      content << "${entry.name}:${entry.size}:${zis.text}"
    }
    assert content == ["one:-1:Hello"]
  }

  //@Test why doesn't this work?
  void testWithSize() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream()
    new ZipBuilder(baos).zip {
      entry("one", size: 10 as long) {os ->
        os.bytes = "Hello".bytes
      }
    }

    def content = []
    new ZipInputStreamTemplate(new ByteArrayResource(baos.toByteArray())).withZipInputStream {InputStream zis, ZipEntry entry ->
      content << "${entry.name}:${entry.size}:${zis.text}"
    }
    assert content == ["one:10:Hello"]
  }

  @Test
  void testWithDate() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream()
    new ZipBuilder(baos).zip {
      entry("one", time: Date.parse("yyyy-MM-dd HH:mm:ss", "2010-11-17 12:34:56").time) {os ->
        os.bytes = "Hello".bytes
      }
    }

    def content = []
    new ZipInputStreamTemplate(new ByteArrayResource(baos.toByteArray())).withZipInputStream {InputStream zis, ZipEntry entry ->
      content << "${entry.name}:${new Date(entry.time).format("yyyy-MM-dd HH:mm:ss")}:${zis.text}"
    }
    assert content == ["one:2010-11-17 12:34:56:Hello"]

  }

}

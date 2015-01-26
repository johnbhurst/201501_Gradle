import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.zip.ZipEntry;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class ZipInputStreamTemplateTest {

  @Test
  public void testOk() {
    final Iterator<String> contentIter = Arrays.asList("one", "two").iterator();
    new ZipInputStreamTemplate(new ClassPathResource("test.zip", getClass())).execute(new ZipInputStreamCallback() {
      public Object doWithZipEntry(InputStream is, ZipEntry entry) throws Exception {
        String expectedContent = contentIter.next();
        assertEquals(expectedContent + ".txt", entry.getName());
        String s = new String(ResourceUtils.getBytes(new InputStreamInputStreamSource(is))).trim();
        assertEquals(expectedContent, s);
        return null;
      }
    });
  }

}


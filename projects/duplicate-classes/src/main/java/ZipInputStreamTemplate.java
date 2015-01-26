import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import groovy.lang.Closure;
import org.springframework.core.io.InputStreamSource;

/**
 * A template class for executing operations on ZIP streams.
 * <p>This class is modeled on the idea of template classes in the Spring Framework.
 * </p>
 * <p>This class separates the resource acquisition and release, as well as exception
 * handling, from the code doing the actual work.
 * The code to do the actual work is provided as a strategy,
 * by way of the <code>ZipInputStreamCallback</code>.
 * </p>
 */
public class ZipInputStreamTemplate {
  private InputStreamSource source;

  public ZipInputStreamTemplate(InputStreamSource source) {
    this.source = source;
  }

  public void execute(final ZipInputStreamCallback callback) {
    new InputStreamTemplate(source).execute(new InputStreamCallback() {
      public Object doInInputStream(InputStream inputStream) {
        try {
          ZipInputStream zis = new ZipInputStream(inputStream);
          ZipEntry entry = zis.getNextEntry();
          while (entry != null) {
            InputStream is = new NonClosingInputStream(zis);
            callback.doWithZipEntry(is, entry);
            entry = zis.getNextEntry();
          }
        }
        catch (Exception ex) {
          throw new RuntimeException(ex);
        }
        return null;
      }
    });
  }

  public void withZipInputStream(final Closure groovyClosure) {
    execute(new ZipInputStreamCallback() {
      @Override
      public Object doWithZipEntry(InputStream is, ZipEntry entry) throws Exception {
        if (groovyClosure.getMaximumNumberOfParameters() > 1) {
          return groovyClosure.call(new Object[] {is, entry});
        }
        else {
          return groovyClosure.call(entry);
        }
      }
    });
  }
}

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Decorates an existing <code>InputStream</code> to suppress <code>close()</code>.
 * This is useful if the existing <code>InputStream</code> in an embedded one,
 * e.g. in a ZIP file.
 */
public class NonClosingInputStream extends FilterInputStream {

  public NonClosingInputStream(InputStream in) {
    super(in);
  }

  @Override
  public void close() throws IOException {
    // don't close
  }

}

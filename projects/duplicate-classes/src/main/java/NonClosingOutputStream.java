import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Decorates an existing <code>OutputStream</code> to suppress <code>close()</code>.
 * This is useful if the existing <code>OutputStream</code> in an embedded one,
 * e.g. in a ZIP file.
 */
public class NonClosingOutputStream extends FilterOutputStream {

  public NonClosingOutputStream(OutputStream out) {
    super(out);
  }

  @Override
  public void close() throws IOException {
    // don't close
  }

}

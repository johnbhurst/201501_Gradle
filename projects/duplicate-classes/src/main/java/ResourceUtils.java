import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.springframework.core.io.InputStreamSource;
import org.springframework.util.FileCopyUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Some utility methods for <code>Resource</code>s.
 */
public class ResourceUtils {

  public static final String LINE_SEPARATOR = System.getProperty("line.separator");

  private ResourceUtils() {

  }

  /**
   * Returns the contents of the given resource, as an array of bytes.
   * <p>This is the most raw form to read resources.
   * If you want to read the resource as a String or a set of lines,
   * use <code>getData()</code> or <code>getLine()</code>/<code>getLines()</code>.
   * @param inputStreamSource The resource to read from.
   * @return The contents of the resource.
   */
  public static byte[] getBytes(InputStreamSource inputStreamSource) {
    return (byte[]) new InputStreamTemplate(inputStreamSource).execute(new InputStreamCallback() {
      public Object doInInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileCopyUtils.copy(inputStream, baos);
        return baos.toByteArray();
      }
    });
  }

  /**
   * Returns the contents of the resource, as a String, using the default character encoding.
   * @param inputStreamSource The resource to read from.
   * @return The contents of the resource.
   */
  public static String getData(InputStreamSource inputStreamSource) {
    return new String(getBytes(inputStreamSource));
  }

  /**
   * Returns the contents of the resource as a String, using the given character encoding.
   * @param inputStreamSource The resource to read from.
   * @param charset The character encoding.
   * @return The contents of the resource.
   */
  public static String getData(InputStreamSource inputStreamSource, Charset charset) {
    return new String(getBytes(inputStreamSource), charset);
  }

  /**
   * Returns the contents of the resource as a list of Strings, using the default character encoding.
   * <p>The lines are split using the platform-defined line separator specified by the
   * system property <code>line.separator</code>.
   * @param inputStreamSource The resource to read from.
   * @return The contents of the resource.
   */
  public static String[] getLines(InputStreamSource inputStreamSource) {
    return StringUtils.split(getData(inputStreamSource), LINE_SEPARATOR);
  }

  /**
   * Returns the contents of the resource as a list of Strings, using the given character encoding.
   * <p>The lines are split using the platform-defined line separator specified by the
   * system property <code>line.separator</code>.
   * @param inputStreamSource The resource to read from.
   * @param charset The character encoding.
   * @return The contents of the resource.
   */
  public static String[] getLines(InputStreamSource inputStreamSource, Charset charset) {
    return StringUtils.split(getData(inputStreamSource, charset), LINE_SEPARATOR);
  }

  /**
   * Returns the given line read from the resource, using the default character encoding.
   * <p>The lines are split using the platform-defined line separator specified by the
   * system property <code>line.separator</code>.
   * @param inputStreamSource The resource to read from.
   * @param lineNumber The line number to read, starting at 0 for the first line.
   * @return The specified line.
   */
  public static String getLine(InputStreamSource inputStreamSource, int lineNumber) {
    return getLines(inputStreamSource)[lineNumber];
  }

  /**
   * Returns the given line read from the resource, using the given character encoding.
   * <p>The lines are split using the platform-defined line separator specified by the
   * system property <code>line.separator</code>.
   * @param inputStreamSource The resource to read from.
   * @param lineNumber The line number to read, starting at 0 for the first line.
   * @param charset The character encoding.
   * @return The specified line.
   */
  public static String getLine(InputStreamSource inputStreamSource, int lineNumber, Charset charset) {
    return getLines(inputStreamSource, charset)[lineNumber];
  }

  /**
   * Returns the lines read from the resource, starting at the given line and using the
   * default character encoding.
   * <p>The lines are split using the platform-defined line separator specified by the
   * system property <code>line.separator</code>.
   * @param inputStreamSource The resource to read from.
   * @param startLine The starting line number, starting at 0 for the first line.
   * @return The lines read from the resource.
   */
  public static String[] getLines(InputStreamSource inputStreamSource, int startLine) {
    String[] lines = getLines(inputStreamSource);
    return Arrays.copyOfRange(lines, startLine, lines.length);
  }

  /**
   * Returns the lines read from the resource, starting at the given line and using the
   * given character encoding.
   * <p>The lines are split using the platform-defined line separator specified by the
   * system property <code>line.separator</code>.
   * @param inputStreamSource The resource to read from.
   * @param startLine The starting line number, starting at 0 for the first line.
   * @param charset The character encoding.
   * @return The lines read from the resource.
   */
  public static String[] getLines(InputStreamSource inputStreamSource, int startLine, Charset charset) {
    String[] lines = getLines(inputStreamSource, charset);
    return Arrays.copyOfRange(lines, startLine, lines.length);
  }

  /**
   * Returns the lines read from the resource, from the given start line to the given end line
   * and using the default character encoding.
   * <p>The lines are split using the platform-defined line separator specified by the
   * system property <code>line.separator</code>.
   * @param inputStreamSource The resource to read from.
   * @param startLine The starting line number, starting at 0 for the first line.
   * @param endLine The ending line number.  <code>endLine - startLine</code> lines are read.
   * @return The lines read from the resource.
   */
  public static String[] getLines(InputStreamSource inputStreamSource, int startLine, int endLine) {
    String[] lines = getLines(inputStreamSource);
    return Arrays.copyOfRange(lines, startLine, endLine);
  }

  /**
   * Returns the lines read from the resource, from the given start line to the given end line
   * and using the given character encoding.
   * <p>The lines are split using the platform-defined line separator specified by the
   * system property <code>line.separator</code>.
   * @param inputStreamSource The resource to read from.
   * @param startLine The starting line number, starting at 0 for the first line.
   * @param endLine The ending line number. <code>endLine - startLine</code> lines are read.
   * @param charset The character encoding.
   * @return The lines read from the resource.
   */
  public static String[] getLines(InputStreamSource inputStreamSource, int startLine, int endLine, Charset charset) {
    String[] lines = getLines(inputStreamSource, charset);
    return Arrays.copyOfRange(lines, startLine, endLine);
  }

  /**
   * Returns the lines read from the resource as a single string, starting at the given line
   * and using the default character encoding.
   * <p>The lines are split using the platform-defined line separator specified by the
   * system property <code>line.separator</code>.
   * @param inputStreamSource The resource to read from.
   * @param startLine The starting line number, starting at 0 for the first line.
   * @return The lines read from the resource.
   */
  public static String getData(InputStreamSource inputStreamSource, int startLine) {
    String[] lines = getLines(inputStreamSource, startLine);
    return StringUtils.join(lines, LINE_SEPARATOR);
  }

  /**
   * Returns the lines read from the resource as a single string, starting at the given line
   * and using the given character encoding.
   * <p>The lines are split using the platform-defined line separator specified by the
   * system property <code>line.separator</code>.
   * @param inputStreamSource The resource to read from.
   * @param startLine The starting line number, starting at 0 for the first line.
   * @param charset The character encoding.
   * @return The lines read from the resource.
   */
  public static String getData(InputStreamSource inputStreamSource, int startLine, Charset charset) {
    String[] lines = getLines(inputStreamSource, startLine, charset);
    return StringUtils.join(lines, LINE_SEPARATOR);
  }

  /**
   * Returns the lines read from the resource as a single string, from the given start
   * line to the given end line, and using the default character encoding.
   * <p>The lines are split using the platform-defined line separator specified by the
   * system property <code>line.separator</code>.
   * @param inputStreamSource The resource to read from.
   * @param startLine The starting line number, starting at 0 for the first line.
   * @param endLine The ending line number.  <code>endLine - startLine</code> lines are read.
   * @return The lines read from the resource.
   */
  public static String getData(InputStreamSource inputStreamSource, int startLine, int endLine) {
    String[] lines = getLines(inputStreamSource, startLine, endLine);
    return StringUtils.join(lines, LINE_SEPARATOR);
  }

  /**
   * Returns the lines read from the resource as a single string, from the given start
   * line to the given end line, and using the given character encoding.
   * <p>The lines are split using the platform-defined line separator specified by the
   * system property <code>line.separator</code>.
   * @param inputStreamSource The resource to read from.
   * @param startLine The starting line number, starting at 0 for the first line.
   * @param endLine The ending line number.  <code>endLine - startLine</code> lines are read.
   * @param charset The character encoding.
   * @return The line read from the resource.
   */
  public static String getData(InputStreamSource inputStreamSource, int startLine, int endLine, Charset charset) {
    String[] lines = getLines(inputStreamSource, startLine, endLine, charset);
    return StringUtils.join(lines, LINE_SEPARATOR);
  }
}

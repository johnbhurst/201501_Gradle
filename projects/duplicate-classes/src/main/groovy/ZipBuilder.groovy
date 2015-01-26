import java.nio.charset.Charset
import java.util.zip.ZipOutputStream
import java.util.zip.ZipEntry

class ZipBuilder {

  ZipOutputStream zos
  ByteArrayOutputStream baos = new ByteArrayOutputStream()

  ZipBuilder() {
    this.zos = new ZipOutputStream(baos)
  }

  ZipBuilder(OutputStream os) {
    this.zos = new ZipOutputStream(os)
  }

  byte[] getData() {
    return baos.toByteArray()
  }

  ZipBuilder zip(Closure closure) {
    closure.delegate = this
    closure.call()
    zos.close()
    return this
  }

  void entry(Map props, String name, Closure closure) {
    def entry = new ZipEntry(name)
    props.each {k, v -> entry[k] = v}
    zos.putNextEntry(entry)
    NonClosingOutputStream ncos = new NonClosingOutputStream(zos)
    closure.call(ncos)
  }

  void entry(String name, Closure closure) {
    entry([:], name, closure)
  }

  void entry(Map props, String name, byte[] data) {
    entry(props, name) {OutputStream os ->
      os.bytes = data
    }
  }

  void entry(String name, byte[] data) {
    entry([:], name, data)
  }

  void entry(Map props, String name, String data) {
    entry(props, name, data.bytes)
  }

  void entry(String name, String data) {
    entry([:], name, data)
  }

  void entry(Map props, String name, String data, Charset charset) {
    entry(props, name) {OutputStream os ->
      new OutputStreamWriter(os, charset).withWriter {writer ->
        writer.write(data)
      }
    }
  }

  void entry(String name, String data, Charset charset) {
    entry([:], name, data, charset)
  }

  void entry(Map props, String name, String data, String charsetName) {
    entry(props, name) {OutputStream os ->
      os.withWriter(charsetName) {writer ->
        writer.write(data)
      }
    }
  }

  void entry(String name, String data, String charsetName) {
    entry([:], name, data, charsetName)
  }

  // ZipEntry properties:
  //setComment(String)
  //setCompressedSize(long)
  //setCrc(long)
  //setExtra(byte[])
  //setMethod(int)
  //setSize(long)
  //setTime(long)

}

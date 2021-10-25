package com.piramide.elwis.web.common.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Implementation of <b>HttpServletResponseWrapper</b> that works with
 * the CompressionServletResponseStream implementation..
 *
 * @author Amy Roh
 * @author Dmitri Valdin
 * @author Fernando
 * @version $Revision: 9703 $, $Date: 2009-09-12 11:46:08 -0400 (Sat, 12 Sep 2009) $
 */

public class CompressionServletResponseWrapper extends HttpServletResponseWrapper {

    // ----------------------------------------------------- Constructor

    /**
     * Calls the parent constructor which creates a ServletResponse adaptor
     * wrapping the given response object.
     */

    public CompressionServletResponseWrapper(HttpServletResponse response) {
        super(response);
        origResponse = response;
        if (debug > 1) {
            System.out.println("CompressionServletResponseWrapper constructor gets called");
        }
    }


    // ----------------------------------------------------- Instance Variables

    /**
     * Original response
     */

    protected HttpServletResponse origResponse = null;

    /**
     * Descriptive information about this Response implementation.
     */

    protected static final String info = "CompressionServletResponseWrapper";

    /**
     * The ServletOutputStream that has been returned by
     * <code>getOutputStream()</code>, if any.
     */

    protected ServletOutputStream stream = null;


    /**
     * The PrintWriter that has been returned by
     * <code>getWriter()</code>, if any.
     */

    protected PrintWriter writer = null;

    /**
     * The threshold number to compress
     */
    protected int threshold = 0;

    /**
     * Debug level
     */
    private int debug = 0;

    /**
     * Content type
     */
    protected String contentType = null;

    // --------------------------------------------------------- Public Methods


    /**
     * Set content type
     */
    public void setContentType(String contentType) {
        if (debug > 1) {
            System.out.println("setContentType to " + contentType);
        }
        this.contentType = contentType;
        origResponse.setContentType(contentType);
    }


    /**
     * Set threshold number
     */
    public void setCompressionThreshold(int threshold) {
        if (debug > 1) {
            System.out.println("setCompressionThreshold to " + threshold);
        }
        this.threshold = threshold;
    }


    /**
     * Set debug level
     */
    public void setDebugLevel(int debug) {
        this.debug = debug;
    }


    /**
     * Create and return a ServletOutputStream to write the content
     * associated with this Response.
     *
     * @throws IOException if an input/output error occurs
     */
    public ServletOutputStream createOutputStream() throws IOException {
        if (debug > 1) {
            System.out.println("createOutputStream gets called");
            System.out.println("CompressionServletResponseWrapper The Content-Type= " + getContentType());
        }
        /**
         * Only compress the response if the response content type is not an image.
         * because images gif, jpeg, png are compressed...so it is not required to compress, in that way
         * it does not compress the image starting response content types.
         */
        if (getContentType() != null && !getContentType().startsWith("image")) {
            CompressionResponseStream stream = new CompressionResponseStream(origResponse);
            stream.setDebugLevel(debug);
            stream.setBuffer(threshold);
            return stream;
        } else {
            return origResponse.getOutputStream();
        }

    }


    /**
     * Finish a response.
     */
    public void finishResponse() {

        try {
            if (writer != null) {
                writer.close();
            } else {
                if (stream != null) {
                    stream.close();
                }
            }
        } catch (IOException e) {
        }
    }


    // ------------------------------------------------ ServletResponse Methods


    /**
     * Flush the buffer and commit this response.
     *
     * @throws IOException if an input/output error occurs
     */
    public void flushBuffer() throws IOException {
        if (debug > 1) {
            System.out.println("flush buffer @ CompressionServletResponseWrapper");
        }
        ((CompressionResponseStream) stream).flush();

    }

    /**
     * Return the servlet output stream associated with this Response.
     *
     * @throws IllegalStateException if <code>getWriter</code> has
     *                               already been called for this response
     * @throws IOException           if an input/output error occurs
     */
    public ServletOutputStream getOutputStream() throws IOException {

        if (writer != null) {
            throw new IllegalStateException("getWriter() has already been called for this response");
        }

        if (stream == null) {
            stream = createOutputStream();
        }
        if (debug > 1) {
            System.out.println("stream is set to " + stream + " in getOutputStream");
        }

        return (stream);

    }

    /**
     * Return the writer associated with this Response.
     *
     * @throws IllegalStateException if <code>getOutputStream</code> has
     *                               already been called for this response
     * @throws IOException           if an input/output error occurs
     */
    public PrintWriter getWriter() throws IOException {

        if (writer != null) {
            return (writer);
        }

        if (stream != null) {
            throw new IllegalStateException("getOutputStream() has already been called for this response");
        }

        stream = createOutputStream();
        if (debug > 1) {
            System.out.println("stream is set to " + stream + " in getWriter");
        }
        //String charset = getCharsetFromContentType(contentType);
        String charEnc = origResponse.getCharacterEncoding();
        if (debug > 1) {
            System.out.println("character encoding is " + charEnc);
        }
        // HttpServletResponse.getCharacterEncoding() shouldn't return null
        // according the spec, so feel free to remove that "if"
        if (charEnc != null) {
            writer = new PrintWriter(new OutputStreamWriter(stream, charEnc));
        } else {
            writer = new PrintWriter(stream);
        }

        return (writer);

    }

    public void setContentLength(int length) {
    }
}

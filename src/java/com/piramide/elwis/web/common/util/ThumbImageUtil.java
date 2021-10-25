package com.piramide.elwis.web.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class ThumbImageUtil {
    private Log log = LogFactory.getLog(ThumbImageUtil.class);

    private int maxSize = 250;
    private String filePath;
    private InputStream fileInputStream;

    public ThumbImageUtil(String filePath, int maxSize) {
        this.filePath = filePath;
        this.maxSize = maxSize;
    }

    public ThumbImageUtil(InputStream fileInputStream, int maxSize) {
        this.fileInputStream = fileInputStream;
        this.maxSize = maxSize;
    }

    public void writeThumb(ByteArrayOutputStream outputStream) throws IOException {
        BufferedImage image = thumbImage();
        ImageIO.write(image, "jpg", outputStream);
    }

    private BufferedImage thumbImage() throws IOException {
        ImageInputStream imageInputStream = readImageInputStream();
        ImageReader reader = getImageReader(imageInputStream);
        reader.setInput(imageInputStream, true, true);
        int size = Math.max(reader.getWidth(0), reader.getHeight(0));
        int subsampling = size / maxSize + (size % maxSize != 0 ? 1 : 0);
        if (size < maxSize) {
            subsampling = 1;
        }

        ImageReadParam param = reader.getDefaultReadParam();
        param.setSourceSubsampling(subsampling, subsampling, 0, 0);
        BufferedImage image = reader.read(0, param);

        BufferedImage thumbImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = thumbImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(image, null, 0, 0);
        reader.dispose();
        return thumbImage;
    }

    private InputStream readImage(String filePath) {
        File imageFile = new File(filePath);
        InputStream in = null;
        try {
            in = new FileInputStream(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return in;
    }

    private ImageInputStream readImageInputStream() throws IOException {
        InputStream inputStream = this.fileInputStream;
        if (null != filePath) {
            inputStream = readImage(filePath);
            log.debug("-> Read Image from " + filePath + " OK");
        }

        if (null == inputStream) {
            throw new RuntimeException("-> Read InputStream FAIL");
        }

        return ImageIO.createImageInputStream(inputStream);
    }

    private ImageReader getImageReader(ImageInputStream in) {
        Iterator readers = ImageIO.getImageReaders(in);
        if (!readers.hasNext()) {
            log.debug("-> Find Reader FAIL");
        }
        ImageReader reader = (ImageReader) readers.next();
        if (readers.hasNext()) {
            log.debug("-> There are more Readers for ");
        }
        return reader;
    }
}


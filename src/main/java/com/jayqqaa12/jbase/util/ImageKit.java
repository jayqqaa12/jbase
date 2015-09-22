package com.jayqqaa12.jbase.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.mortennobel.imagescaling.ResampleOp;

/***
 * please add
 * 
 * <dependency> <groupId>com.mortennobel</groupId>
 * <artifactId>java-image-scaling</artifactId> <version>0.8.5</version>
 * </dependency>
 * 
 * @author 12
 *
 */

@Deprecated
public class ImageKit {
	private static ImageKit manager = new ImageKit();

	/**
	 * read image from file, now it can support image type:
	 * bmp,wbmp,gif,jpge,png
	 * 
	 * @param file
	 * @return BufferedImage
	 * 
	 * 
	 *         <pre>
	 * BufferedImage image;
	 * image = ImageUtils.readImage(new File(&quot;myImage.jpg&quot;));
	 * image = ImageUtils.readImage(new File(&quot;myImage.gif&quot;));
	 * image = ImageUtils.readImage(new File(&quot;myImage.bmp&quot;));
	 * image = ImageUtils.readImage(new File(&quot;myImage.png&quot;));
	 * </pre>
	 */
	public BufferedImage readImage(File file) {
		BufferedImage image = null;
		if (file != null && file.isFile() && file.exists()) {
			try {
				image = ImageIO.read(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return image;
	}

	/**
	 * get the image width
	 * 
	 * @param image
	 * @return image width
	 */
	public double getWidth(BufferedImage image) {
		return image.getWidth();
	}

	/**
	 * get the image height
	 * 
	 * @param image
	 * @return image height
	 */
	public double getHeight(BufferedImage image) {
		return image.getHeight();
	}

	/**
	 * @param image
	 *            BufferedImage.
	 * @param width
	 *            zoom width.
	 * @param heigth
	 *            zoom heigth.
	 * @return BufferedImage
	 */
	public BufferedImage zoom(BufferedImage image, int width, int heigth) {
		ResampleOp resampleOp = new ResampleOp(width, heigth);
		BufferedImage tag = resampleOp.filter(image, null);
		return tag;
	}

	/**
	 * 
	 * @param image
	 *            BufferedImage
	 * @param width
	 *            zoom width.
	 * @param heigth
	 *            zoom heigth.
	 * @return double array.double[0]:width,double[1]:heigth.
	 */
	public double[] zoomSize(BufferedImage image, int width, int heigth) {
		double[] zoomSize = new double[2];
		double srcWidth = getWidth(image);
		double srcHeigth = getHeight(image);

		if (srcWidth > srcHeigth) {
			zoomSize[0] = (srcWidth / srcHeigth) * heigth;
			zoomSize[1] = heigth;
		}
		if (srcWidth < srcHeigth) {
			zoomSize[0] = width;
			zoomSize[1] = (srcHeigth / srcWidth) * width;
		}
		return zoomSize;
	}

	/**
	 * 
	 * Output to file out according to the style<code>BufferedImage</code> If
	 * can not appoint image or formateName or file out ,do nothing. Now it can
	 * support image type：bmp,wbmp,jpeg,png.
	 * 
	 * @param image
	 *            BufferedImage.
	 * @param formatName
	 *            format name.
	 * @param out
	 *            output path.
	 * @throws IOException
	 *             IOException
	 */
	public void writeImage(BufferedImage image, String formatName, File file) throws IOException {
		if (image != null && formatName != null && !"".equals(formatName) && file != null) {
			ImageIO.write(image, formatName, file);
		}
	}

	/***
	 * 
	 * @param src
	 * @param to
	 * @param w
	 * @param h
	 * @param ext
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage getCompassImage(String src, int w, int h) throws IOException {
		BufferedImage image = manager.readImage(new File(src));

		if (image.getWidth() < 480) {
			w = image.getWidth();
			h = image.getHeight();
		}

		double[] size = manager.zoomSize(image, w, h);

		return manager.zoom(image, (int) size[0], (int) size[1]);
	}

	/***
	 * 
	 * @param src
	 * @param to
	 * @param w
	 * @param h
	 * @param ext
	 * @throws IOException
	 */
	public static void compassImage(File src, File to, int w, int h, String ext) throws IOException {
		BufferedImage image = manager.readImage(src);

		if (image.getWidth() > 480) {
			w = image.getWidth() / 2;
			h = image.getHeight() / 2;
		}

		double[] size = manager.zoomSize(image, w, h);

		manager.writeImage(manager.zoom(image, (int) size[0], (int) size[1]), ext, to);
	}

	/***
	 * 
	 * @param src
	 * @param to
	 * @param w
	 * @param h
	 * @param ext
	 * @throws IOException
	 */
	public static void compassImage(String src, String to, int w, int h, String ext) throws IOException {
		compassImage(new File(src), new File(to), w, h, ext);
	}

	public static String getFormatName(Object o) {
		try {
			ImageInputStream iis = ImageIO.createImageInputStream(o);
			Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
			if (!iter.hasNext()) {
				return null;
			}

			ImageReader reader = iter.next();
			iis.close();
			return reader.getFormatName();
		} catch (IOException e) {}
		return null;
	}

}

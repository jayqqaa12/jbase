package com.jayqqaa12.jbase.sdk.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.mortennobel.imagescaling.ResampleOp;

/***
 * 
 * 必需添加
 * 		<dependency>
			<groupId>com.mortennobel</groupId>
			<artifactId>java-image-scaling</artifactId>
			<version>0.8.5</version>
		</dependency>

 * 
 * @author 12
 *
 */

@Deprecated
public class ImageKit {
	private static ImageKit manager = new ImageKit();

 
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

 
	public double getWidth(BufferedImage image) {
		return image.getWidth();
	}

 
	public double getHeight(BufferedImage image) {
		return image.getHeight();
	}
 
	public BufferedImage zoom(BufferedImage image, int width, int heigth) {
		
		
		ResampleOp resampleOp = new ResampleOp(width, heigth);
		BufferedImage tag = resampleOp.filter(image, null);
		return tag;
	}

 
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

 
	public void writeImage(BufferedImage image, String formatName, File file) throws IOException {
		if (image != null && formatName != null && !"".equals(formatName) && file != null) {
			
			ImageIO.write(image, formatName, file);
			
		}
	}
 
	public static BufferedImage getCompassImage(String src, int w, int h) throws IOException {
		BufferedImage image = manager.readImage(new File(src));
 

		double[] size = manager.zoomSize(image, w, h);

		return manager.zoom(image, (int) size[0], (int) size[1]);
	}

 
	public static void compassImage(File src, File to, int w, int h, String ext) throws IOException {
		BufferedImage image = manager.readImage(src);

		double[] size = manager.zoomSize(image, w, h);
		
		

		manager.writeImage(manager.zoom(image, (int) size[0], (int) size[1]), ext, to);
	}

 
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

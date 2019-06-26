package util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

/**
 * 图像处理工具类
 * 
 * @author ordinary-student
 *
 */
public class ImageUtils
{

	/*
	 * 构造方法
	 */
	public ImageUtils()
	{
	}

	/**
	 * 改变图片的大小
	 * 
	 * @param oldImageFile旧的图片文件
	 * @param width新图片宽度
	 * @param height新图片高度
	 * @return 新图片文件
	 * @throws IOException
	 */
	public static File resizeImage(File oldImageFile, int width, int height) throws IOException
	{
		// 获取旧文件名
		String oldImageName = oldImageFile.getName();
		// 获取名字
		String name = oldImageName.substring(0, oldImageName.lastIndexOf("."));
		// 获取尾缀
		String suffix = oldImageName.substring(oldImageName.lastIndexOf(".") + 1);

		// 读取原图片
		BufferedImage originalImage = ImageIO.read(oldImageFile);
		// 获取原图片的类型
		int type = (originalImage.getType() == 0) ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

		// 封装新图片的大小、类型
		BufferedImage newImage = new BufferedImage(width, height, type);
		// 绘制
		Graphics2D g = newImage.createGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();

		// 将源色复制到目标色
		g.setComposite(AlphaComposite.Src);
		// 插值
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		// 呈现
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		// 抗锯齿
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// 新的图像文件名
		String newImageFileName = name + "_" + width + "x" + height + "." + suffix;
		// 封装新的图像文件
		File newImageFile = new File(oldImageFile.getParentFile(), newImageFileName);
		// 文件不存在就创建
		if (!newImageFile.exists())
		{
			newImageFile.createNewFile();
		}

		// 封装文件输出流
		OutputStream outputStream = new FileOutputStream(newImageFile);
		// 写图像文件数据
		ImageIO.write(newImage, suffix, outputStream);
		// 关闭流
		if (outputStream != null)
		{
			outputStream.close();
		}

		// 返回新图片文件
		return newImageFile;
	}

	/**
	 * 将图片灰度化
	 * 
	 * @param imageFile待灰度化的图像文件
	 * @return 新图像文件
	 * @throws Exception
	 */
	public static File grayImage(File imageFile) throws IOException
	{
		// 获取旧文件名
		String oldImageName = imageFile.getName();
		// 获取名字
		String name = oldImageName.substring(0, oldImageName.lastIndexOf("."));
		// 获取尾缀
		String suffix = oldImageName.substring(oldImageName.lastIndexOf(".") + 1);

		// 读取图片
		BufferedImage oldImage = ImageIO.read(imageFile);
		// 获取图片类型
		int imageType = oldImage.getType();
		// 获取图片宽度
		int imageWidth = oldImage.getWidth();
		// 获取图片高度
		int imageHeight = oldImage.getHeight();

		// 开始扫描点的横坐标
		int startX = 0;
		// 开始扫描点的纵坐标
		int startY = 0;
		// 偏移量
		int offset = 0;
		// 扫描间距
		int scanSize = imageWidth;

		// 被遍历的宽度间距
		int widthInterval = imageWidth - startX;
		// 被遍历的高度间距
		int heightInterval = imageHeight - startY;
		// 横向中间点
		int x0 = imageWidth / 2;
		// 纵向中间点
		int y0 = imageHeight / 2;

		// 旧rgb的数组，保存像素，用一维数组表示二位图像像素数组
		int[] oldRGBArray = new int[offset + heightInterval * scanSize + widthInterval];
		// 新的rgb的数组，保存处理后的像素
		int[] newRGBArray = new int[offset + heightInterval * scanSize + widthInterval];

		// 开始扫描，获取旧图片的RGB值
		oldImage.getRGB(startX, startY, imageWidth, imageHeight, oldRGBArray, offset, scanSize);

		int rgb = oldRGBArray[offset + (y0 - startY) * scanSize + (x0 - startX)];
		Color c = new Color(rgb);

		// 转换RGB值
		for (int i = 0; i < imageHeight - startY; i++)
		{
			for (int j = 0; j < imageWidth - startX; j++)
			{
				c = new Color(oldRGBArray[i * widthInterval + j]);
				// 彩色图像转换成无彩色的灰度图像Y=0.299*R + 0.587*G + 0.114*B
				int gray = (int) (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue());
				// 新灰度图像的像素数组
				newRGBArray[i * widthInterval + j] = new Color(gray, gray, gray).getRGB();
			}
		}

		// 新的图像文件名
		String newImageFileName = name + "_gray." + suffix;
		// 保存新图片
		File newImageFile = new File(imageFile.getParentFile(), newImageFileName);
		// 文件不存在就创建
		if (!newImageFile.exists())
		{
			newImageFile.createNewFile();
		}

		// 封装文件输出流
		OutputStream outputStream = new FileOutputStream(newImageFile);
		// 封装新图片
		BufferedImage newImage = new BufferedImage(imageWidth, imageHeight, imageType);
		// 设置RGB值
		newImage.setRGB(startX, startY, imageWidth, imageHeight, newRGBArray, offset, scanSize);
		// 写图片数据
		ImageIO.write(newImage, suffix, outputStream);

		// 关闭流
		if (outputStream != null)
		{
			outputStream.close();
		}

		// 返回新图像文件
		return newImageFile;
	}

}

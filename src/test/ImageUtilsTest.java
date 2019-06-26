package test;

import java.io.File;
import java.io.IOException;

import util.ImageUtils;

/**
 * 图像处理工具测试类
 * 
 * @author ordinary-student
 *
 */
public class ImageUtilsTest
{
	public static void main(String[] args) throws IOException
	{
		File file = new File("src\\res\\test.jpg");
		// 改变大小
		ImageUtils.resizeImage(file, 1000, 1000);
		// 灰度化
		ImageUtils.grayImage(file);
	}
}

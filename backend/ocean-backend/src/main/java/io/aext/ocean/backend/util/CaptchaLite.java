package io.aext.ocean.backend.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rojar Smith
 *
 * @date 2021-06-30
 */
@Slf4j
public class CaptchaLite {
	static Random random = new Random();

	@Setter
	private int width = 160;

	@Setter
	private int height = 40;

	@Setter
	private int lineSize = 30;

	@Setter
	private int stringNum = 4;

	@Setter
	private String randomString = "3478acdefghkmnptuvwxy";

	/*
	 * Get font
	 */
	private Font getFont() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		try (InputStream stream = loader.getResourceAsStream("fonts/ubuntu-light.ttf")) {

			Font font = Font.createFont(Font.TRUETYPE_FONT, stream);

			font = font.deriveFont(Font.BOLD, 24F);
			return font;
		} catch (Exception e) {
			log.error("'handleResponseStatusException':", e);
			return null;
		}
	}

	/*
	 * Get color
	 */
	private static Color getRandomColor(int fc, int bc) {

		fc = Math.min(fc, 255);
		bc = Math.min(bc, 255);

		int r = fc + random.nextInt(bc - fc - 16);
		int g = fc + random.nextInt(bc - fc - 14);
		int b = fc + random.nextInt(bc - fc - 12);

		return new Color(r, g, b);
	}

	/*
	 * draw noise line
	 */
	private void drawLine(Graphics g) {
		int x = random.nextInt(width);
		int y = random.nextInt(height);
		int xl = random.nextInt(20);
		int yl = random.nextInt(10);
		g.drawLine(x, y, x + xl, y + yl);
	}

	/*
	 * Get random char
	 */
	private String getRandomString(int num) {
		num = num > 0 ? num : randomString.length();
		return String.valueOf(randomString.charAt(random.nextInt(num)));
	}

	/*
	 * Draw string
	 */
	private String drawString(Graphics g, String randomStr, int i) {
		g.setFont(getFont());
		g.setColor(getRandomColor(108, 190));
//		System.out.println(random.nextInt(randomString.length()));
		String rand = getRandomString(random.nextInt(randomString.length()));
		randomStr += rand;
		g.translate(random.nextInt(3), random.nextInt(6));
		g.drawString(rand, 40 * i + 10, 25);
		return randomStr;
	}

	/*
	 * Generate random image
	 */
	public void getRandomCodeImage(HttpServletRequest request, HttpServletResponse response) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
		Font font = getFont();
		GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		localGraphicsEnvironment.registerFont(font);
		Graphics g = localGraphicsEnvironment.createGraphics(image);
		g.fillRect(0, 0, width, height);
		g.setColor(getRandomColor(105, 189));
		g.setFont(font);

		for (int i = 0; i < lineSize; i++) {
			drawLine(g);
		}

		String random_string = "";
		for (int i = 0; i < stringNum; i++) {
			random_string = drawString(g, random_string, i);
		}

		System.out.println(random_string);

		g.dispose();

		try {
			ImageIO.write(image, "PNG", response.getOutputStream());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Generate random image
	 */
	public List<Object> getRandomCodeImage() {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
		Font font = getFont();
		GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		localGraphicsEnvironment.registerFont(font);
		Graphics g = localGraphicsEnvironment.createGraphics(image);
		g.fillRect(0, 0, width, height);
		g.setColor(getRandomColor(105, 189));
		g.setFont(font);

		for (int i = 0; i < lineSize; i++) {
			drawLine(g);
		}

		String random_string = "";
		for (int i = 0; i < stringNum; i++) {
			random_string = drawString(g, random_string, i);
		}

		System.out.println(random_string);

		g.dispose();

		List<Object> res = new ArrayList<>();
		res.add(random_string);
		res.add(image);

		return res;
	}

	public List<Object> getRandomCodeBase64() {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
		Font font = getFont();
		GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		localGraphicsEnvironment.registerFont(font);
		Graphics g = localGraphicsEnvironment.createGraphics(image);
		g.fillRect(0, 0, width, height);
		g.setColor(getRandomColor(105, 189));
		g.setFont(getFont());

		for (int i = 0; i < lineSize; i++) {
			drawLine(g);
		}

		String random_string = "";
		for (int i = 0; i < stringNum; i++) {
			random_string = drawString(g, random_string, i);
		}

		System.out.println(random_string);

		g.dispose();

		String base64String = "";
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(image, "PNG", bos);

			byte[] bytes = bos.toByteArray();
			Base64.Encoder encoder = Base64.getEncoder();
			base64String = encoder.encodeToString(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<Object> res = new ArrayList<>();
		res.add(random_string);
		res.add(base64String);

		return res;
	}
}

package io.github.volyx.initials;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InitialsAvatarsServlet extends HttpServlet {

    private static final int MAX_IMG_SIZE = 1000;
    private final static int CACHE_EXP_IN_SECS = 10 * 60;

    private final static float FONT_FACTOR = 6f / 11f;

    private static final Pattern PARAMS = Pattern.compile("(\\d+)" + "(x)" + "(\\d+)" + "(/)"
            + "([\\w@\\.\\-]+)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.UNICODE_CHARACTER_CLASS);


    // We'll cache the color of some user for some minutes (wo in a document, it
    // should show the same color)
    // https://code.google.com/p/guava-libraries/wiki/CachesExplained
    private static LoadingCache<String, Color> colorsCache = CacheBuilder.newBuilder().maximumSize(500).expireAfterAccess(
            CACHE_EXP_IN_SECS, TimeUnit.SECONDS).build(new CacheLoader<String, Color>() {
        @Override
        public Color load(final String key) {
            return new Color(ColorHelper.getRandomClearColorInt(key));
        }
    });

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse resp)
            throws ServletException, IOException {

        final String path = request.getPathInfo().substring(1);

        final Matcher m = PARAMS.matcher(path);
        if (!m.find()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        final int width = Math.min(Integer.parseInt(m.group(1)), MAX_IMG_SIZE);
        final int height = Math.min(Integer.parseInt(m.group(3)), MAX_IMG_SIZE);
        final String name = m.group(5);

        if (name.length() < 2) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        final String initial = name.substring(0, 2).toUpperCase();

        final BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g2 = img.createGraphics();

        // Rectangle of random color
        final Rectangle2D.Double rectangle = new Rectangle2D.Double(0, 0, width, height);
        g2.setPaint(colorsCache.getUnchecked(name));
        g2.fill(rectangle);

        // Antialiassing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Font scale
        final int fontSize = Math.round((Math.min(width, height) * FONT_FACTOR));
        final Font font = new Font(Font.SANS_SERIF, Font.PLAIN, fontSize);
        g2.setFont(font);
        final FontMetrics fm = g2.getFontMetrics();
        final float fontSizeLast = width / (float) fm.stringWidth(initial) * fontSize;
        font.deriveFont(fontSizeLast);

        // Font color
        g2.setColor(Color.WHITE);

        // Center font
        final Rectangle2D fontRect = fm.getStringBounds(initial, g2);
        final int x = (width - (int) fontRect.getWidth()) / 2;
        final int y = (height - (int) fontRect.getHeight()) / 2 + fm.getAscent();

        // Draw font
        g2.drawString(initial, x, y);

        ImageIO.write(img, "PNG", resp.getOutputStream());

    }

}

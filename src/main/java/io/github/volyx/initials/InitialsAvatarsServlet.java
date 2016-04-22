package io.github.volyx.initials;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InitialsAvatarsServlet extends HttpServlet {

  private static final int MAX_IMG_SIZE = 1000;

  private static final Pattern PARAMS = Pattern.compile("(\\d+)" + "(x)" + "(\\d+)" + "(\\/)"
      + "([\\w@\\.\\-]+)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.UNICODE_CHARACTER_CLASS);

  private static final long serialVersionUID = 3757314092763168153L;

  @Override
  protected void doGet(final HttpServletRequest request, final HttpServletResponse resp)
      throws ServletException, IOException {

    final String path = request.getPathInfo().substring(1);

    final Matcher m = PARAMS.matcher(path);
    if (m.find()) {
      final int width = Math.min(Integer.parseInt(m.group(1)), MAX_IMG_SIZE);
      final int height = Math.min(Integer.parseInt(m.group(3)), MAX_IMG_SIZE);
      final String name = m.group(5);
      InitialsAvatarsServerUtils.doInitialsResponse(resp, width, height, name);
    } else {
      // Wrong params
      resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }

}

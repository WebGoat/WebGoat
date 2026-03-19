protected AttackResult furBall(String url) {
    try {
        URL parsedUrl = new URL(url);

        // 1. Проверяем хост (белый список)
        if (!"ifconfig.pro".equalsIgnoreCase(parsedUrl.getHost())) {
            return getFailedResult("Access to this URL is not allowed.");
        }

        // 2. Запрещаем локальные IP (включая localhost)
        if (isLocalAddress(parsedUrl)) {
            return getFailedResult("Access to internal resources is forbidden.");
        }

        // 3. Проверяем протокол (только HTTP или HTTPS)
        String protocol = parsedUrl.getProtocol().toLowerCase();
        if (!"http".equals(protocol) && !"https".equals(protocol)) {
            return getFailedResult("Only HTTP/HTTPS protocols are allowed.");
        }

        // 4. Проверяем порт (только стандартные)
        int port = parsedUrl.getPort();
        if (port == -1) {
            port = "https".equals(protocol) ? 443 : 80;
        } else if (("http".equals(protocol) && port != 80) ||
                   ("https".equals(protocol) && port != 443)) {
            return getFailedResult("Non-standard ports are not allowed.");
        }

        // 5. Получаем путь и query (если они есть) и проверяем на базовые проблемы
        String path = parsedUrl.getPath();
        String query = parsedUrl.getQuery();
        if (path == null) path = "";
        if (query != null) {
            // Простейшая проверка на потенциально опасные символы
            if (query.contains("..") || query.contains("\\")) {
                return getFailedResult("Invalid characters in query string.");
            }
        }

        // 6. Собираем безопасный URL
        URL safeUrl = new URL(protocol, "ifconfig.pro", port, path + (query != null ? "?" + query : ""));

        // 7. Открываем соединение с безопасными настройками
        HttpURLConnection connection = (HttpURLConnection) safeUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setInstanceFollowRedirects(false);

        try (InputStream in = connection.getInputStream()) {
            String html = new String(in.readAllBytes(), StandardCharsets.UTF_8).replaceAll("\n", "<br>");
            return success(this).feedback("ssrf.success").output(html).build();
        }

    } catch (MalformedURLException e) {
        return getFailedResult("Invalid URL format: " + e.getMessage());
    } catch (IOException e) {
        return getFailedResult("Failed to retrieve content: " + e.getMessage());
    }
}

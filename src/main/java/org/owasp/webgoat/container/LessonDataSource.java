package org.owasp.webgoat.container;

import java.io.PrintWriter;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.owasp.webgoat.container.lessons.LessonConnectionInvocationHandler;
import org.springframework.jdbc.datasource.ConnectionProxy;

public class LessonDataSource implements DataSource {

  private final DataSource originalDataSource;

  public LessonDataSource(DataSource dataSource) {
    this.originalDataSource = dataSource;
  }

  @Override
  public Connection getConnection() throws SQLException {
    var targetConnection = originalDataSource.getConnection();
    return (Connection)
        Proxy.newProxyInstance(
            ConnectionProxy.class.getClassLoader(),
            new Class[] {ConnectionProxy.class},
            new LessonConnectionInvocationHandler(targetConnection));
  }

  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    return originalDataSource.getConnection(username, password);
  }

  @Override
  public PrintWriter getLogWriter() throws SQLException {
    return originalDataSource.getLogWriter();
  }

  @Override
  public void setLogWriter(PrintWriter out) throws SQLException {
    originalDataSource.setLogWriter(out);
  }

  @Override
  public void setLoginTimeout(int seconds) throws SQLException {
    originalDataSource.setLoginTimeout(seconds);
  }

  @Override
  public int getLoginTimeout() throws SQLException {
    return originalDataSource.getLoginTimeout();
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return originalDataSource.getParentLogger();
  }

  @Override
  public <T> T unwrap(Class<T> clazz) throws SQLException {
    return originalDataSource.unwrap(clazz);
  }

  @Override
  public boolean isWrapperFor(Class<?> clazz) throws SQLException {
    return originalDataSource.isWrapperFor(clazz);
  }
}

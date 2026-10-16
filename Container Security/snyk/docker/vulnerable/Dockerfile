FROM debian:bookworm
ARG version=2.4.49-4
RUN apt update
ADD dependencies/apache2_2.4.49-4_amd64.deb dependencies/apache2-bin_2.4.49-4_amd64.deb dependencies/apache2-data_2.4.49-4_all.deb dependencies/apache2-utils_2.4.49-4_amd64.deb /
RUN apt install -y perl libxml2 libaprutil1-dbd-sqlite3 libnghttp2-14 libaprutil1-ldap libbrotli1 libcurl4 libjansson4 liblua5.3-0 procps mime-support
RUN dpkg -i /apache2-utils_2.4.49-4_amd64.deb /apache2-data_2.4.49-4_all.deb /apache2-bin_2.4.49-4_amd64.deb /apache2_2.4.49-4_amd64.deb
ADD entry.sh /entry.sh
ADD 000-default.conf /etc/apache2/sites-enabled/000-default.conf
RUN chmod 755 /entry.sh && chown -R www-data:www-data /var/www/html && mkdir /var/run/apache2 && a2enmod cgi

ENTRYPOINT ["/entry.sh"]

FROM java:8

RUN groupadd -r dynamodb && useradd -r -g dynamodb dynamodb

RUN gpg --keyserver ha.pool.sks-keyservers.net --recv-keys B42F6819007F00F88E364FD4036A9C25BF357DD4
RUN curl -s -L "https://github.com/tianon/gosu/releases/download/1.6/gosu-$(dpkg --print-architecture)" -o /usr/local/bin/gosu \
    && curl -s -L "https://github.com/tianon/gosu/releases/download/1.6/gosu-$(dpkg --print-architecture).asc" -o /usr/local/bin/gosu.asc \
    && gpg --verify /usr/local/bin/gosu.asc \
    && rm /usr/local/bin/gosu.asc \
    && chmod +x /usr/local/bin/gosu

RUN mkdir /opt/dynamodb \
   	&& curl -s -L "http://dynamodb-local.s3-website-us-west-2.amazonaws.com/dynamodb_local_latest.tar.gz" | tar -x -z -C "/opt/dynamodb"

WORKDIR /opt/dynamodb
VOLUME /var/lib/dynamodb

COPY docker-entrypoint.sh /
ENTRYPOINT ["/docker-entrypoint.sh"]

EXPOSE 8000

CMD ["dynamodb", "-dbPath", "/var/lib/dynamodb", "-port", "8000"]
    

FROM alpine:latest

RUN apk update
RUN apk fetch openjdk8
RUN apk add openjdk8
ENV JAVA_HOME /usr
ENV SO_HOME /opt/knowesis/sift/orchestrator


RUN mkdir -p  $SO_HOME/bin  $SO_HOME/conf  $SO_HOME/flow  $SO_HOME/log
RUN set -x && \
    apk --update add --virtual build-dependencies   && \
    addgroup -S siftuser && adduser -S -H -G siftuser -h $SO_HOME siftuser && \
    RUN echo "siftuser:sift@123" | chpasswd   && \
    chown -R siftuser:siftuser /opt/knowesis  && \
    apk del build-dependencies && \ 
    rm -rf /var/cache/apk/*

RUN apk add --no-cache bash tzdata curl

COPY target/so-emailhandler-exec/bin  $SO_HOME/bin/

COPY target/so-emailhandler-exec/lib  $SO_HOME/lib/

COPY target/so-emailhandler-exec/conf $SO_HOME/conf

COPY target/so-emailhandler-exec/flow $SO_HOME/flow

WORKDIR $SO_HOME

CMD ["bin/so-emailhandler.sh","start"] && ["bin/so-env.sh"]

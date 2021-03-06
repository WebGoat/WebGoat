BRANCH := $(shell git rev-parse --abbrev-ref HEAD)
COMMIT := $(shell git rev-parse HEAD)
USER=krol
PROJECT=webgoat
RELEASE=$(shell git tag | tail -1)
VERSION=$(shell echo ${RELEASE} | cut -c 2-)

download-jar:
	curl -OL https://github.com/WebGoat/WebGoat/releases/download/${RELEASE}/webgoat-server-${VERSION}.jar
	curl -OL https://github.com/WebGoat/WebGoat/releases/download/${RELEASE}/webwolf-${VERSION}.jar
	mv web*.jar docker

build-docker:
	docker build --no-cache --build-arg webgoat_version=${VERSION} -t ${USER}/${PROJECT}:${BRANCH} -f docker/Dockerfile docker

run-docker:
	docker run -d -p 80:8888 -p 8080:8080 -it -p 9090:9090 -e TZ=Europe/Amsterdam ${USER}/${PROJECT}:${BRANCH}
# Docker all-in-one image

## Docker build

	docker build --no-cache --build-arg webgoat_version=v8.0.0-SNAPSHOT -t webgoat/goatandwolf:latest .
	
## Docker run
	
	docker run -d -p 80:8888 -p 8080:8080 -p 9090:9090 -e TZ=Europe/Amsterdam webgoat/goatandwolf:latest
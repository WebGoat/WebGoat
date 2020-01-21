# Docker all-in-one image

## Docker build

	docker build --no-cache --build-arg webgoat_version_env=v8.0.0-SNAPSHOT -t goatandwolf:latest .
	
## Docker run
	
	docker run -d -p 80:80 goatandwolf:latest
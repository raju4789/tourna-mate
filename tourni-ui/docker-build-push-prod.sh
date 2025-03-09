export BUILD_MODE=production

docker build --build-arg BUILD_MODE=${BUILD_MODE} -t raju5800/tourni-ui:latest .

docker push raju5800/tourni-ui:latest
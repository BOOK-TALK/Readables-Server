#!/bin/bash

# 스크립트 실행 시 발생할 수 있는 오류에 대해 즉시 스크립트를 중단
set -e

# 전체 컨테이너 재시작
############################################
echo "<< 전체 컨테이너 삭제 >>"
sudo docker compose down

echo "<< 전체 이미지 삭제 >>"
images=$(sudo docker images -q)

if [ -n "$images" ]; then # 이미지가 있을때 삭제하도록
  sudo docker rmi -f $images
fi

echo "<< spring app 빌드 >>"
sudo chmod +x gradlew
sudo ./gradlew clean build

echo "<< 전체 재배포 >>"
sudo docker compose up -d

echo "<< 배포 성공 !! >>"

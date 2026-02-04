#!/bin/bash

# 设置脚本在遇到错误时退出
set -e

# 定义变量
IMAGE_NAME="asp-base-service"
TAG="latest"
DOCKERFILE_PATH="src/main/docker/dockerfile"
ENV_FILE_PATH="src/main/docker/env.list"

# 检查 Dockerfile 和环境变量文件是否存在
if [ ! -f "$DOCKERFILE_PATH" ]; then
    echo "未找到 Dockerfile 文件：$DOCKERFILE_PATH。"
    exit 1
fi
if [ ! -f "$ENV_FILE_PATH" ]; then
    echo "未找到环境变量文件：$ENV_FILE_PATH。"
    exit 1
fi

# 检查容器是否正在运行，如果是则停止并删除
if [ "$(docker ps -aq -f name=^${IMAGE_NAME}$)" ]; then
    echo "检测到容器 $IMAGE_NAME 已存在，正在停止并删除..."
    docker stop $IMAGE_NAME
    docker rm $IMAGE_NAME
fi

# 构建 Docker 镜像
echo "正在构建 Docker 镜像..."
docker build -f $DOCKERFILE_PATH -t $IMAGE_NAME:$TAG .

# 清除构建过程中产生的悬空镜像
echo "正在清理悬空镜像..."
if docker images -f "dangling=true" -q | xargs -r docker rmi; then
    echo "悬空镜像已清理完毕。"
else
    echo "无悬空镜像需要清理。"
fi

# 清除所有已经停止的容器
echo "正在清理已停止的容器..."
docker container prune -f

# 列出当前所有镜像
echo "当前所有镜像列表如下："
docker images

# 运行 Docker 容器，并设置环境变量和端口映射
echo "正在使用环境变量文件 $ENV_FILE_PATH 启动 Docker 容器..."
docker run -d --name $IMAGE_NAME --network="host" --env-file $ENV_FILE_PATH --restart unless-stopped $IMAGE_NAME:$TAG

# 显示运行中的容器
echo "当前运行中的容器列表如下："
docker ps

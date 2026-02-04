pipeline {
    agent any

    parameters {
        string(name: 'APP_NAME', defaultValue: 'qjyy-gateway', description: '请输入软件名称')
        string(name: 'VERSION', defaultValue: '1.0.5', description: '请输入软件版本')
        choice(name: 'ENV', choices: ['prod','prod2','test' ], description: '选择部署环境') //测试环境一般不部署网关
    }

    environment {
        TEAM = "qjyy"
        GIT_URL = 'http://192.168.100.136/jt/qjyy-framework.git'
        GIT_BRANCH = 'master'
        GIT_CREDENTIALS_ID = '1efdc209-96f1-401d-8c29-a41ea341edf8'

        PROJECT_PATH = 'asp-base-service'
        JAR_NAME = 'asp-base-service'

        DOCKER_REGISTRY = '192.9.253.166:5001'
        DOCKER_CREDENTIALS_ID = 'c598d34c-1b07-4c30-b3d6-9c6eb870bbe6'

        //远程服务器用户名和地址
        //测试
        REMOTE_CREDENTIALS_ID_TEST = 'gitlab_root_account'
        REMOTE_SERVER_TEST = 'root@192.168.100.135'
        REMOTE_PATH_TEST = '/home/qjyy-framework/qjyy-gateway' //部署路径

        //正式
        REMOTE_CREDENTIALS_ID_PROD1 = 'gitlab_root_account'
        REMOTE_SERVER_PROD1 = 'root@192.168.100.153'
        REMOTE_PATH_PROD1 = '/home/qjyy-framework/qjyy-gateway' //部署路径

        TIMESTAMP = "${new Date().format('yyyyMMddHHmm')}" // 获取当前时间戳
    }
    tools {
        maven 'maven3.8.6'
        jdk 'JDK8'
    }
    stages {
        stage('检查参数') {
            steps {
                script {
                    echo "当前目录:"
                    sh "pwd"
                    // 打印所有参数
                    echo "所有参数:"
                    params.each { key, value ->
                        echo "${key}: ${value}"
                    }
                }
            }
        }

        stage('设置远程服务器') {
            steps {
                script {
                    if (params.ENV == 'test') {
                        env.REMOTE_CREDENTIALS_ID = "${REMOTE_CREDENTIALS_ID_TEST}"
                        env.REMOTE_SERVER = "${REMOTE_SERVER_TEST}"
                        env.REMOTE_PATH = "${REMOTE_PATH_TEST}"
                    } else if (params.ENV == 'prod') {
                        env.REMOTE_CREDENTIALS_ID = "${REMOTE_CREDENTIALS_ID_PROD1}"
                        env.REMOTE_SERVER = "${REMOTE_SERVER_PROD1}"
                        env.REMOTE_PATH = "${REMOTE_PATH_PROD1}"
                    }
                    echo "Environment set to ${params.ENV}"
                    echo "Using server: ${env.REMOTE_SERVER}"
                }
            }
        }

        stage('拉取Git代码') {
            steps {
                git branch: "${GIT_BRANCH}", credentialsId: "${GIT_CREDENTIALS_ID}", poll: false, url: "${GIT_URL}"
                sh 'java -version'
            }
        }
        stage('Maven打包') {
            steps {
                sh '''
                    mvn clean package -U -Dmaven.test.skip=true -f "${PROJECT_PATH}/pom.xml"
                '''
            }
        }
        stage('docker镜像构建') {
            steps {
                script {
                    // 构建 Docker 镜像
                    // 定义不需要环境后缀的环境列表
                    def noSuffixEnvs = ['prod', 'prod2']
                    def envSuffix = noSuffixEnvs.contains(params.ENV) ? '' : "-${params.ENV}"

                    def newVersion = "${params.VERSION}${envSuffix}-${TIMESTAMP}".toString()
                    def latestVersion = "latest${envSuffix}".toString()
                    def standardVersion = "${params.VERSION}${envSuffix}".toString()
                    // 打上时间戳版本标签
                    sh "docker build -t ${DOCKER_REGISTRY}/${TEAM}/${params.APP_NAME}:${newVersion} --build-arg JAR_NAME=${JAR_NAME} --build-arg ENV=${params.ENV} ${PROJECT_PATH}"
                    // 打上 latest 标签
                    sh "docker tag ${DOCKER_REGISTRY}/${TEAM}/${params.APP_NAME}:${newVersion} ${DOCKER_REGISTRY}/${TEAM}/${params.APP_NAME}:${latestVersion}"
                    // 打上指定版本标签
                    sh "docker tag ${DOCKER_REGISTRY}/${TEAM}/${params.APP_NAME}:${newVersion} ${DOCKER_REGISTRY}/${TEAM}/${params.APP_NAME}:${standardVersion}"

                    withCredentials([usernamePassword(credentialsId: "${DOCKER_CREDENTIALS_ID}", passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
                        //push到远程仓库
                        echo "$PASSWORD | docker login -u $USERNAME --password-stdin ${DOCKER_REGISTRY}"
                        sh "docker push ${DOCKER_REGISTRY}/${TEAM}/${params.APP_NAME}:${newVersion}"
                        sh "docker push ${DOCKER_REGISTRY}/${TEAM}/${params.APP_NAME}:${latestVersion}"
                        sh "docker push ${DOCKER_REGISTRY}/${TEAM}/${params.APP_NAME}:${standardVersion}"
                    }

                    // 删除本地镜像
                    sh "docker rmi ${DOCKER_REGISTRY}/${TEAM}/${params.APP_NAME}:${newVersion}"
                    sh "docker rmi ${DOCKER_REGISTRY}/${TEAM}/${params.APP_NAME}:${latestVersion}"
                    sh "docker rmi ${DOCKER_REGISTRY}/${TEAM}/${params.APP_NAME}:${standardVersion}"
                }
            }
        }
        stage('部署至目标服务器') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${REMOTE_CREDENTIALS_ID}", passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
                    //执行docker compose启动容器
                    sh """
                        sshpass -p '${PASSWORD}' ssh -o StrictHostKeyChecking=no ${REMOTE_SERVER} mkdir -p ${REMOTE_PATH}
                        if [ "${params.ENV}" = "test" ]; then
                            sshpass -p '${PASSWORD}' scp -o StrictHostKeyChecking=no ${PROJECT_PATH}/deploy/docker-compose-${params.ENV}.yml ${REMOTE_SERVER}:${REMOTE_PATH}
                            sshpass -p '${PASSWORD}' ssh -o StrictHostKeyChecking=no ${REMOTE_SERVER} "cd ${REMOTE_PATH} && docker compose -f docker-compose-${params.ENV}.yml pull && docker compose -f docker-compose-${params.ENV}.yml up -d"
                        else
                            sshpass -p '${PASSWORD}' scp -o StrictHostKeyChecking=no ${PROJECT_PATH}/deploy/docker-compose.yml ${REMOTE_SERVER}:${REMOTE_PATH}
                            sshpass -p '${PASSWORD}' ssh -o StrictHostKeyChecking=no ${REMOTE_SERVER} "cd ${REMOTE_PATH} && docker compose pull && docker compose up -d"
                        fi
                    """
                }
            }
        }
    }
}
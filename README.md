# ReadMe

## Background Knowledge

- Microservice
- [kubernetes](https://kubernetes.io)
- [Spring-Cloud](https://spring.io/projects/spring-cloud)
- [fabric8](http://fabric8.io)

## Preparation

- My Local Environment with versions
  - Java: 11
  - Maven: 3.8.6
  - [Docker](https://www.docker.com): 20.10.10
  - [minikube](https://minikube.sigs.k8s.io/docs): v1.25.2
  - [spring-cloud-kubernetes-discovery-server](https://docs.spring.io/spring-cloud-kubernetes/docs/current/reference/html/#spring-cloud-kubernetes-discoveryserver): 2.1.0-M3

## Overall Description

### Spring-Cloud-Kubernetes

#### Simple Description

使用 Kubernetes 原生的 服务发现、配置管理、负载均衡能力.
  > Api Gateway 仍旧使用 Spring-Cloud 提供的各类 Api Gateway(TODO, 为什么选 Gateway 而不是 Ingress)

#### Service Discovery

- Register: 应用注册到 kubernetes 中, kubernetes 维护了集群内所有应用对应的 Pod、Service等信息。

- Discovery:
  - kubernetes 网络层提供服务发现
  - spring-cloud-kubernetes-discovery-server 提供发现功能, 可以通过 Discovery 获取

- Loadbalancer
  - 通过 kubernetes 网络层负载的实现, 完成服务间 RPC 的负载均衡
  - 通过 spring-cloud-kubernetes-discovery-server 提供的发现功能, 做客户端负载均衡

##### spring-kubernetes-discovery-server

如果需要像其他 Spring-Cloud 项目一样使用 Spring-kubernetes, 可以通过启动一个 `spring-cloud-kubernetes-discovery-server`, 实现 `Spring-Cloud` 的服务注册相关能力。

- TODO
  - spring-cloud-kubernetes-discovery-server

#### Config Center

### Fabric8

通过 Fabric8社区 提供的 `fabric8-maven-plugin` 完成对应用的打包、部署。 [Fabric8](http://fabric8.io) 是一个开源的 `kubernetes` 开发平台, 提供了 `kubernetes-java-client`, `OpenShift-Elasticsearch-plugin`, `fabric8-maven-plugin`, `docker-maven-plugin` 等工具, 对应的工具可以查看 [Fabric GitHub 仓库](https://github.com/fabric8io)。

- [fabric-maven-plugin](https://github.com/fabric8io/fabric8-maven-plugin): 为 Docker、Kubernetes、OpenShift 构建和部署 Java 应用的一站式插件, 着重于以下任务:
  1. 构建 Docker 镜像;
  2. 创建 Kubernetes, OpenShift 资源(如: yaml 编排文件);
  3. 将应用发布到 Kubernetes/OpenShift.

- 可执行操作:

Goal | Description
:-:|:-:
build | 创建 Docker 镜像
resource | 创建 Kubernetes 和 OpenShift 资源文件
deploy | 将应用发布到 Kubernetes 或 OpenShift
push | 将 docker 镜像 push 到镜像仓库
watch |

详情查看[文档](http://maven.fabric8.io).

- fabric8 配置路径: `src/main/fabric8`. fabric8-maven-plugin 可以直接读取该路径下的 `kubernetes` 编排文件, 并在编排文件中追加一些配置, 具体差异可对比该路径下的 yaml 文件和 `target/class/META-INF/fabric8`。

## Notice

- `spring-cloud-kubernetes-discovery-server` 启动后, 将服务 endpoint 映射到宿主机后, 访问 403, 需要配置 [kubernetes API 访问控制](https://kubernetes.io/zh/docs/concepts/security/controlling-access/), 本地是直接放开了所有读, 生产环境需要根据实际情况来配置。

- 执行 `mvn clean fabric8:deploy -Pkubernetes` 部署, pod 状态为 `ErrImagePull`, 原因为本地部署的 minikube 环境未获取到创建的应用镜像, 通过 `minikube image load <image-name>` 手动将镜像加载到 minikube 环境。 详情参考 [minikube 文档](https://minikube.sigs.k8s.io/docs/handbook/pushing)
  > minikube 推荐通过安装 `Xhyve` 解决该问题, 本地环境未成功, 可能跟系统权限有关, 没细看.

- 配置的 `nodePort` 在宿主机无法访问, 需要通过 `minikube service <service-name> --url` 将端口暴露给宿主机, 后可以通过 `curl http://127.0.0.1:<service-port>/<app-uri>` 调用接口。

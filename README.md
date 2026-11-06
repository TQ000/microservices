# Microservices Ecommerce

代码与配置使用两个平级 Git 仓库：

```text
devtalles/
├── microservices-ecommerce/   # 公共代码仓库
└── ecommerce-config/         # 独立配置仓库，GitHub 必须设置为 Private
```

`ecommerce-config` 的 Git 历史从原 `config-data` 提取，配置变更在该目录单独提交。
公共仓库不再保存实际环境配置，`.env.example` 仅提供环境变量示例。
真实密码和 Token 使用运行环境注入；复制成 `.env` 后也需要手动加载，Spring Boot 不会自动读取它。

## 本地启动

需要 Java 21。先从项目根目录启动数据库：

```bash
docker compose up -d
```

然后在 `config-server` 目录执行：

```bash
./mvnw spring-boot:run
```

默认读取平级目录 `ecommerce-config`，配置文件必须已提交到其 `main` 分支。
VS Code 使用 `ConfigServerApplication` 配置调试，工作目录已设置为该模块。

Config Server 启动后，在库存、订单和产品各自的模块目录运行 `./mvnw spring-boot:run`。
三个服务依赖配置服务器，读取失败时启动会停止，不会忽略缺失配置。

| 服务 | 端口 |
| --- | --- |
| Config Server | 8888 |
| 订单 | 8081 |
| 库存 | 8082 |
| 产品 | 8083 |

## GitHub 私有配置仓库

先创建 Private 仓库并上传 `ecommerce-config`，再向 Config Server 进程提供：

```text
CONFIG_GIT_URI=https://github.com/<owner>/ecommerce-config.git
CONFIG_GIT_USERNAME=<GitHub username>
CONFIG_GIT_TOKEN=<read-only token for the private repository>
```

GitHub Token 不写入 YAML，也不要提交到 Git。
服务名称和 `spring.config.import` 保留在各微服务中，数据库配置位于私有仓库。

Config Server 默认只监听本机。部署到其他机器前，需要为其配置接口启用认证和 HTTPS。

删除文件不会移除旧提交：原配置曾进入公共仓库的 Git 历史。
如果历史中出现过真实密码，应先更换密码，再单独规划历史清理；本次不重写或强制推送公共历史。

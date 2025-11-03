# 本仓库是一个简易在线购物系统

## 一、目录结构

### 1. `backend/` —  后端 Spring Boot 
- `src/main/java/com/shop/...` — Java 源码（控制器、服务、模型、拦截器等）
- `src/main/resources/application.yml` — 配置文件

### 2. `frontend/` —  前端 Vue 3 + Vite 
- `src/` — Vue 源码（`App.vue`、`router.js`、`pages/`）

---

## 二、运行环境

- Java 17（或兼容的 JDK 17）
- Maven 3.x
- Node.js 18+ 与 npm
- MySQL：项目使用 Spring Data JPA，默认为内存或 application.yml 指定的数据源。

在 Windows PowerShell 下安装/检查示例：

```powershell
# 检查 Java
java -version

# 检查 Maven
mvn -v

# 检查 Node/npm
node -v
npm -v
```

---

## 三、运行步骤

### 1. 后端配置: `backend/src/main/resources/application.yml`
- `spring.datasource`：数据库连接配置
- `jwt.secret`：JWT 签名密钥

### 2. 启动后端服务

```shell
cd ..\backend
mvn clean package
mvn spring-boot:run
```

### 3. 启动前端服务

```shell
cd ..\frontend
npm install
npm run dev -- --port 3000
```

---

## PS: 默认管理员账号

- 用户名：`root`
- 密码（明文）：`041018`


## 本地内网运行指南（无 Docker）

### 环境准备
- JDK 17（建议安装 Temurin 17）
- Node.js 18+（建议 20）
- MySQL 8（本地开启并创建数据库 `shopdb`，账号密码 root/root 或自行修改）

### 1. 配置后端数据库连接
后端默认指向本机 MySQL：
`backend/src/main/resources/application.yml`
- URL 默认：`jdbc:mysql://localhost:3306/shopdb?...`
- 用户/密码默认：`root` / `root`
如与本地不一致，修改为你的实际连接。

可选：也支持通过环境变量覆盖：
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

### 2. 启动后端
```bash
cd backend
mvn spring-boot:run
```
启动成功后访问：`http://localhost:8080/api/products`

### 3. 启动前端（开发模式）
```bash
cd frontend
npm install
npm run dev
```
打开：`http://localhost:5173`
说明：开发代理已配置，将 `/api` 代理到 `http://localhost:8080`。

### 4. 启动前端（预览/演示）
```bash
cd frontend
npm install
npm run build
npm run preview
```
默认端口：`http://localhost:4173`

### 5. 局域网访问
确保前后端监听在本机地址（默认即可）。同一局域网内的其他设备可通过你电脑的局域网 IP 访问：
- 前端（开发）：`http://你的局域网IP:5173`
- 前端（预览）：`http://你的局域网IP:4173`
- 后端：`http://你的局域网IP:8080`

Windows 可通过 `ipconfig`，macOS/Linux 可通过 `ifconfig`/`ip a` 查询本机局域网 IP。

### 6. 管理员账号
项目默认不预置管理员。可先注册普通用户，再在数据库中提权：
```sql
UPDATE users SET role='ADMIN' WHERE username='你的用户名';
```

### 7. 常见问题
- 无法连接数据库：确认 MySQL 已启动、端口 3306 未被占用、账号密码正确。
- 前端 404 刷新：开发模式正常；若用静态服务器托管 SPA，需要将未命中路径回退到 `index.html`。



# 定义态建模服务 - 接口使用与流程说明

## 1. MVP 使用顺序（闭环跑通）

1. **创建路线** → `POST /routes`
2. **创建版本** → `POST /route-versions`
3. **维护工序图** → `POST /graphs/process/save`（也可先 `/validate`）
4. **维护工序资源（房间）** → `POST /resources`
5. **维护工步图** → `POST /graphs/step/save`（入口必须是 `resourceRoomId`）
6. **挂载设备** → `POST /device-mounts/resource` / `POST /device-mounts/step`
7. **发布版本** → `POST /route-versions/{id}/release`

> 发布时会强制校验：工序图 + 所有资源下的工步图 + 依赖参数合法性。通过后版本冻结。

## 2. 接口说明与要点

### 2.1 路线与版本

- `POST /routes`：创建路线。
- `PUT /routes/{id}`：更新路线。
- `GET /routes/{id}`：路线详情。
- `GET /routes/list`：路线列表。
- `POST /route-versions`：创建版本（默认为 DRAFT）。
- `PUT /route-versions/{id}`：更新版本（RELEASED 不允许）。
- `GET /route-versions/{id}`：版本详情。
- `GET /route-versions/list?routeId=...`：版本列表。
- `POST /route-versions/{id}/copy`：复制版本（深拷贝工序/资源/工步/挂载）。
- `POST /route-versions/{id}/release`：发布版本（强制校验）。

### 2.2 工序画布（一级画布）

- `GET /graphs/process/{routeVersionId}`：获取工序图。
- `POST /graphs/process/save`：保存工序图。
- `POST /graphs/process/validate`：校验工序图。
- `GET /versions/{id}/ops-graph`：返回工序摘要（R/S/E 与状态）。

### 2.3 工序资源（房间）

- `POST /resources`：创建资源（必须绑定工序）。
- `PUT /resources/{id}`：更新资源。
- `DELETE /resources/{id}`：删除资源（同时删除其工步与挂载）。
- `GET /process-nodes/{processNodeId}/resources`：工序资源列表。

### 2.4 工步画布（二级画布）

- `GET /graphs/step/{resourceRoomId}`：获取工步图（入口必须是资源）。
- `POST /graphs/step/save`：保存工步图。
- `POST /graphs/step/validate`：校验工步图。

### 2.5 设备挂载与汇总

- `POST /device-mounts/resource`：资源级挂设备。
- `POST /device-mounts/step`：工步级挂设备。
- `DELETE /device-mounts/{id}`：删除挂载。
- `GET /resources/{resourceRoomId}/devices`：资源设备汇总（含来源工步）。

## 3. 发布校验逻辑（简版）

- **工序图校验**：节点唯一性、依赖引用合法、无环、依赖类型/强度合法、滞后非负。
- **资源校验**：每个工序至少一个资源。
- **工步图校验**：对每个资源的工步图进行 DAG 校验。

## 4. 流程图（Mermaid）

```mermaid
flowchart TD
    A[创建路线 /routes] --> B[创建版本 /route-versions]
    B --> C[维护工序图 /graphs/process/save]
    C --> D[维护工序资源 /resources]
    D --> E[维护工步图 /graphs/step/save]
    E --> F[挂载设备 /device-mounts/*]
    F --> G[发布版本 /route-versions/{id}/release]
    G -->|校验通过| H[状态变为 RELEASED]
    G -->|校验失败| I[返回错误清单]
```

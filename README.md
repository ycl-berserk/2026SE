# 2026SE
2026软件工程导论大作业

4.3更新：初始前端建成，在miniprogram-1文件夹下，需要通过微信开发者工具查看。另附API.md作为前后端对接参考

## 项目配置说明

### 微信小程序开发配置

1. 复制配置文件模板：
   ```bash
   cp miniprogram-1/project.config.template.json miniprogram-1/project.config.json
   ```

2. 将 `project.config.json` 中的 `YOUR_APP_ID_HERE` 替换为你的真实 AppID

3. 使用微信开发者工具打开 `miniprogram-1` 文件夹即可运行

> ⚠️ 注意：`project.config.json` 已被加入 `.gitignore`，不会被提交到仓库，请妥善保管你的 AppID
<template>
  <section class="admin-page">
    <header class="page-head">
      <div>
        <h1>用户管理</h1>
        <p>查看用户资料、认证情况和账号状态，保持平台运行稳定。</p>
      </div>
      <div class="filters">
        <input v-model.trim="filters.keyword" class="input" placeholder="搜索用户名或姓名" />
        <select v-model="filters.role" class="input">
          <option value="">全部角色</option>
          <option value="USER">普通用户</option>
          <option value="ADMIN">管理员</option>
        </select>
        <select v-model="filters.enabled" class="input">
          <option value="">全部状态</option>
          <option value="true">启用</option>
          <option value="false">禁用</option>
        </select>
        <button class="btn btn-primary" @click="loadUsers">查询</button>
      </div>
    </header>
    <section class="summary-strip">
      <article class="card summary-item">
        <span>当前结果</span>
        <strong>{{ adminStore.users.length }}</strong>
      </article>
      <article class="card summary-item">
        <span>角色筛选</span>
        <strong>{{ roleLabel(filters.role) }}</strong>
      </article>
    </section>
    <p v-if="adminStore.errorMessage" class="error">{{ adminStore.errorMessage }}</p>
    <section class="card table-card">
      <div v-if="!adminStore.users.length" class="empty-state">当前没有符合条件的用户记录。</div>
      <table>
        <thead>
          <tr>
            <th>用户</th>
            <th>角色</th>
            <th>认证</th>
            <th>状态</th>
            <th>发布数</th>
            <th>订单数</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody v-if="adminStore.users.length">
          <tr v-for="item in adminStore.users" :key="item.id">
            <td>{{ item.name }}（{{ item.username }}）</td>
            <td>{{ roleLabel(item.role) }}</td>
            <td><span class="status-pill" :class="item.verified ? 'verified' : 'pending'">{{ item.verified ? "已认证" : "未认证" }}</span></td>
            <td><span class="status-pill" :class="item.enabled ? 'enabled' : 'disabled'">{{ item.enabled ? "启用" : "禁用" }}</span></td>
            <td>{{ item.publishCount }}</td>
            <td>{{ item.orderCount }}</td>
            <td class="actions">
              <button class="btn btn-muted" @click="toggleVerify(item)">
                {{ item.verified ? "取消认证" : "设为认证" }}
              </button>
              <button class="btn btn-accent" @click="toggleEnabled(item)">
                {{ item.enabled ? "禁用" : "启用" }}
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </section>
  </section>
</template>

<script setup>
import { onMounted, reactive } from "vue";
import { useAdminStore } from "@/stores/admin";

const adminStore = useAdminStore();
const filters = reactive({
  keyword: "",
  role: "",
  enabled: ""
});

async function loadUsers() {
  await adminStore.loadUsers({
    ...filters,
    enabled: filters.enabled === "" ? undefined : filters.enabled === "true"
  });
}

async function toggleVerify(item) {
  await adminStore.updateUserVerify(item.id, !item.verified);
}

async function toggleEnabled(item) {
  await adminStore.updateUserEnabled(item.id, !item.enabled);
}

function roleLabel(role) {
  const labels = {
    "": "全部角色",
    USER: "普通用户",
    ADMIN: "管理员"
  };
  return labels[role] || role || "普通用户";
}

onMounted(loadUsers);
</script>

<style scoped>
.admin-page {
  display: grid;
  gap: 16px;
}

.page-head {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  align-items: flex-start;
}

.page-head h1 {
  margin: 0;
  font-family: var(--font-display);
}

.page-head p {
  margin: 8px 0 0;
  color: var(--muted);
}

.filters {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.summary-strip {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 12px;
}

.summary-item {
  padding: 16px 18px;
  display: grid;
  gap: 8px;
}

.summary-item span {
  color: var(--muted);
  font-size: 13px;
}

.summary-item strong {
  font-size: 24px;
}

.table-card {
  padding: 10px;
  overflow: auto;
}

.empty-state {
  padding: 28px 18px 18px;
  color: var(--muted);
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  padding: 12px;
  border-bottom: 1px solid var(--border);
  text-align: left;
}

.actions {
  display: flex;
  gap: 8px;
}

.status-pill {
  display: inline-flex;
  align-items: center;
  padding: 5px 10px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 700;
}

.status-pill.verified,
.status-pill.enabled {
  color: var(--success);
  background: rgba(22, 163, 74, 0.1);
}

.status-pill.pending,
.status-pill.disabled {
  color: var(--danger);
  background: rgba(220, 38, 38, 0.08);
}

.error {
  margin: 0;
  color: var(--danger);
}
</style>

<template>
  <section class="admin-page">
    <header class="page-head">
      <div>
        <h1>商品管理</h1>
        <p>查看商品发布情况，并对异常或无效商品进行处理。</p>
      </div>
      <div class="filters">
        <input v-model.trim="filters.keyword" class="input" placeholder="搜索商品名称" />
        <select v-model="filters.status" class="input">
          <option value="">全部状态</option>
          <option value="AVAILABLE">在售</option>
          <option value="SOLD">已售出</option>
          <option value="RESERVED">已预留</option>
        </select>
        <button class="btn btn-primary" @click="loadProducts">查询</button>
      </div>
    </header>
    <section class="summary-strip">
      <article class="card summary-item">
        <span>当前结果</span>
        <strong>{{ adminStore.products.length }}</strong>
      </article>
      <article class="card summary-item">
        <span>筛选状态</span>
        <strong>{{ statusLabel(filters.status) }}</strong>
      </article>
    </section>
    <p v-if="adminStore.errorMessage" class="error">{{ adminStore.errorMessage }}</p>
    <section class="card table-card">
      <div v-if="!adminStore.products.length" class="empty-state">当前没有符合条件的商品记录。</div>
      <table>
        <thead>
          <tr>
            <th>商品</th>
            <th>校区</th>
            <th>状态</th>
            <th>价格</th>
            <th>卖家</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody v-if="adminStore.products.length">
          <tr v-for="item in adminStore.products" :key="item.id">
            <td>{{ item.title }}</td>
            <td>{{ item.campus }}</td>
            <td><span class="status-pill" :class="statusClass(item.status)">{{ statusLabel(item.status) }}</span></td>
            <td>¥{{ item.price }}</td>
            <td>{{ item.seller?.name || "未知卖家" }}</td>
            <td class="actions">
              <button class="btn btn-muted" @click="toggleStatus(item)">
                {{ item.status === "AVAILABLE" ? "下架" : "上架" }}
              </button>
              <button class="btn btn-accent" @click="deleteProduct(item.id)">删除</button>
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
  status: ""
});

async function loadProducts() {
  await adminStore.loadProducts(filters);
}

async function toggleStatus(item) {
  const nextStatus = item.status === "AVAILABLE" ? "SOLD" : "AVAILABLE";
  await adminStore.updateProductStatus(item.id, nextStatus);
}

async function deleteProduct(id) {
  await adminStore.deleteProduct(id);
}

function statusLabel(status) {
  const labels = {
    "": "全部商品",
    AVAILABLE: "在售",
    SOLD: "已售出",
    RESERVED: "已预留"
  };
  return labels[status] || status || "全部商品";
}

function statusClass(status) {
  return {
    available: status === "AVAILABLE",
    sold: status === "SOLD",
    reserved: status === "RESERVED"
  };
}

onMounted(loadProducts);
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
  background: var(--surface-soft);
}

.status-pill.available {
  color: var(--success);
  background: rgba(22, 163, 74, 0.1);
}

.status-pill.sold {
  color: var(--danger);
  background: rgba(220, 38, 38, 0.08);
}

.status-pill.reserved {
  color: #b76a00;
  background: rgba(234, 109, 39, 0.12);
}

.error {
  margin: 0;
  color: var(--danger);
}
</style>

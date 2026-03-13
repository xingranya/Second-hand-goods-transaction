<template>
  <section class="admin-page">
    <header class="page-head">
      <div>
        <h1>订单管理</h1>
        <p>统一查看订单状态、交易双方和金额信息，便于快速定位异常订单。</p>
      </div>
      <div class="filters">
        <select v-model="filters.status" class="input">
          <option value="">全部状态</option>
          <option value="PENDING">已下单</option>
          <option value="PAID">已付款</option>
          <option value="SHIPPED">已发货</option>
          <option value="RECEIVED">待收货</option>
          <option value="COMPLETED">待评价</option>
          <option value="CANCELLED">已取消</option>
        </select>
        <button class="btn btn-primary" @click="loadOrders">查询</button>
      </div>
    </header>
    <section class="summary-strip">
      <article class="card summary-item">
        <span>当前结果</span>
        <strong>{{ adminStore.orders.length }}</strong>
      </article>
      <article class="card summary-item">
        <span>筛选状态</span>
        <strong>{{ statusLabel(filters.status) }}</strong>
      </article>
    </section>
    <p v-if="adminStore.errorMessage" class="error">{{ adminStore.errorMessage }}</p>
    <section class="card table-card">
      <div v-if="!adminStore.orders.length" class="empty-state">当前没有符合条件的订单记录。</div>
      <table>
        <thead>
          <tr>
            <th>订单号</th>
            <th>商品</th>
            <th>买家</th>
            <th>卖家</th>
            <th>状态</th>
            <th>金额</th>
            <th>时间</th>
          </tr>
        </thead>
        <tbody v-if="adminStore.orders.length">
          <tr v-for="item in adminStore.orders" :key="item.id">
            <td>{{ item.id }}</td>
            <td>{{ item.items?.[0]?.title || "未命名商品" }}</td>
            <td>{{ item.buyer?.name || "未知买家" }}</td>
            <td>{{ item.seller?.name || "未知卖家" }}</td>
            <td><span class="status-pill">{{ item.status }}</span></td>
            <td>¥{{ item.totalAmount }}</td>
            <td>{{ item.createTime || "-" }}</td>
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
  status: ""
});

async function loadOrders() {
  await adminStore.loadOrders(filters);
}

function statusLabel(status) {
  const labels = {
    "": "全部订单",
    PENDING: "已下单",
    PAID: "已付款",
    SHIPPED: "已发货",
    RECEIVED: "待收货",
    COMPLETED: "待评价",
    CANCELLED: "已取消"
  };
  return labels[status] || status || "全部订单";
}

onMounted(loadOrders);
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

.status-pill {
  display: inline-flex;
  align-items: center;
  padding: 5px 10px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 700;
  color: var(--primary);
  background: var(--surface-soft);
}

.error {
  margin: 0;
  color: var(--danger);
}
</style>

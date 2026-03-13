<template>
  <section class="card profile-card">
    <div class="profile-top">
      <div>
        <p class="eyebrow">个人工作台</p>
        <div class="name-row">
          <h2>{{ profile?.name || "未登录用户" }}</h2>
          <span class="tag">{{ profile?.verified ? "已认证" : "未认证" }}</span>
        </div>
        <p class="meta">
          {{ profile?.school || "暂未绑定学校" }} · {{ profile?.role === "ADMIN" ? "管理员" : "校园用户" }}
        </p>
      </div>
      <div class="status-card">
        <strong>{{ profile?.enabled === false ? "账号受限" : "账号正常" }}</strong>
        <span>未读消息 {{ profile?.unreadMessageCount ?? 0 }}</span>
      </div>
    </div>

    <div class="stats">
      <div>
        <strong>{{ profile?.publishCount ?? 0 }}</strong>
        <span>我发布的</span>
      </div>
      <div>
        <strong>{{ profile?.soldCount ?? 0 }}</strong>
        <span>已完成交易</span>
      </div>
      <div>
        <strong>{{ profile?.wantedCount ?? 0 }}</strong>
        <span>我的求购</span>
      </div>
      <div>
        <strong>{{ profile?.orderCount ?? 0 }}</strong>
        <span>相关订单</span>
      </div>
    </div>
  </section>
</template>

<script setup>
defineProps({
  profile: {
    type: Object,
    default: null
  }
});
</script>

<style scoped>
.profile-card {
  padding: 22px;
  display: grid;
  gap: 18px;
}

.profile-top {
  display: flex;
  justify-content: space-between;
  gap: 16px;
}

.eyebrow {
  margin: 0 0 8px;
  color: var(--primary);
  font-size: 12px;
  letter-spacing: 0.18em;
  text-transform: uppercase;
  font-weight: 800;
}

.name-row {
  display: flex;
  align-items: center;
  gap: 10px;
}

h2 {
  margin: 0;
  font-family: var(--font-display);
}

.meta {
  margin: 8px 0 0;
  color: var(--muted);
}

.status-card {
  min-width: 160px;
  padding: 14px 16px;
  border-radius: 18px;
  background: var(--surface-soft);
  display: grid;
  gap: 6px;
}

.status-card strong {
  color: var(--primary-strong);
}

.status-card span {
  color: var(--muted);
  font-size: 13px;
}

.stats {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 12px;
}

.stats div {
  background: linear-gradient(180deg, rgba(237, 244, 255, 0.84), rgba(255, 255, 255, 0.92));
  border-radius: 16px;
  padding: 14px 12px;
  text-align: center;
}

.stats strong {
  display: block;
  font-size: 26px;
  color: var(--primary);
}

.stats span {
  font-size: 12px;
  color: var(--muted);
}

@media (max-width: 760px) {
  .profile-top {
    flex-direction: column;
  }

  .stats {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}
</style>

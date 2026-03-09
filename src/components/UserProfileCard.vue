<template>
  <section class="card profile-card">
    <div class="name-row">
      <h2>{{ profile?.name || "未登录用户" }}</h2>
      <span class="tag">{{ profile?.verified ? "已认证" : "未认证" }}</span>
    </div>
    <p>{{ profile?.school || "暂未绑定学校" }}</p>
    <div class="stats">
      <div>
        <strong>{{ profile?.publishCount ?? 0 }}</strong>
        <span>我发布的</span>
      </div>
      <div>
        <strong>{{ profile?.soldCount ?? 0 }}</strong>
        <span>我卖出的</span>
      </div>
      <div>
        <strong>{{ profile?.favoriteCount ?? 0 }}</strong>
        <span>我的收藏</span>
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
  padding: 18px;
}

.name-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
}

h2 {
  margin: 0;
}

p {
  margin: 6px 0 0;
  color: var(--muted);
}

.stats {
  margin-top: 14px;
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 10px;
}

.stats div {
  background: var(--surface-soft);
  border-radius: 10px;
  padding: 10px;
  text-align: center;
}

.stats strong {
  display: block;
  font-size: 22px;
  color: var(--primary);
}

.stats span {
  font-size: 12px;
  color: var(--muted);
}
</style>

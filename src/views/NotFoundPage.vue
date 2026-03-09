<template>
  <section class="page-container not-found">
    <article class="card box">
      <h1>404</h1>
      <p>页面不存在，返回首页继续逛逛。</p>
      <RouterLink class="btn btn-primary" to="/">返回首页</RouterLink>
    </article>
  </section>
</template>

<script setup>
import { RouterLink } from "vue-router";
</script>

<style scoped>
.not-found {
  padding-top: 24px;
}

.box {
  padding: 40px;
  text-align: center;
}

h1 {
  margin: 0;
  font-size: 64px;
  color: var(--primary);
}

p {
  margin: 10px 0 16px;
  color: var(--muted);
}
</style>
